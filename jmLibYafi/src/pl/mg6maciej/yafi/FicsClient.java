/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6maciej.yafi;

import pl.mg6maciej.chess.PositionWrapper;
import java.io.IOException;
import pl.mg6maciej.ArrayUtils;
import pl.mg6maciej.StringUtils;
import pl.mg6maciej.chess.GameInfo;

/**
 * @author mg6maciej
 */
public class FicsClient implements TsListener {

	private Stream stream;
	private StringBuffer buffer;
	
	private String username;
	private String password;
	private boolean timeseal;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public FicsClient() {
	}

	public void connect(String username, String password, boolean timeseal) {
		this.username = username;
		this.password = password;
		this.timeseal = timeseal;
		fireDebugMessage("before connecting");
		Thread t = new Thread(new Runnable() {
			public void run() {
				loop();
			}
		});
		t.start();
	}

	public void write(String data) {
		if (connected()) {
			try {
				//fireDebugMessage("sending data: " + data);
				stream.write(data.getBytes());
			} catch (IOException ex) {
				fireErrorSendingData(ex, data);
				ex.printStackTrace();
			}
		} else {
			fireDebugMessage("not sending (not connected): " + data);
		}
	}

	public boolean connected() {
		return stream != null && stream.connected();
	}

	public long getRecvBytes() {
		if (stream == null) {
			return 0;
		}
		return stream.getBytesReceived();
	}

	public long getSentBytes() {
		if (stream == null) {
			return 0;
		}
		return stream.getBytesSent();
	}

