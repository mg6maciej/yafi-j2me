
package pl.mg6maciej.yafi;

import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import pl.mg6maciej.AbstractCanvas;
import pl.mg6maciej.AbstractMidlet;
import pl.mg6maciej.DateUtils;
import pl.mg6maciej.GraphicsUtils;
import pl.mg6maciej.HtmlEntityEncoder;
import pl.mg6maciej.LoginScreen;
import pl.mg6maciej.ResourceBundle;
import pl.mg6maciej.ScrollableTextCanvas;
import pl.mg6maciej.StringUtils;
import pl.mg6maciej.chess.GameInfo;
import pl.mg6maciej.chess.PositionWrapper;

/**
 * @author mg6maciej
 */
public class YafiMidlet extends AbstractMidlet implements CommandListener,
		BoardScreen.MoveListener, FicsClient.OutputListener {
	private static final ResourceBundle resources = new ResourceBundleEn();

	//private static final Command backCmd = new Command(getString("cmd back"), Command.BACK, 0);
	//private static final Command exitCmd = new Command(getString("cmd exit"), Command.EXIT, 0);
//	private static final Command castleShortCmd = new Command(getString("cmd castle short"), Command.OK, 0);
//	private static final Command castleLongCmd = new Command(getString("cmd castle long"), Command.OK, 0);
	private static final Command flipCmd = new Command(getString("cmd flip"), Command.OK, 6);
	private static final Command drawCmd = new Command(getString("cmd draw"), Command.OK, 2);
	private static final Command resignCmd = new Command(getString("cmd resign"), Command.OK, 3);
	private static final Command forwardCmd = new Command(getString("cmd forward"), Command.OK, 2);
	private static final Command backwardCmd = new Command(getString("cmd backward"), Command.OK, 3);
	private static final Command unexamineCmd = new Command(getString("cmd unexamine"), Command.OK, 4);
	private static final Command unobserveCmd = new Command(getString("cmd unobserve"), Command.OK, 2);
//	private static final Command showConsoleCmd = new Command(getString("cmd show console"), Command.SCREEN, 0);
//	private static final Command showStatisticsCmd = new Command(getString("cmd show statistics"), Command.SCREEN, 0);
//	private static final Command showAliasesCmd = new Command(getString("cmd show aliases"), Command.SCREEN, 0);
//	private static final Command showLoginScriptCmd = new Command(getString("cmd show login script"), Command.SCREEN, 0);
//	private static final Command incBoardSizeCmd = new Command(getString("cmd inc board size"), Command.OK, 0);
//	private static final Command decBoardSizeCmd = new Command(getString("cmd dec board size"), Command.OK, 0);
	private static final Command incFontSizeCmd = new Command(getString("cmd inc font size"), Command.OK, 2);
	private static final Command decFontSizeCmd = new Command(getString("cmd dec font size"), Command.OK, 3);
	private static final Command sendCmd = new Command(getString("cmd send"), Command.ITEM, 2);
	private static final Command cleanCmd = new Command(getString("cmd clean"), Command.OK, 3);
	private static final Command clearCmd = new Command(getString("cmd clear"), Command.ITEM, 0);
//	private static final Command refreshCmd = new Command(getString("cmd refresh"), Command.OK, 0);
	private static final Command cancelCmd = new Command(getString("cmd cancel"), Command.CANCEL, 1);
	private static final Command okCmd = new Command(getString("cmd ok"), Command.OK, 2);
//	private static final Command fullscreenCmd = new Command(getString("cmd fullscreen"), Command.SCREEN, 0);
//	private static final Command insertNewlineCmd = new Command(getString("cmd insert newline"), Command.ITEM, 0);
	private static final Command addCmd = new Command(getString("cmd add"), Command.OK, 3);
	private static final Command editCmd = new Command(getString("cmd edit"), Command.OK, 2);
	private static final Command removeCmd = new Command(getString("cmd remove"), Command.OK, 4);
	private static final Command switchToCmd = new Command(getString("cmd show screens"), Command.BACK, 1);
	private static final Command selectCmd = new Command(getString("cmd select"), Command.ITEM, 0);
	private static final Command connectCmd = new Command(getString("cmd connect"), Command.OK, 0);
	private static final Command chatCmd = new Command(getString("cmd chat"), Command.SCREEN, 0);
	private static final Command matchCmd = new Command(getString("cmd match"), Command.SCREEN, 0);
	private static final Command chatPrivatelyCmd = new Command(getString("cmd chat privately"), Command.SCREEN, 0);
	private static final Command playCmd = new Command(getString("cmd play"), Command.OK, 0);

	private final StringItem recvBytesField = new StringItem(getString("label received"), null);
	private final StringItem sentBytesField = new StringItem(getString("label sent"), null);
	private final StringItem freeMemoryField = new StringItem(getString("label free memory"), null);
	private final StringItem usedMemoryField = new StringItem(getString("label used memory"), null);
	private final StringItem freeStorageField = new StringItem(getString("label free storage"), null);
	private final StringItem usedStorageField = new StringItem(getString("label used storage"), null);

	//private final StringItem outputField = new StringItemstatic(null, StringUtils.EMPTY);

	private final TextField aliasNameField = new TextField(getString("label alias name"), null, 32, TextField.ANY);
	private final TextField aliasCommandsField = new TextField(getString("label alias commands"), null, 5000, TextField.ANY);
	
	private final TextField promptItem = new TextField(getString("label prompt"), null, 32, TextField.ANY);
	private final Gauge boardSizeItem = new Gauge(getString("label board size"), true, BoardScreen.getSizesCount(), 0);
	private final Gauge boardFontSizeItem = new Gauge(getString("label board font size"), true, 2, 0);
	private final Gauge consoleFontSizeItem = new Gauge(getString("label console font size"), true, 2, 0);
	private final ChoiceGroup pointerMoveStyleItem = new ChoiceGroup(getString("label pointer move style"), ChoiceGroup.EXCLUSIVE);
	private final ChoiceGroup randomOptionsItem = new ChoiceGroup(getString("label show options"), ChoiceGroup.MULTIPLE);
	private final ChoiceGroup backgroundsItem = new ChoiceGroup(getString("label backgrounds"), ChoiceGroup.EXCLUSIVE);
	
	private final TextField seekTimeField = new TextField(getString("label seek time"), "15", 3, TextField.NUMERIC);
	private final TextField seekIncrementField = new TextField(getString("label seek increment"), "0", 3, TextField.NUMERIC);
	private final ChoiceGroup seekRatedField = new ChoiceGroup(StringUtils.EMPTY, ChoiceGroup.MULTIPLE);
	private final ChoiceGroup seekTypeField = new ChoiceGroup(getString("label seek type"), ChoiceGroup.POPUP);
	private final TextField seekMinRatingField = new TextField(getString("label seek min rating"), "0", 4, TextField.NUMERIC);
	private final TextField seekMaxRatingField = new TextField(getString("label seek max rating"), "9999", 4, TextField.NUMERIC);

	private final TextField matchHandleField = new TextField(getString("label match handle"), null, 32, TextField.ANY);
	private final TextField matchTimeField = new TextField(getString("label match time"), "15", 3, TextField.NUMERIC);
	private final TextField matchIncrementField = new TextField(getString("label match increment"), "0", 3, TextField.NUMERIC);
	private final ChoiceGroup matchRatedField = new ChoiceGroup(StringUtils.EMPTY, ChoiceGroup.MULTIPLE);
	private final ChoiceGroup matchTypeField = new ChoiceGroup(getString("label match type"), ChoiceGroup.POPUP);

	private Form statiscticsScreen;
	private LoginScreen loginScreen;
	private BoardScreen boardScreen;
	//private Form consoleScreen;
	private TextBox commandLineScreen;
	private Alert confirmExitScreen;
	private AliasesList aliasesScreen;
	private ScrollableTextCanvas consoleScreen;
	private TextBox loginScriptScreen;
	private TextBox logoutScriptScreen;
	private Form editAliasScreen;
	private Alert infoScreen;
	private Form settingsScreen;
	private SoughtList soughtScreen;
	private Form seekScreen;
	private Form matchScreen;
	private ChatScreen chatScreen;
	private TextBox chatWriteScreen;
	private List screensScreen;

	private static final int layout = Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_AFTER;

	private FicsClient client;

	private Settings settings;
	
//	private Displayable rootScreen;

	private Displayable[] screens;
	
	private boolean chatPrivately;
	
//	private boolean updateChat;

	protected void initialize() {
		try {
			infoScreen = new Alert(null);
			infoScreen.setCommandListener(this);
			infoScreen.addCommand(okCmd);
			infoScreen.setTimeout(Alert.FOREVER);
			
			settings = new Settings();
			
			loginScreen = new LoginScreen(getDisplay());
//			rootScreen = loginScreen;
			loginScreen.setCommandListener(this);
			//loginScreen.addCommand(exitCmd);
			loginScreen.addCommand(connectCmd);
//			loginScreen.addCommand(showConsoleCmd);
//			loginScreen.addCommand(showAliasesCmd);
//			loginScreen.addCommand(showBoardCmd);
//			loginScreen.addCommand(showLoginScriptCmd);
//			loginScreen.addCommand(showStatisticsCmd);
			loginScreen.addCommand(switchToCmd);
			loginScreen.setUsername(settings.getUsername());
			loginScreen.setPassword(settings.getPassword());
			loginScreen.setRememberPassword(settings.getRememberPassword());
			//loginScreen.setUseLoginButton(false);
			loginScreen.setTitle(getString("title login"));
			{
				LoginScreen x = loginScreen;
				String message = "Press " + x.getKeyName(x.getKeyCode(LoginScreen.GAME_A))
						+ ", " + x.getKeyName(x.getKeyCode(LoginScreen.GAME_B))
						+ ", " + x.getKeyName(x.getKeyCode(LoginScreen.GAME_C))
						+ " or " + x.getKeyName(x.getKeyCode(LoginScreen.GAME_D))
						+ " to switch between board, chat, command line and console.";
				loginScreen.setErrorMessage(message);
			}

			boardScreen = new BoardScreen(getDisplay());
			boardScreen.setCommandListener(this);
			boardScreen.setMoveListener(this);
			//boardScreen.addCommand(exitCmd);
			//boardScreen.addCommand(drawCmd);
			//boardScreen.addCommand(resignCmd);
			boardScreen.addCommand(flipCmd);
			//boardScreen.addCommand(castleShortCmd);
			//boardScreen.addCommand(castleLongCmd);
//			boardScreen.addCommand(incBoardSizeCmd);
//			boardScreen.addCommand(decBoardSizeCmd);
//			boardScreen.addCommand(incFontSizeCmd);
//			boardScreen.addCommand(decFontSizeCmd);
			boardScreen.addCommand(switchToCmd);
//			boardScreen.addCommand(showConsoleCmd);
//			boardScreen.addCommand(showAliasesCmd);
//			boardScreen.addCommand(showLoginCmd);
//			boardScreen.addCommand(showLoginScriptCmd);
//			boardScreen.addCommand(showStatisticsCmd);
			boardScreen.setTitle(getString("title board"));
//			if (!boardScreen.hasPointerEvents()) {
//				boardScreen.setFullScreenMode(true);
//			}

			commandLineScreen = new TextBox(getString("title command line"), null, 5000, TextField.ANY);
			commandLineScreen.setCommandListener(this);
			commandLineScreen.addCommand(switchToCmd);
			commandLineScreen.addCommand(sendCmd);
			commandLineScreen.setInitialInputMode("MIDP_LOWERCASE_LATIN");

			statiscticsScreen = new Form(getString("title statistics"));
			statiscticsScreen.setCommandListener(this);
			//statiscticsScreen.addCommand(backCmd);
			statiscticsScreen.addCommand(switchToCmd);
			statiscticsScreen.append(recvBytesField);
			recvBytesField.setLayout(layout);
			statiscticsScreen.append(sentBytesField);
			sentBytesField.setLayout(layout);
			statiscticsScreen.append(freeMemoryField);
			freeMemoryField.setLayout(layout);
			statiscticsScreen.append(usedMemoryField);
			usedMemoryField.setLayout(layout);
			statiscticsScreen.append(freeStorageField);
			freeStorageField.setLayout(layout);
			statiscticsScreen.append(usedStorageField);
			usedStorageField.setLayout(layout);

			confirmExitScreen = new Alert(getString("title quit"), getString("text quit yafi"), null, null);
			confirmExitScreen.setCommandListener(this);
			confirmExitScreen.addCommand(cancelCmd);
			confirmExitScreen.addCommand(okCmd);

			aliasesScreen = new AliasesList(getDisplay());
			aliasesScreen.setCommandListener(this);
			//aliasesScreen.addCommand(backCmd);
			aliasesScreen.addCommand(addCmd);
			aliasesScreen.addCommand(editCmd);
			aliasesScreen.addCommand(removeCmd);
			aliasesScreen.addCommand(switchToCmd);
			aliasesScreen.setSelectCommand(editCmd);
			aliasesScreen.setTitle(getString("title aliases"));

			consoleScreen = new ScrollableTextCanvas(getDisplay());
			consoleScreen.setCommandListener(this);
			//consoleScreen.addCommand(backCmd);
			consoleScreen.addCommand(incFontSizeCmd);
			consoleScreen.addCommand(decFontSizeCmd);
			consoleScreen.addCommand(clearCmd);
			consoleScreen.addCommand(switchToCmd);
			consoleScreen.setTitle(getString("title console"));
			consoleScreen.clear();
			//consoleScreen.setFontFace(Font.FACE_MONOSPACE);

			loginScriptScreen = new TextBox(getString("title login script"), null, 4096, TextField.ANY);
			loginScriptScreen.setCommandListener(this);
			loginScriptScreen.addCommand(cancelCmd);
			loginScriptScreen.addCommand(okCmd);
//			loginScriptScreen.addCommand(insertNewlineCmd);
			loginScriptScreen.setInitialInputMode("MIDP_LOWERCASE_LATIN");

			logoutScriptScreen = new TextBox(getString("title logout script"), null, 4096, TextField.ANY);
			logoutScriptScreen.setCommandListener(this);
			logoutScriptScreen.addCommand(cancelCmd);
			logoutScriptScreen.addCommand(okCmd);
			logoutScriptScreen.setInitialInputMode("MIDP_LOWERCASE_LATIN");

			editAliasScreen = new Form(getString("title edit alias"));
			editAliasScreen.setCommandListener(this);
			editAliasScreen.addCommand(cancelCmd);
			editAliasScreen.addCommand(okCmd);
			editAliasScreen.append(aliasNameField);
			aliasNameField.setLayout(layout);
			aliasNameField.setInitialInputMode("MIDP_LOWERCASE_LATIN");
			editAliasScreen.append(aliasCommandsField);
			aliasCommandsField.setLayout(layout);
//			aliasCommandsField.addCommand(insertNewlineCmd);
			aliasCommandsField.setInitialInputMode("MIDP_LOWERCASE_LATIN");
			
			settingsScreen = new Form(getString("title settings"));
			settingsScreen.setCommandListener(this);
			settingsScreen.addCommand(cancelCmd);
			settingsScreen.addCommand(okCmd);
//			settingsScreen.addCommand(showScreensScreenCmd);
			settingsScreen.append(promptItem);
			promptItem.setLayout(layout);
			promptItem.setInitialInputMode("MIDP_LOWERCASE_LATIN");
			settingsScreen.append(boardSizeItem);
			boardSizeItem.setLayout(layout);
			settingsScreen.append(boardFontSizeItem);
			boardFontSizeItem.setLayout(layout);
			settingsScreen.append(consoleFontSizeItem);
			consoleFontSizeItem.setLayout(layout);
			if (boardScreen.hasPointerEvents()) {
				settingsScreen.append(pointerMoveStyleItem);
			}
			pointerMoveStyleItem.setLayout(layout);
			pointerMoveStyleItem.append("Drag & Drop", null);
			pointerMoveStyleItem.append("Click & Click", null);
			settingsScreen.append(randomOptionsItem);
			randomOptionsItem.append("Show additional game info", null);
			randomOptionsItem.append("Use timeseal", null);
			randomOptionsItem.append("Board fullscreen mode", null);
			settingsScreen.append(backgroundsItem);
			fillBackgrounds();
			
			soughtScreen = new SoughtList(getDisplay());
			soughtScreen.setCommandListener(this);
			soughtScreen.addCommand(switchToCmd);
			soughtScreen.addCommand(playCmd);
			soughtScreen.addCommand(cleanCmd);
			soughtScreen.setSelectCommand(playCmd);
			soughtScreen.setTitle(getString("title sought"));
			
			seekScreen = new Form(getString("title seek"));
			seekScreen.setCommandListener(this);
			//seekScreen.addCommand(backCmd);
			seekScreen.addCommand(okCmd);
			seekScreen.addCommand(switchToCmd);
			seekScreen.append(seekTimeField);
			seekTimeField.setLayout(layout);
			seekScreen.append(seekIncrementField);
			seekIncrementField.setLayout(layout);
			seekScreen.append(seekRatedField);
			seekRatedField.setLayout(layout);
			seekRatedField.append(getString("label seek rated"), null);
			seekScreen.append(seekTypeField);
			seekTypeField.setLayout(layout);
			seekTypeField.append("chess", null);
			seekTypeField.append("crazyhouse", null);
			seekTypeField.append("suicide", null);
			seekTypeField.append("atomic", null);
			seekTypeField.append("losers", null);
			seekTypeField.append("wild fr", null);
			seekTypeField.append("wild 0", null);
			seekTypeField.append("wild 1", null);
			seekTypeField.append("wild 2", null);
			seekTypeField.append("wild 3", null);
			seekTypeField.append("wild 4", null);
			seekTypeField.append("wild 5", null);
			seekTypeField.append("wild 8", null);
			seekTypeField.append("wild 8a", null);
			seekScreen.append(seekMinRatingField);
			seekMinRatingField.setLayout(layout);
			seekScreen.append(seekMaxRatingField);
			seekMaxRatingField.setLayout(layout);
			
			matchScreen = new Form(getString("title match"));
			matchScreen.setCommandListener(this);
			//matchScreen.addCommand(backCmd);
			matchScreen.addCommand(okCmd);
			matchScreen.addCommand(switchToCmd);
			matchScreen.append(matchHandleField);
			matchHandleField.setLayout(layout);
			matchHandleField.setInitialInputMode("MIDP_LOWERCASE_LATIN");
			matchScreen.append(matchTimeField);
			matchTimeField.setLayout(layout);
			matchScreen.append(matchIncrementField);
			matchIncrementField.setLayout(layout);
			matchScreen.append(matchRatedField);
			matchRatedField.setLayout(layout);
			matchRatedField.append(getString("label match rated"), null);
			matchScreen.append(matchTypeField);
			matchTypeField.setLayout(layout);
			matchTypeField.append("chess", null);
			matchTypeField.append("crazyhouse", null);
			matchTypeField.append("suicide", null);
			matchTypeField.append("atomic", null);
			matchTypeField.append("losers", null);
			matchTypeField.append("wild fr", null);
			matchTypeField.append("wild 0", null);
			matchTypeField.append("wild 1", null);
			matchTypeField.append("wild 2", null);
			matchTypeField.append("wild 3", null);
			matchTypeField.append("wild 4", null);
			matchTypeField.append("wild 5", null);
			matchTypeField.append("wild 8", null);
			matchTypeField.append("wild 8a", null);
			
			chatScreen = new ChatScreen(getDisplay());
			chatScreen.setCommandListener(this);
			//chatScreen.addCommand(backCmd);
			chatScreen.addCommand(chatCmd);
			chatScreen.addCommand(chatPrivatelyCmd);
			chatScreen.addCommand(matchCmd);
			chatScreen.addCommand(clearCmd);
			chatScreen.addCommand(switchToCmd);
			chatScreen.setSelectCommand(chatPrivatelyCmd);
			chatScreen.setTitle(getString("title chat"));
			
			chatWriteScreen = new TextBox(null, null, 1024, TextField.ANY);
			chatWriteScreen.setCommandListener(this);
			chatWriteScreen.addCommand(cancelCmd);
			chatWriteScreen.addCommand(sendCmd);
			chatWriteScreen.setInitialInputMode("MIDP_LOWERCASE_LATIN");
			
			screensScreen = new List(getString("title screens"), List.IMPLICIT);
			screensScreen.setCommandListener(this);
			screensScreen.setSelectCommand(selectCmd);
			screens = new Displayable[] {
				boardScreen,
				chatScreen,
				soughtScreen,
				seekScreen,
				matchScreen,
				commandLineScreen,
				consoleScreen,
				aliasesScreen,
				loginScriptScreen,
				logoutScriptScreen,
				settingsScreen,
				statiscticsScreen,
				loginScreen,
			};
			for (int i = 0; i < screens.length; i++) {
				screensScreen.append(screens[i].getTitle(), null);
			}
			screensScreen.append("- - -", null);
			screensScreen.append(getString("cmd disconnect"), null);
			screensScreen.append(getString("cmd exit"), null);
			updateFromSettings();
		} catch (Throwable ex) {
			handleError(ex, "initialize");
		}
//		TimesealStream.addListener(new Listener() {
//			public void data(byte[] data) {
//				StringBuffer buffer = new StringBuffer();
//				StringBuffer buffer2 = new StringBuffer();
//				for (int i = 0; i < data.length; i++) {
//					if (i > 0) {
//						buffer.append('-');
//					}
//					int x = data[i] & 0xff;
//					buffer2.append((char) x);
//					if (x < 0x10) {
//						buffer.append('0');
//					}
//					buffer.append(Integer.toHexString(x));
//				}
//				statiscticsScreen.append(buffer.toString());
//				statiscticsScreen.append(buffer2.toString());
//			}
//		});
	}

	protected void startMidlet() {
		switchToLoginScreen();
	}

	protected void pauseMidlet() {
	}

	protected void resumeMidlet() {
	}

	protected void destroyMidlet(boolean unconditional) {
		try {
			closeConnection();
		} catch (Throwable ex) {
			handleError(ex, "destroy midlet");
		}
	}
	
	private void closeConnection() throws RecordStoreException, IOException {
		if (client != null) {
			client.setListener(null);
			sendMultiline(settings.getLogoutScript());
			send("quit");
			client = null;
		}
	}

	public void switchDisplayable(Displayable next) {
		Displayable d = getDisplay().getCurrent();
		if (d == soughtScreen) {
			send("iset seekinfo 0");
		}
		super.switchDisplayable(next);
	}

	public void commandAction(Command cmd, Displayable d) {
		try {
			if (cmd == AbstractCanvas.GAME_A_COMMAND) {
				switchToBoardScreen();
			}
			if (cmd == AbstractCanvas.GAME_B_COMMAND) {
				switchToChatScreen();
			}
			if (cmd == AbstractCanvas.GAME_C_COMMAND) {
				switchToCommandLineScreen();
			}
			if (cmd == AbstractCanvas.GAME_D_COMMAND) {
				switchToConsoleScreen();
			}
//			if (cmd == exitCmd) {
//				switchDisplayable(confirmExitScreen);
//			}
//			if (cmd == backCmd) {
//				if (d == soughtScreen) {
//					send("iset seekinfo 0");
//				}
//				switchToRootScreen();
//			}
//			if (cmd == showConsoleCmd) {
//				switchToCommandLineScreen();
//			}
//			if (cmd == showStatisticsCmd) {
//				switchToStatisticsScreen();
//			}
//			if (cmd == showAliasesCmd) {
//				switchToAliasesScreen();
//			}
//			if (cmd == showLoginScriptCmd) {
//				switchToLoginScriptScreen();
//			}
			if (cmd == switchToCmd) {
				switchToScreensScreen();
			}
//			if (d == statiscticsScreen) {
//				if (cmd == refreshCmd) {
//					refreshStats();
//				}
//			}
			if (d == loginScreen) {
				if (cmd == connectCmd) {
					connectToFics();
				}
			}
			if (d == boardScreen) {
//				if (cmd == castleShortCmd) {
//					send("oo", false);
//				}
//				if (cmd == castleLongCmd) {
//					send("ooo", false);
//				}
				if (cmd == flipCmd) {
					boardScreen.flip();
				}
				if (cmd == drawCmd) {
					send("draw");
				}
				if (cmd == resignCmd) {
					send("resign");
				}
				if (cmd == forwardCmd) {
					send("forw", false);
				}
				if (cmd == backwardCmd) {
					send("back", false);
				}
				if (cmd == unexamineCmd) {
					send("unex");
				}
				if (cmd == unobserveCmd) {
					send("unob");
				}
//				if (cmd == incBoardSizeCmd) {
//					if (boardScreen.increaseSize()) {
//						settings.saveBoardSize(boardScreen.getSize());
//					}
//				}
//				if (cmd == decBoardSizeCmd) {
//					if (boardScreen.decreaseSize()) {
//						settings.saveBoardSize(boardScreen.getSize());
//					}
//				}
//				if (cmd == incFontSizeCmd) {
//					if (boardScreen.increaseFontSize()) {
//						settings.saveBoardFontSize(boardScreen.getFontSize());
//					}
//				}
//				if (cmd == decFontSizeCmd) {
//					if (boardScreen.decreaseFontSize()) {
//						settings.saveBoardFontSize(boardScreen.getFontSize());
//					}
//				}
			}
			if (d == commandLineScreen) {
				if (cmd == sendCmd) {
					sendMultiline(commandLineScreen.getString());
					switchToConsoleScreen();
					commandLineScreen.setString(null);
				}
				if (cmd == cancelCmd) {
					switchToScreensScreen();
				}
			}
			if (d == confirmExitScreen) {
				if (cmd == cancelCmd) {
					switchToScreensScreen();
				}
				if (cmd == okCmd) {
					exitMidlet();
				}
			}
			if (d == aliasesScreen) {
				if (cmd == addCmd) {
					Alias alias = new Alias();
					editAlias(alias);
				}
				if (cmd == editCmd) {
					int index = aliasesScreen.getSelectedIndex();
					if (index != -1) {
						Alias alias = (Alias) aliasesScreen.get(index);
						editAlias(alias);
					}
				}
				if (cmd == removeCmd) {
					int index = aliasesScreen.getSelectedIndex();
					if (index != -1) {
						Alias alias = (Alias) aliasesScreen.get(index);
						settings.deleteAlias(alias);
						aliasesScreen.remove(index);
					}
				}
			}
			if (d == consoleScreen) {
				if (cmd == incFontSizeCmd) {
					if (consoleScreen.increaseFontSize()) {
						settings.saveConsoleFontSize(consoleScreen.getFontSize());
					}
				}
				if (cmd == decFontSizeCmd) {
					if (consoleScreen.decreaseFontSize()) {
						settings.saveConsoleFontSize(consoleScreen.getFontSize());
					}
				}
				if (cmd == clearCmd) {
					consoleScreen.clear();
					consoleScreen.append(settings.getPrompt());
				}
			}
			if (d == loginScriptScreen) {
				if (cmd == okCmd) {
					String loginScript = loginScriptScreen.getString();
					settings.saveLoginScript(loginScript);
				}
				switchToScreensScreen();
			}
			if (d == logoutScriptScreen) {
				if (cmd == okCmd) {
					String logoutScript = logoutScriptScreen.getString();
					settings.saveLogoutScript(logoutScript);
				}
				switchToScreensScreen();
			}
			if (d == editAliasScreen) {
				if (cmd == okCmd) {
					editedAlias.setName(aliasNameField.getString());
					editedAlias.setCommands(aliasCommandsField.getString());
					settings.saveAlias(editedAlias);
				}
				//if (cmd == okCmd || cmd == cancelCmd) {
				editedAlias = null;
				switchToAliasesScreen();
				//}
			}
			if (d == settingsScreen) {
//				if (cmd == cancelCmd) {
//					switchToRootScreen();
//				}
				if (cmd == okCmd) {
					updateSettings();
//					switchToRootScreen();
				}
				switchToScreensScreen();
			}
			if (d == soughtScreen) {
				if (cmd == playCmd) {
					SeekInfo seekInfo = (SeekInfo) soughtScreen.get(soughtScreen.getSelectedIndex());
					if (seekInfo != null) {
						send("play " + seekInfo.getSeekNumber());
						switchToConsoleScreen();
					}
				}
				if (cmd == cleanCmd) {
					soughtScreen.clean();
				}
			}
			if (d == seekScreen) {
				if (cmd == okCmd) {
					send("seek " + seekTimeField.getString()
							+ " " + seekIncrementField.getString()
							+ " " + (seekRatedField.isSelected(0) ? "r" : "u")
							+ " " + seekTypeField.getString(seekTypeField.getSelectedIndex())
							+ " " + seekMinRatingField.getString()
							+ "-" + seekMaxRatingField.getString());
					switchToConsoleScreen();
				}
			}
			if (d == matchScreen) {
				if (cmd == okCmd) {
					send("match " + matchHandleField.getString()
							+ " " + matchTimeField.getString()
							+ " " + matchIncrementField.getString()
							+ " " + (matchRatedField.isSelected(0) ? "r" : "u")
							+ " " + matchTypeField.getString(matchTypeField.getSelectedIndex()));
					switchToConsoleScreen();
				}
			}
			if (d == chatScreen) {
				if (cmd == chatCmd) {
					if (chatScreen.getCurrentChatText() != null) {
						chatWriteScreen.setTitle(chatScreen.getCurrentChatText());
						chatPrivately = false;
						switchDisplayable(chatWriteScreen);
					}
				}
				if (cmd == chatPrivatelyCmd) {
					if (chatScreen.getSelectedIndex() != -1) {
						chatWriteScreen.setTitle(chatScreen.getSelectedHandle());
						chatPrivately = true;
						switchDisplayable(chatWriteScreen);
					}
				}
				if (cmd == matchCmd) {
					if (chatScreen.getSelectedIndex() != -1) {
						//System.out.println("before match, " + chatScreen.getSelectedHandle());
						String handle = chatScreen.getSelectedHandle();
						if (matchHandleField.getMaxSize() < handle.length()) {
							handle = handle.substring(0, matchHandleField.getMaxSize());
						}
						matchHandleField.setString(handle);
						//System.out.println("after match");
						switchToMatchScreen();
					}
				}
				if (cmd == clearCmd) {
					chatScreen.removeAll();
				}
//				if (cmd == addCmd) {
//					Random r = new Random();
//					chatScreen.addDiscussionStatement(new DiscussionStatement("" + r.nextInt() % 5, "" + r.nextInt(), r.nextLong() + " " + r.nextLong(), DiscussionStatement.CHANNEL_TELL));
//				}
			}
			if (d == chatWriteScreen) {
				if (cmd == sendCmd) {
					if (chatWriteScreen.getString().length() > 0) {
						String id = chatPrivately ? chatScreen.getSelectedHandle() : chatScreen.getCurrentChatId();
						String myHandle = client != null ? client.getUsername() : "[ not sent ]";
						String message = encoder.encode(chatWriteScreen.getString());
						int type = chatPrivately ? DiscussionStatement.TELL : chatScreen.getCurrentChatType();
						send("tell " + id + " " + message);
						DiscussionStatement ds = new DiscussionStatement(id, myHandle, message, type);
						chatScreen.addDiscussionStatement(ds);
						switchToChatScreen();
						chatWriteScreen.setString(null);
					}
				}
				if (cmd == cancelCmd) {
					switchToChatScreen();
					//chatWriteScreen.setString(null);
				}
			}
			if (d == screensScreen) {
				if (cmd == selectCmd) {
					int index = screensScreen.getSelectedIndex();
					Displayable screen = null;
					if (index < screens.length) {
						screen = screens[index];
					}
					if (screen == boardScreen) {
						switchToBoardScreen();
					}
					if (screen == chatScreen) {
						switchToChatScreen();
					}
					if (screen == soughtScreen) {
						switchToSoughtScreen();
					}
					if (screen == seekScreen) {
						switchToSeekScreen();
					}
					if (screen == matchScreen) {
						switchToMatchScreen();
					}
					if (screen == commandLineScreen) {
						switchToCommandLineScreen();
					}
					if (screen == consoleScreen) {
						switchToConsoleScreen();
					}
					if (screen == aliasesScreen) {
						switchToAliasesScreen();
					}
					if (screen == loginScriptScreen) {
						switchToLoginScriptScreen();
					}
					if (screen == logoutScriptScreen) {
						switchToLogoutScriptScreen();
					}
					if (screen == settingsScreen) {
						switchToSettingsScreen();
					}
					if (screen == statiscticsScreen) {
						switchToStatisticsScreen();
					}
					if (screen == loginScreen) {
						switchToLoginScreen();
					}
					if (screen == null) {
						String str = screensScreen.getString(index);
						if (getString("cmd disconnect").equals(str)) {
							closeConnection();
							loginScreen.setErrorMessage("Disconnected");
							infoScreen.setTitle(getString("title disconnected"));
							infoScreen.setString(getString("text disconnected"));
							switchToInfoScreen();
						}
						if (getString("cmd exit").equals(str)) {
							switchDisplayable(confirmExitScreen);
						}
					}
				}
			}
			if (d == infoScreen) {
				if (cmd == okCmd) {
					switchToLoginScreen();
				}
			}
		} catch (Throwable ex) {
			handleError(ex, "commandAction cmd d");
		}
	}

	private HtmlEntityEncoder encoder = HtmlEntityEncoder.instance();

//	public void commandAction(Command cmd, Item item) {
//		try {
//			if (item == outputField) {
//				if (cmd == clearCmd) {
//					outputField.setText(StringUtils.EMPTY);
//					consoleScreen.setTitle(getString("console"));
//				}
//				if (cmd == fullscreenCmd) {
//					switchDisplayable(outputScreen);
//				}
//			}
//			if (item == aliasCommandsField) {
//				if (cmd == insertNewlineCmd) {
//					aliasCommandsField.setString(aliasCommandsField.getString() + "\n");
//				}
//			}
//		} catch (Throwable ex) {
//			handleError(ex, "commandAction cmd item");
//		}
//	}

	public void move(int xFrom, int yFrom, int xTo, int yTo) {
		StringBuffer buffer = new StringBuffer();
		buffer.append((char) ('a' + xFrom));
		buffer.append((char) ('8' - yFrom));
		buffer.append((char) ('a' + xTo));
		buffer.append((char) ('8' - yTo));
		debugMessage("sending move " + buffer + " " + DateUtils.formatDate(System.currentTimeMillis()));
		send(buffer.toString(), false);
	}

	private void send(String command) {
		send(command, true);
	}

	private void send(String command, boolean showInConsole) {
		if (client != null && client.connected()) {
			client.write(command + "\n");
			//client.write("$$set busy just issued command \"" + command + "\"\n");
			if (showInConsole) {
				consoleScreen.append("[CMD SENT] " + command + "\n");
			}
		} else {
			consoleScreen.append("[NOT SENT] " + command + "\n");
		}
	}
	
	private void sendMultiline(String commands) throws RecordStoreException, IOException {
		Alias[] aliases = settings.getAliases();
		String[] tmp = StringUtils.split(commands, '\n');
		for (int i = 0; i < tmp.length; i++) {
			String command = tmp[i];
			if (!StringUtils.isNullOrEmpty(command)) {
				boolean aliasSent = false;
				for (int j = 0; j < aliases.length; j++) {
					Alias alias = aliases[j];
					if (alias.getName().equals(commands)) {
						String[] aliasTmp = StringUtils.split(alias.getCommands(), '\n');
						for (int k = 0; k < aliasTmp.length; k++) {
							String aliasCommand = aliasTmp[k];
							if (!StringUtils.isNullOrEmpty(aliasCommand)) {
								send(encoder.encode(aliasCommand));
							}
						}
						aliasSent = true;
					}
				}
				if (!aliasSent) {
					send(encoder.encode(command));
				}
			}
		}
	}

	public static Object getProperty(String key) {
		Object value = resources.getProperty(key);
		if (value == null) {
			value = "[" + key + "] is missing";
		}
		return value;
	}

	public static String getString(String key) {
		String value = resources.getString(key);
		if (value == null) {
			value = "[" + key + "] is missing";
			System.out.println(value);
		}
		return value;
	}

	private void switchToLoginScreen() {
//		rootScreen = loginScreen;
		switchDisplayable(loginScreen);
	}
//
//	private void switchToRootScreen() {
//		switchDisplayable(rootScreen);
//	}
	
	private void switchToBoardScreen() {
//		rootScreen = boardScreen;
//		boardScreen.setFullScreenMode(!boardScreen.hasPointerEvents());
		switchDisplayable(boardScreen);
	}
	
	private void switchToChatScreen() {
		switchDisplayable(chatScreen);
	}
	
	private void switchToSoughtScreen() {
		soughtScreen.removeAll();
		send("iset seekinfo 1");
		switchDisplayable(soughtScreen);
	}
	
	private void switchToSeekScreen() {
		switchDisplayable(seekScreen);
	}
	
	private void switchToMatchScreen() {
		switchDisplayable(matchScreen);
	}

	private void switchToCommandLineScreen() {
		switchDisplayable(commandLineScreen);
	}

	private void switchToStatisticsScreen() {
		refreshStats();
		switchDisplayable(statiscticsScreen);
	}

	private void switchToAliasesScreen() throws RecordStoreException, IOException {
		aliasesScreen.removeAll();
		Alias[] aliases = settings.getAliases();
		for (int i = 0; i < aliases.length; i++) {
			aliasesScreen.append(aliases[i]);
		}
		switchDisplayable(aliasesScreen);
	}

	private void switchToLoginScriptScreen() throws RecordStoreException, IOException {
		loginScriptScreen.setString(settings.getLoginScript());
		switchDisplayable(loginScriptScreen);
	}

	private void switchToLogoutScriptScreen() throws RecordStoreException, IOException {
		logoutScriptScreen.setString(settings.getLogoutScript());
		switchDisplayable(logoutScriptScreen);
	}
	
	private void switchToConsoleScreen() {
		switchDisplayable(consoleScreen);
	}
	
	private void switchToSettingsScreen() throws RecordStoreException, IOException {
		promptItem.setString(settings.getPrompt());
		int index = BoardScreen.getIndex(settings.getBoardSize()) + 1;
		boardSizeItem.setValue(index);
		int boardFontSize = settings.getBoardFontSize();
		if (boardFontSize == Font.SIZE_SMALL) {
			index = 0;
		} else if (boardFontSize == Font.SIZE_MEDIUM) {
			index = 1;
		} else {
			index = 2;
		}
		boardFontSizeItem.setValue(index);
		int consoleFontSize = settings.getConsoleFontSize();
		if (consoleFontSize == Font.SIZE_SMALL) {
			index = 0;
		} else if (consoleFontSize == Font.SIZE_MEDIUM) {
			index = 1;
		} else {
			index = 2;
		}
		consoleFontSizeItem.setValue(index);
		pointerMoveStyleItem.setSelectedIndex(settings.getUseDragAndDrop() ? 0 : 1, true);
		randomOptionsItem.setSelectedIndex(0, settings.getShowAdditionalGameInfo());
		randomOptionsItem.setSelectedIndex(1, settings.getUseTimeseal());
		randomOptionsItem.setSelectedIndex(2, settings.getBoardFullscreenMode());
		if (backgroundsItem.size() == 0) {
			fillBackgrounds();
		}
		switchDisplayable(settingsScreen);
	}

	private void fillBackgrounds() throws RecordStoreException, IOException {
		BoardBackground[] backgrounds = settings.getBackgrounds();
		backgroundsItem.deleteAll();
		int width = getDisplay().getBestImageWidth(Display.CHOICE_GROUP_ELEMENT);
		if (width == 0) {
			width = 16;
		}
		int height = getDisplay().getBestImageHeight(Display.CHOICE_GROUP_ELEMENT);
		if (height == 0) {
			height = 16;
		}
		Image colors = Image.createImage(width, height);
		Graphics g = colors.getGraphics();
		for (int i = 0; i < backgrounds.length; i++) {
			BoardBackground bg = backgrounds[i];
			String name = bg.getName();
			int lightColor = bg.getLightColor();
			g.setColor(lightColor);
			GraphicsUtils.fillRect(g, 0, 0, width / 3, height / 3);
			GraphicsUtils.fillRect(g, width / 3, height / 3, width - width / 3, height - height / 3);
			int darkColor = bg.getDarkColor();
			g.setColor(darkColor);
			GraphicsUtils.fillRect(g, width / 3, 0, width - width / 3, height / 3);
			GraphicsUtils.fillRect(g, 0, height / 3, width / 3, height - height / 3);
			backgroundsItem.append(name, Image.createImage(colors));
		}
		for (int i = 0; i < backgrounds.length; i++) {
			BoardBackground bg = backgrounds[i];
			if (bg.getRsIndex() == settings.getBoardBackgroundId()) {
				backgroundsItem.setSelectedIndex(i, true);
				break;
			}
		}
	}
	
	private void updateSettings() throws RecordStoreException, IOException {
		settings.savePrompt(promptItem.getString());
		int size = BoardScreen.getSize(boardSizeItem.getValue() - 1);
		settings.saveBoardSize(size);
		int[] sizes = { Font.SIZE_SMALL, Font.SIZE_MEDIUM, Font.SIZE_LARGE };
		int boardFontIndex = boardFontSizeItem.getValue();
		settings.saveBoardFontSize(sizes[boardFontIndex]);
		int consoleFontIndex = consoleFontSizeItem.getValue();
		settings.saveConsoleFontSize(sizes[consoleFontIndex]);
		settings.saveUseDragAndDrop(pointerMoveStyleItem.getSelectedIndex() == 0);
		settings.saveShowAdditionalGameInfo(randomOptionsItem.isSelected(0));
		settings.saveUseTimeseal(randomOptionsItem.isSelected(1));
		settings.saveBoardFullscreenMode(randomOptionsItem.isSelected(2));
		BoardBackground[] backgrounds = settings.getBackgrounds();
		BoardBackground background  = backgrounds[backgroundsItem.getSelectedIndex()];
		settings.saveBoardBackgroundId(background.getRsIndex());
		updateFromSettings();
	}
	
	private void updateFromSettings() throws RecordStoreException, IOException {
		boardScreen.setSize(settings.getBoardSize());
		boardScreen.setFontSize(settings.getBoardFontSize());
		boardScreen.setUseDragAndDrop(settings.getUseDragAndDrop());
		consoleScreen.setFontSize(settings.getConsoleFontSize());
		boardScreen.setShowAdditionalInfo(settings.getShowAdditionalGameInfo());
		BoardBackground[] backgrounds = settings.getBackgrounds();
		BoardBackground background = null;
		int rsIndex = settings.getBoardBackgroundId();
		for (int i = 0; i < backgrounds.length; i++) {
			if (backgrounds[i].getRsIndex() == rsIndex) {
				background = backgrounds[i];
				break;
			}
		}
		boardScreen.setLightSquareColor(background.getLightColor());
		boardScreen.setDarkSquareColor(background.getDarkColor());
		boardScreen.setFullScreenMode(settings.getBoardFullscreenMode());
	}

	private void switchToScreensScreen() {
		switchDisplayable(screensScreen);
	}
	
	private void switchToInfoScreen() {
//		rootScreen = loginScreen;
		switchDisplayable(infoScreen);
	}

	private Alias editedAlias;

	private void editAlias(Alias alias) {
		editedAlias = alias;
		aliasNameField.setString(alias.getName());
		aliasCommandsField.setString(alias.getCommands());
		switchDisplayable(editAliasScreen);
	}

	private void refreshStats() {
		long recvBytes = client != null ? client.getRecvBytes() : 0;
		recvBytesField.setText(Long.toString(recvBytes) + getString("text bytes"));
		long sentBytes = client != null ? client.getSentBytes() : 0;
		sentBytesField.setText(Long.toString(sentBytes) + getString("text bytes"));
		Runtime.getRuntime().gc();
		long freeMemory = Runtime.getRuntime().freeMemory();
		freeMemoryField.setText(Long.toString(freeMemory) + getString("text bytes"));
		long usedMemory = Runtime.getRuntime().totalMemory() - freeMemory;
		usedMemoryField.setText(Long.toString(usedMemory) + getString("text bytes"));
		try {
			String[] list = RecordStore.listRecordStores();
			int usedStorage = 0;
			if (list == null) {
				freeStorageField.setText("UNKNOWN");
			} else {
				int freeStorage = 0;
				for (int i = 0; i < list.length; i++) {
					RecordStore rs = RecordStore.openRecordStore(list[i], false);
					usedStorage += rs.getSize();
					if (i == 0) {
						freeStorage = rs.getSizeAvailable();
					}
					rs.closeRecordStore();
				}
				freeStorageField.setText(Integer.toString(freeStorage) + getString("text bytes"));
			}
			usedStorageField.setText(Integer.toString(usedStorage) + getString("text bytes"));
		} catch (RecordStoreException ex) {
			handleError(ex, "refreshStats");
		}
	}

	private void connectToFics() throws RecordStoreException, IOException {
		String username = loginScreen.getUsername();
		String password = loginScreen.getPassword();
		boolean rememberPassword = loginScreen.getRememberPassword();
		loginScreen.setErrorMessage("Connecting...");
		settings.saveUsername(username);
		settings.savePassword(rememberPassword ? password : StringUtils.EMPTY);
		settings.saveRememberPassword(rememberPassword);
		if (StringUtils.isNullOrEmpty(username)) {
			username = "g";
		}
		if (StringUtils.isNullOrEmpty(password)) {
			password = "yyyyyyyy";
		}
		closeConnection();
		client = new FicsClient();
		client.setListener(this);
		client.connect(username, password, settings.getUseTimeseal());
	}
	
	private void handleError(Throwable ex, String id) {
		ex.printStackTrace();
		infoScreen.setTitle(getString("title error"));
		String message = ex.getMessage() == null
				? "" : "[message: " + ex.getMessage() + "]\n";
		infoScreen.setString("Fatal error id = \"" + id + "\":\n" + message + ex);
		switchToInfoScreen();
		if (ex instanceof Error) {
			consoleScreen.clear();
//			System.out.println("handle error");
			chatScreen.removeAll();
//			try {
//				closeConnection();
//			} catch (Exception ignored) {
//				ignored.printStackTrace();
//			}
		}
	}

	public void connected() {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				try {
					//switchToBoardScreen();
					loginScreen.setErrorMessage("Connected");
//					String[] tmp = StringUtils.split(settings.getLoginScript(), '\n');
//					for (int i = 0; i < tmp.length; i++) {
//						String command = tmp[i];
//						if (!StringUtils.isNullOrEmpty(command)) {
//							send(encoder.encode(command));
//						}
//					}
//					updateChat = true;
					//send("=ch");
					sendMultiline(settings.getLoginScript());
				} catch (Throwable ex) {
					handleError(ex, "connected");
				}
			}
		});
