
package pl.mg6maciej.yafi;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import pl.mg6maciej.GraphicsUtils;
import pl.mg6maciej.HtmlEntityEncoder;
import pl.mg6maciej.ListCanvas;
import pl.mg6maciej.Utils;

/**
 * @author mg6maciej
 */
public class ChatScreen extends ListCanvas {

	public ChatScreen(Display display) {
		super(display);
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	private Font fontHandle = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private Font fontDate = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private Font fontMessage = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private Font fontTitle = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

	public Font getFontHandle() {
		return fontHandle;
	}

	public Font getFontDate() {
		return fontDate;
	}

	public Font getFontMessage() {
		return fontMessage;
	}

	public Font getFontTitle() {
		return fontTitle;
	}
//
//	protected void paint(Graphics g) {
//		super.paint(g);
//		System.out.println("chat paint call");
//	}

	protected void paintElement(Graphics g, int y, Object object, boolean selected) {
		DsCache cache = (DsCache) object;
		String handle = cache.getDs().getHandle();
//		int type = wrapper.getDs().type;
//		if (type == DiscussionStatement.CHANNEL_TELL) {
//			handle += '(' + wrapper.getDs().getId() + ')';
//		} else if (type == DiscussionStatement.SHOUT) {
//			handle += " [shouts]";
//		} else if (type == DiscussionStatement.IT_SHOUT) {
//			handle = "--> " + handle;
//		} else if (type == DiscussionStatement.CHESS_SHOUT) {
//			handle += " [c-shouts]";
//		}
		String strDate = Utils.formatDate(cache.getDs().getDate());
		//int handleX = 3;//(getWidth() - 6 - getFontDate().stringWidth(strDate)) / 2;
		int messageY = y + Utils.max(getFontHandle().getHeight(), getFontDate().getHeight()) + 4;
		g.setColor(0x000000);
		g.setFont(getFontHandle());
		int strDateWidth = getFontDate().stringWidth(strDate);
		int handleWidth = getWidth() - strDateWidth - 6 - 2;
		GraphicsUtils.drawString(g, handle, 3, y + 2, Graphics.LEFT | Graphics.TOP, handleWidth);
		g.setFont(getFontDate());
		GraphicsUtils.drawString(g, strDate, getWidth() - 3, y + 2, Graphics.RIGHT | Graphics.TOP);
		g.setFont(getFontMessage());
		//GraphicsUtils.drawWrappedString(g, ds.getMessage(), 3, messageY, Graphics.LEFT | Graphics.TOP, getWidth() - 6);
		GraphicsUtils.drawStringArray(g, cache.getMessage(), 3, messageY, Graphics.LEFT | Graphics.TOP, getHeight());
	}

	protected int getElementHeight(Object object) {
		DsCache cache = (DsCache) object;
		int linesMessage = cache.getMessage().length;
		return 6 + linesMessage * getFontMessage().getHeight()
				+ Utils.max(getFontHandle().getHeight(), getFontDate().getHeight());
	}

	protected void paintTitle(Graphics g) {
		int titleHeight = getTitleHeight();
		g.setColor(0xeeeeee);
		g.fillRect(0, 0, getWidth(), titleHeight);
		if (currentChat != null) {
			g.setColor(0xffffff);
			g.fillRect(15, 3, getWidth() - 30, titleHeight - 4);
			g.setColor(0x000000);
			g.setFont(getFontTitle());
			int index = chatList.indexOf(currentChat);
			int size = chatList.size();
			if (index > 1) {
				g.drawLine(3, 3, 3, titleHeight - 2);
				g.drawLine(3, 3, 7, 3);
			}
			if (index > 0) {
				g.drawLine(9, 3, 9, titleHeight - 2);
				g.drawLine(9, 3, 13, 3);
			}
			g.drawLine(15, 3, getWidth() - 16, 3);
			g.drawLine(15, 3, 15, titleHeight - 2);
			g.drawLine(getWidth() - 16, 3, getWidth() - 16, titleHeight - 2);
			g.drawLine(0, titleHeight - 1, getWidth() - 1, titleHeight - 1);
			if (index < size - 1) {
				g.drawLine(getWidth() - 14, 3, getWidth() - 10, 3);
				g.drawLine(getWidth() - 10, 3, getWidth() - 10, titleHeight - 2);
			}
			if (index < size - 2) {
				g.drawLine(getWidth() - 8, 3, getWidth() - 4, 3);
				g.drawLine(getWidth() - 4, 3, getWidth() - 4, titleHeight - 2);
			}
			GraphicsUtils.drawString(g, getCurrentChatText(), getWidth() / 2, titleHeight - 3, Graphics.HCENTER | Graphics.BOTTOM);
		}
	}

	protected int getTitleHeight() {
		int height = getFontTitle().getHeight();
		return height + 9;
	}
	
	private Hashtable chats = new Hashtable();
	private Vector chatList = new Vector();
	private String currentChat;

	public void addDiscussionStatement(DiscussionStatement ds) {
		//if (!ds.getHandle().toLowerCase().equals("roboadmin")) {
		DsCache cache = new DsCache(ds);
		String id = ds.getId();
		if (currentChat == null) {
			currentChat = id;
		}
		if (currentChat.equals(id)) {
			append(cache);
			if (getSelectedIndex() == size() - 2) {
				selectNextObject();
			}
		}
		Vector chat = (Vector) chats.get(id);
		if (chat == null) {
			chat = new Vector();
			chats.put(id, chat);
			chatList.addElement(id);
		}
		chat.addElement(cache);
		repaint();
		//updateTitle();
		//}
	}
	
//	private void updateTitle() {
//		String title = getCurrentChatText();
//		int indexChat = chatList.indexOf(currentChat);
//		if (indexChat < chatList.size() - 1) {
//			title = "> " + title;
//		}
//		if (indexChat > 0) {
//			title = "< " + title;
//		}
//		setTitle(title);
//	}

	protected void leftKeyPressed() {
		moveToPreviousChat();
	}

	protected void leftKeyRepeated() {
		moveToPreviousChat();
	}

	protected void rightKeyPressed() {
		moveToNextChat();
	}

	protected void rightKeyRepeated() {
		moveToNextChat();
	}

	protected boolean pressed(int x, int y) {
		if (y < getTitleHeight()) {
			if (x < getWidth() / 2) {
				moveToPreviousChat();
			} else {
				moveToNextChat();
			}
			return true;
		} else {
			return super.pressed(x, y);
		}
	}

	public String getCurrentChatText() {
		if (currentChat == null) {
			return null;
		}
		if ("^".equals(currentChat)) {
			return "Chess shout";
		}
		if ("!".equals(currentChat)) {
			return "Shout";
		}
		try {
			Integer.parseInt(currentChat);
			return "Channel " + currentChat;
		} catch (NumberFormatException ignore) {
			//ignore.printStackTrace();
		}
		return currentChat;
	}

	public String getCurrentChatId() {
		return currentChat;
	}
	
	public int getCurrentChatType() {
		Vector chat = (Vector) chats.get(currentChat);
		if (chat.size() > 0) {
			DsCache cache = (DsCache) chat.elementAt(0);
			return cache.getDs().getType();
		} else {
			return DiscussionStatement.TELL;
		}
	}
	
	public String getSelectedHandle() {
		return ((DsCache) get(getSelectedIndex())).getDs().getHandle();
	}
	
	private void moveToPreviousChat() {
		int indexChat = chatList.indexOf(currentChat);
		if (indexChat > 0) {
			currentChat = (String) chatList.elementAt(indexChat - 1);
			updateCurrentChat();
		}
	}
	
	private void moveToNextChat() {
		int indexChat = chatList.indexOf(currentChat);
		if (indexChat < chatList.size() - 1) {
			currentChat = (String) chatList.elementAt(indexChat + 1);
			updateCurrentChat();
		}
	}
	
	private void updateCurrentChat() {
		//System.out.println("update current chat");
		super.removeAll();
		Vector chat = (Vector) chats.get(currentChat);
		for (int i = 0; i < chat.size(); i++) {
			append(chat.elementAt(i));
		}
		setSelectedIndex(chat.size() - 1);
		//updateTitle();
	}

	public void removeAll() {
		//System.out.println("why is this called?");
		setTitle("Chat");
		chats.clear();
		chatList.removeAllElements();
		currentChat = null;
		super.removeAll();
	}

	private class DsCache {
		
		private DiscussionStatement ds;
		private String[] message;

		public DsCache(DiscussionStatement ds) {
			this.ds = ds;
		}

		public DiscussionStatement getDs() {
			return ds;
		}

		public String[] getMessage() {
			if (message == null) {
				String msg = HtmlEntityEncoder.instance().decode(ds.getMessage());
				message = GraphicsUtils.calculateLines(msg, getFontMessage(), getWidth() - 6);
			}
			return message;
		}
	}
}