	private void loop() {
		try {
			try {
				fireDebugMessage("connecting");
				stream = createStream();
				fireDebugMessage("connected");
				// compressmove audiochat    seekremove   defprompt
				// lock         startpos     block        gameinfo
				// [xdr]        pendinfo     graph        seekinfo
				// extascii     nohighlight  vthighlight  showserver
				// pin          ms           pinginfo     boardinfo
				// extuserinfo  seekca       showownseek  premove
				// smartmove    movecase     suicide      crazyhouse
				// losers       wildcastle   fr           nowrap
				// allresults   [obsping]    singleboard
				write("%b00010000000000000100000000000001000\n");
				fireDebugMessage("ivars sent");
				buffer = new StringBuffer();
				state = CONNECTED;
				setErrorMessage(null);
				int b = stream.read();
				while (b != -1) {
					parse(b);
					if (getErrorMessage() != null) {
						break;
					}
					b = stream.read();
				}
				fireDebugMessage("buffer is:\n'''" + buffer.toString() + "'''");
				fireDisconnected();
			} finally {
				buffer = null;
				if (stream != null) {
					stream.close();
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			fireDisconnectedWithError(ex);
		}
	}

	private Stream createStream() throws IOException {
		try {
			if (timeseal) {
				TimesealStream ts = new TimesealStream("freechess.org", 5000);
				ts.setListener(this);
				return ts;
			} else {
				return new PlainStream("freechess.org", 5000);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			if (timeseal) {
				TimesealStream ts = new TimesealStream("69.36.243.188", 23);
				ts.setListener(this);
				return ts;
			} else {
				return new PlainStream("69.36.243.188", 23);
			}
		}
	}

	private static byte[] prompt = "\nfics% ".getBytes();
	private int[] promptBuffer = new int[prompt.length];
	private int pbIndex = 0;

	private void parse(int b) {
		if (b < ' ' && b != '\n' || b > '~') {
			if (!buffer.toString().startsWith("\n             _       __     __                             __")) {
				System.out.println("--------------------");
				System.out.println(buffer);
				System.out.println(b);
				System.out.println("--------------------");
			}
			return;
		}
		buffer.append((char) b);
		if (state == LOGGED_IN) {
			promptBuffer[pbIndex++] = b;
			pbIndex %= prompt.length;
			for (int i = prompt.length - 1; i >= 0; i--) {
				if (i == 0 && buffer.length() == prompt.length - 1) {
					buffer.setLength(0);
					return;
				}
				if (prompt[i] != promptBuffer[(i + pbIndex) % prompt.length]) {
					return;
				}
			}
			buffer.setLength(buffer.length() - prompt.length + 1);
			String block = buffer.toString();
			buffer.setLength(0);
			parse(block);
		} else {
			tryLogin();
		}
	}
	
	private int state;
	private static final int CONNECTED = 1;
	private static final int AFTER_USERNAME = 2;
	private static final int AFTER_PASSWORD = 3;
	private static final int STARTING_SESSION = 4;
	private static final int LOGGED_IN = 5;
	
	private void tryLogin() {
		char last = buffer.charAt(buffer.length() - 1);
		switch (state) {
			case CONNECTED:
				if (last == ' ' && buffer.toString().endsWith("\nlogin: ")) {
					fireDebugMessage("send username");
					buffer.setLength(0);
					write(username + "\n");
					state = AFTER_USERNAME;
				}
				break;
			case AFTER_USERNAME:
				if (last == ' ' && buffer.toString().endsWith("\npassword: ")) {
					fireDebugMessage("send password");
					buffer.setLength(0);
					write(password + "\n");
					state = AFTER_PASSWORD;
				} else if (last == '"' && buffer.toString().endsWith("\nPress return to enter the server as \"")) {
					fireDebugMessage("confirm guest login");
					buffer.setLength(0);
					write("y\n");
					state = STARTING_SESSION;
				} else if (last == '.' && buffer.toString().endsWith("  Try again.")) {
					fireDebugMessage("error after username");
					String tmp = buffer.toString();
					int lastNewline = tmp.lastIndexOf('\n');
					tmp = tmp.substring(lastNewline + 1, tmp.length() - "  Try again.".length());
					setErrorMessage(tmp);
				} else if (last == '*' && buffer.toString().endsWith("\n**** LOGIN TIMEOUT ****")) {
					fireDebugMessage("timeout after username");
					setErrorMessage("**** LOGIN TIMEOUT ****");
				}
				break;
			case AFTER_PASSWORD:
				if (last == ' ' && buffer.toString().endsWith("\n**** Starting FICS session as ")) {
					fireDebugMessage("starting session - registered");
					buffer.setLength(0);
					state = STARTING_SESSION;
				} else if (last == '*' && buffer.toString().endsWith("\n**** Invalid password! ****")) {
					fireDebugMessage("invalid password");
					setErrorMessage("**** Invalid password! ****");
				} else if (last == 'g' && buffer.toString().endsWith(".\nPlease email: abuse@freechess.org")) {
					fireDebugMessage("error after password");
					String tmp = buffer.toString();
					int indexPlayer = tmp.indexOf("Player \"");
					tmp = tmp.substring(indexPlayer);
					setErrorMessage(tmp);
				} else if (last == '*' && buffer.toString().endsWith("\n**** LOGIN TIMEOUT ****")) {
					fireDebugMessage("timeout after password");
					setErrorMessage("**** LOGIN TIMEOUT ****");
				}
				break;
			case STARTING_SESSION:
				if (last == ' ' && buffer.toString().endsWith("\n**** Starting FICS session as ")) {
					fireDebugMessage("starting session - guest");
					buffer.setLength(0);
				} else if (last == '*' && buffer.toString().endsWith("\n**** LOGIN TIMEOUT ****")) {
					fireDebugMessage("timeout starting session");
					setErrorMessage("**** LOGIN TIMEOUT ****");
				} else if (last == '\n' && buffer.toString().endsWith(" ****\n")) {
					fireDebugMessage("logged on");
					buffer.setLength(buffer.length() - " ****\n".length());
					String handle = buffer.toString();
					buffer.setLength(0);
					int indexTitles = handle.indexOf('(');
					String titles;
					if (indexTitles != -1) {
						titles = handle.substring(indexTitles);
						handle = handle.substring(0, indexTitles);
					} else {
						titles = StringUtils.EMPTY;
					}
					write("set interface Yafi 0.2.5\n");
					state = LOGGED_IN;
					username = handle;
					fireConnected();
//					System.out.println("Handle is: [" + handle + "]");
//					System.out.println("Titles is: [" + titles + "]");
				}
				break;
		}
	}
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private void parse(String block) {
		if (block == null) {
			return;
		}
		int indexSeekRemove = block.indexOf("\n<sr> ");
		if (indexSeekRemove != -1) {
			int seekRemoveStart = indexSeekRemove + "\n<sr> ".length();
			int seekRemoveEnd = block.indexOf('\n', seekRemoveStart);
			String seekRemove = block.substring(seekRemoveStart, seekRemoveEnd);
			block = block.substring(0, indexSeekRemove) + block.substring(seekRemoveEnd + 1);
			String[] data = StringUtils.split(seekRemove, ' ');
			int[] seekNumbers = new int[data.length];
			try {
				for (int i = 0; i < data.length; i++) {
					seekNumbers[i] = Integer.parseInt(data[i]);
				}
				fireSeeksRemoved(seekNumbers);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		if (block.startsWith("\n<s> ")) {
			//<s> 81 w=GuestYPDN ti=01 rt=0P t=4 i=10 r=u tp=blitz c=? rr=0-9999 a=t f=t
			int seekStart = "\n<s> ".length();
			int seekEnd = block.indexOf('\n', seekStart);
			String seek = block.substring(seekStart, seekEnd);
			try {
				SeekInfo seekInfo = SeekInfo.fromFicsData(seek);
				fireSeek(seekInfo);
				if (seekEnd + 1 == block.length()) {
					block = StringUtils.EMPTY;
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		if (block.startsWith("seekinfo set.\n<sc>\n<s> ")) {
			String seeks = block.substring("seekinfo set.\n<sc>\n<s> ".length(), block.length() - 1);
			String[] data = StringUtils.split(seeks, "\n<s> ");
			for (int i = 0; i < data.length; i++) {
				SeekInfo seekInfo = SeekInfo.fromFicsData(data[i]);
				fireSeek(seekInfo);
			}
			block = StringUtils.EMPTY;
		}
		int indexStyle12 = block.indexOf("\n<12> ");
		if (indexStyle12 != -1) {
			int style12Start = indexStyle12 + "\n<12> ".length();
			int style12End = block.indexOf('\n', style12Start);
			String style12 = block.substring(style12Start, style12End);
			block = block.substring(0, indexStyle12) + block.substring(style12End + 1);
			try {
				PositionWrapper position = PositionWrapper.fromStyle12(style12);
				firePositionReceived(position);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		int indexGameInfo1 = block.indexOf("\n<g1> ");
		if (indexGameInfo1 != -1) {
			int gameInfoStart = indexGameInfo1 + "\n<g1> ".length();
			int gameInfoEnd = block.indexOf('\n', gameInfoStart);
			String strGameInfo = block.substring(gameInfoStart, gameInfoEnd);
			block = block.substring(0, indexGameInfo1) + block.substring(gameInfoEnd + 1);
			try {
				GameInfo gameInfo = GameInfo.fromFics(strGameInfo);
				fireGameInfoReceived(gameInfo);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		int indexGameInfo = block.indexOf("\n{Game ");
		if (indexGameInfo != -1) {
			int gameNumberStart = indexGameInfo + "\n{Game ".length();
			int gameNumberEnd = block.indexOf(' ', gameNumberStart);
			try {
				int gameNumber = Integer.parseInt(block.substring(gameNumberStart, gameNumberEnd));
				int resultEnd = block.indexOf('\n', gameNumberEnd);
				int resultStart = block.lastIndexOf(' ', resultEnd) + 1;
				String result = block.substring(resultStart, resultEnd);
				if (ArrayUtils.contains(new String[] {"*", "1/2-1/2", "1-0", "0-1"}, result)) {
					int descriptionStart = block.indexOf(") ") + ") ".length();
					int descriptionEnd = resultStart - "} ".length();
					String description = block.substring(descriptionStart, descriptionEnd);
					fireGameEnded(gameNumber, result, description);
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		int indexCreating = block.indexOf("\nCreating: ");
		if (indexCreating == -1) {
			indexCreating = block.indexOf("\nContinuing: ");
		}
		if (indexCreating != -1) {
			firePlayingGameStarted();
		}
		if (block.startsWith("Game ")) {
			int gameNumberStart = "Game ".length();
			int gameNumberEnd = block.indexOf(": ", gameNumberStart);
			if (gameNumberEnd != -1) {
				try {
					String strGameNumber = block.substring(gameNumberStart, gameNumberEnd);
					int gameNumber = Integer.parseInt(strGameNumber);
					int gameCommentStart = gameNumberEnd + ": ".length();
					int gameCommentEnd = block.indexOf('\n', gameCommentStart);
					String strGameComment = block.substring(gameCommentStart, gameCommentEnd);
					if (strGameComment.endsWith(" goes forward 1 move.")
							|| strGameComment.endsWith(" backs up 1 move.")) {
						block = block.substring(gameCommentEnd + 1);
					}
					fireGameCommentReceived(gameNumber, strGameComment);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		int indexGameComment = block.indexOf("\nGame ");
		if (indexGameComment != -1) {
			int gameNumberStart = indexGameComment + "\nGame ".length();
			int gameNumberEnd = block.indexOf(": ", gameNumberStart);
			if (gameNumberEnd != -1) {
				try {
					String strGameNumber = block.substring(gameNumberStart, gameNumberEnd);
					int gameNumber = Integer.parseInt(strGameNumber);
					int gameCommentStart = gameNumberEnd + ": ".length();
					int gameCommentEnd = block.indexOf('\n', gameCommentStart);
					String strGameComment = block.substring(gameCommentStart, gameCommentEnd);
//					if (strGameComment.endsWith(" goes forward 1 move.")
//							|| strGameComment.endsWith(" backs up 1 move.")) {
//						block = block.substring(gameCommentEnd + 1);
//					}
					fireGameCommentReceived(gameNumber, strGameComment);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		if (block.startsWith("\n")) {
			if (block.indexOf('\n', 1) == block.length() - 1) {
				int indexTell = block.indexOf(" tells you: ");
				if (indexTell != -1) {
					String handle = block.substring(1, indexTell);
					int indexTitles = handle.indexOf('(');
					String titles;
					if (indexTitles != -1) {
						titles = handle.substring(indexTitles);
						handle = handle.substring(0, indexTitles);
					} else {
						titles = StringUtils.EMPTY;
					}
					if (FicsUtils.isValidHandle(handle) && FicsUtils.isValidTitles(titles)) {
						int messageStart = indexTell + " tells you: ".length();
						int messageEnd = block.length() - 1;
						String message = block.substring(messageStart, messageEnd);
						int type = DiscussionStatement.TELL;
						String id = handle;
						DiscussionStatement ds = new DiscussionStatement(id, handle, message, type);
						fireDiscussionStatementReceived(ds);
					}
				}
				int indexChannelTell = block.indexOf("): ");
				if (indexChannelTell != -1) {
					int channelStart = block.lastIndexOf('(', indexChannelTell);
					if (channelStart != -1) {
						String channel = block.substring(channelStart + 1, indexChannelTell);
						if (FicsUtils.isValidChannel(channel)) {
							String handle = block.substring(1, channelStart);
							int indexTitles = handle.indexOf('(');
							String titles;
							if (indexTitles != -1) {
								titles = handle.substring(indexTitles);
								handle = handle.substring(0, indexTitles);
							} else {
								titles = StringUtils.EMPTY;
							}
							if (FicsUtils.isValidHandle(handle) && FicsUtils.isValidTitles(titles)) {
								int messageStart = indexChannelTell + "): ".length();
								int messageEnd = block.length() - 1;
								String message = block.substring(messageStart, messageEnd);
								int type = DiscussionStatement.CHANNEL_TELL;
								String id = channel;
								DiscussionStatement ds = new DiscussionStatement(id, handle, message, type);
								fireDiscussionStatementReceived(ds);
							}
						}
					}
				}
			}
		}
		if (block.length() > 0) {
			fireBlockReceived(block);
		}
	}

	public void debug(String message) {
		fireDebugMessage(message);
	}

	public interface OutputListener {

		void connected();

		void disconnected();

		void disconnectedWithError(Throwable ex);

		void errorSendingData(Throwable ex, String data);

		void positionReceived(PositionWrapper position);

		void gameInfoReceived(GameInfo gameInfo);

		void gameCommentReceived(int gameNumber, String gameComment);

		void playingGameStarted();

		void gameEnded(int gameNumber, String result, String description);
		
		void seeksRemoved(int[] seekNumbers);
		
		void seek(SeekInfo seekInfo);
		
		void discussionStatementReceived(DiscussionStatement ds);
		
		void blockReceived(String block);
		
		void debugMessage(String message);
	}

	private OutputListener listener;

	public void setListener(OutputListener l) {
		this.listener = l;
	}

	private void fireConnected() {
		if (listener != null) {
			listener.connected();
		}
	}

	private void fireDisconnected() {
		if (listener != null) {
			listener.disconnected();
		}
	}

	private void fireDisconnectedWithError(Throwable ex) {
		if (listener != null) {
			listener.disconnectedWithError(ex);
		}
	}

	private void fireErrorSendingData(Throwable ex, String data) {
		if (listener != null) {
			listener.errorSendingData(ex, data);
		}
	}

	private void firePositionReceived(PositionWrapper position) {
		if (listener != null) {
			listener.positionReceived(position);
		}
	}

	private void fireGameInfoReceived(GameInfo gameInfo) {
		if (listener != null) {
			listener.gameInfoReceived(gameInfo);
		}
	}

	private void fireGameCommentReceived(int gameNumber, String gameComment) {
		if (listener != null) {
			listener.gameCommentReceived(gameNumber, gameComment);
		}
	}

	private void firePlayingGameStarted() {
		if (listener != null) {
			listener.playingGameStarted();
		}
	}

	private void fireGameEnded(int gameNumber, String result, String description) {
		if (listener != null) {
			listener.gameEnded(gameNumber, result, description);
		}
	}
	
	private void fireSeeksRemoved(int[] seekNumbers) {
		if (listener != null) {
			listener.seeksRemoved(seekNumbers);
		}
	}
	
	private void fireSeek(SeekInfo seekInfo) {
		if (listener != null) {
			listener.seek(seekInfo);
		}
	}
	
	private void fireDiscussionStatementReceived(DiscussionStatement ds) {
		if (listener != null) {
			listener.discussionStatementReceived(ds);
		}
	}

	private void fireBlockReceived(String block) {
		if (listener != null) {
			listener.blockReceived(block);
		}
	}
	
	private void fireDebugMessage(String message) {
		if (listener != null) {
			listener.debugMessage(message);
		}
	}
}