//		try {
//			
//		} catch (final Throwable ex) {
//			getDisplay().callSerially(new Runnable() {
//				public void run() {
//					handleError(ex, "settings.getLoginScript()");
//				}
//			});
//		}
	}

	public void disconnected() {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				if (client.getErrorMessage() != null) {
					loginScreen.setErrorMessage(client.getErrorMessage());
					switchToLoginScreen();
				} else {
					loginScreen.setErrorMessage("Disconnected");
					infoScreen.setTitle(getString("title disconnected"));
					infoScreen.setString(getString("text disconnected"));
					switchToInfoScreen();
				}
			}
		});
	}

	public void disconnectedWithError(final Throwable ex) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				loginScreen.setErrorMessage("Disconnected / ERROR");
				infoScreen.setTitle(getString("title error"));
				infoScreen.setString(getString("text disconnected with error") + ex.toString());
				switchToInfoScreen();
			}
		});
	}

	public void errorSendingData(final Throwable ex, final String data) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				infoScreen.setTitle(getString("title error"));
				infoScreen.setString(getString("text error sending data") + data + "\n" + ex.toString());
				switchToInfoScreen();
			}
		});
	}

	public void positionReceived(final PositionWrapper position) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				if (boardScreen.getPositionWrapper() != null
						&& boardScreen.getPositionWrapper().gameNumber != position.gameNumber) {
					boardScreen.setComment(null);
				}
				boardScreen.setPositionWrapper(position);
				//consoleScreen.append("relation: " + position.relation + "\n");
				boardScreen.removeCommand(drawCmd);
				boardScreen.removeCommand(resignCmd);
				boardScreen.removeCommand(forwardCmd);
				boardScreen.removeCommand(backwardCmd);
				boardScreen.removeCommand(unexamineCmd);
				boardScreen.removeCommand(unobserveCmd);
				int rel = position.relation;
				if (rel == 1 || rel == -1) {
					boardScreen.addCommand(drawCmd);
					boardScreen.addCommand(resignCmd);
				} else if (rel == 2) {
					boardScreen.addCommand(forwardCmd);
					boardScreen.addCommand(backwardCmd);
					boardScreen.addCommand(unexamineCmd);
				} else if (rel == 0 || rel == -2) {
					boardScreen.addCommand(unobserveCmd);
				}
				debugMessage("position " + position.movePretty + " lag: " + position.lag + " " + DateUtils.formatDate(System.currentTimeMillis()));
			}
		});
	}

	public void gameInfoReceived(final GameInfo gameInfo) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				PositionWrapper wrapper = boardScreen.getPositionWrapper();
				if (wrapper.gameNumber == gameInfo.gameNumber) {

				}
			}
		});
	}

	public void gameCommentReceived(final int gameNumber, final String gameComment) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				PositionWrapper wrapper = boardScreen.getPositionWrapper();
				if (wrapper.gameNumber == gameNumber) {
					boardScreen.setComment(gameComment);
				}
			}
		});
	}

	public void playingGameStarted() {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				switchToBoardScreen();
			}
		});
	}

	public void gameEnded(final int gameNumber, final String result, final String description) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				PositionWrapper wrapper = boardScreen.getPositionWrapper();
				if (wrapper.gameNumber == gameNumber) {
					wrapper.clockRunning = false;
					wrapper.result = result;
					wrapper.description = description;
				}
			}
		});
	}
	
	public void seeksRemoved(final int[] seekNumbers) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				soughtScreen.removeSeeks(seekNumbers);
			}
		});
	}

	public void seek(final SeekInfo seekInfo) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				soughtScreen.addSeek(seekInfo);
			}
		});
	}

	public void discussionStatementReceived(final DiscussionStatement ds) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				chatScreen.addDiscussionStatement(ds);
			}
		});
	}

	public void blockReceived(final String block) {
//		if (block.startsWith("\n             _       __     __                             __")) {
//			return;
//		}
		getDisplay().callSerially(new Runnable() {
			public void run() {
				try {
					if ("[H[2J\n".equals(block)) {
						//outputScreen.setText(null);
						//outputField.setText(StringUtils.EMPTY);
						//consoleScreen.setTitle(getString("title console"));
						consoleScreen.clear();
						consoleScreen.append(settings.getPrompt());
						return;
					}
					consoleScreen.append(encoder.decode(block) + settings.getPrompt());
//					String output = settings.getPrompt() + encoder.decode(block) + outputField.getText();
//					int maxSize = 300;
//					output = StringUtils.truncate(output, maxSize);
//					outputField.setText(output);
//					String title = getString("title console") + "/" + output.length() + getString("text bytes");
//					consoleScreen.setTitle(title);
				} catch (Throwable ex) {
					handleError(ex, "blockReceived");
				}
			}
		});
	}

	public void debugMessage(final String message) {
		getDisplay().callSerially(new Runnable() {
			public void run() {
				if (consoleScreen.debug) {
					consoleScreen.append("[DEBUG] " + message + "\n");
				}
			}
		});
	}
}
