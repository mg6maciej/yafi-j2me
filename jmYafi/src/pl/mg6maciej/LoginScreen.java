
package pl.mg6maciej;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 * @author mg6maciej
 */
public class LoginScreen extends AbstractCanvas {

	private String username;
	private String password;
	private boolean rememberPassword;
	private String errorMessage;
	
	private int selected = OBJECT_USERNAME;
	private static final int OBJECT_USERNAME = 0;
	private static final int OBJECT_PASSWORD = 1;
	private static final int OBJECT_REMEBER_PASSWORD = 2;
	
	private static final String labelUsername = "Username";
	private static final String labelPassword = "Password";
	private static final String labelRememberPassword = "Remember password";
	
	private Font font;
	
	public LoginScreen(Display display) {
		super(display);
	}

	public String getUsername() {
		return username == null ? StringUtils.EMPTY : username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password == null ? StringUtils.EMPTY : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getRememberPassword() {
		return rememberPassword;
	}

	public void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		repaint();
	}
	
	private static final Command cancelCmd = new Command("Cancel", Command.CANCEL, 0);
	private static final Command okCmd = new Command("OK", Command.OK, 0);
	
	private TextBox editor;

	public void commandAction(Command cmd, Displayable d) {
		if (d == editor) {
			if (cmd == okCmd) {
				if (selected == OBJECT_USERNAME) {
					setUsername(editor.getString());
				} else if (selected == OBJECT_PASSWORD) {
					setPassword(editor.getString());
				}
			}
			getDisplay().setCurrent(this);
			editor = null;
		} else {
			super.commandAction(cmd, d);
		}
	}
	
	private void edit() {
		if (selected == OBJECT_USERNAME) {
			editor = new TextBox(labelUsername, getUsername(), 32, TextField.NON_PREDICTIVE);
		} else if (selected == OBJECT_PASSWORD) {
			editor = new TextBox(labelPassword, getPassword(), 32, TextField.NON_PREDICTIVE | TextField.PASSWORD);
		}
		editor.setInitialInputMode("MIDP_LOWERCASE_LATIN");
		editor.setCommandListener(this);
		editor.addCommand(cancelCmd);
		editor.addCommand(okCmd);
		getDisplay().setCurrent(editor);
	}

	protected void downKeyPressed() {
		if (selected == OBJECT_REMEBER_PASSWORD) {
			return;
		}
		if (selected == OBJECT_USERNAME) {
			selected = OBJECT_PASSWORD;
		} else if (selected == OBJECT_PASSWORD) {
			selected = OBJECT_REMEBER_PASSWORD;
		}
		repaint();
	}

	protected void upKeyPressed() {
		if (selected == OBJECT_USERNAME) {
			return;
		}
		if (selected == OBJECT_PASSWORD) {
			selected = OBJECT_USERNAME;
		} else if (selected == OBJECT_REMEBER_PASSWORD) {
			selected = OBJECT_PASSWORD;
		}
		repaint();
	}

	protected void fireKeyReleased() {
		makeAction();
	}
	
	private void makeAction() {
		if (selected == OBJECT_REMEBER_PASSWORD) {
			setRememberPassword(!getRememberPassword());
			repaint();
		} else {
			edit();
		}
	}

	protected boolean pressed(int x, int y) {
		int fieldPadding = 3;
		int textWidth = getFont().stringWidth("XXXXXXXXXXXXXXXX");
		int fieldWidth = textWidth + 2 * fieldPadding;
		int usernameWidth = getFont().stringWidth(labelUsername + ':');
		int passwordWidth = getFont().stringWidth(labelPassword + ':');
		int rememberPasswordWidth = getFont().stringWidth(labelRememberPassword);
		int labelWidth = usernameWidth > passwordWidth ? usernameWidth : passwordWidth;
		int labelFieldMargin = 6;
		int labelLabelMargin = 10;
		int totalWidth = labelWidth + labelFieldMargin + fieldWidth;
		int displayMargin = 3;
		int maxWidth = getWidth() - 2 * displayMargin;
		if (totalWidth > maxWidth) {
			fieldWidth -= totalWidth - maxWidth;
			totalWidth = maxWidth;
		}
		int _x = (getWidth() - totalWidth) / 2 + labelWidth + labelFieldMargin;
		int _y = getHeight() / 5;
		int height = getFont().getHeight();
		int fieldHeight = height + 2 * fieldPadding;
		int checkboxWidth = height + labelFieldMargin + rememberPasswordWidth;
		int checkboxX = (getWidth() - checkboxWidth) / 2;
		if (checkboxX < displayMargin) {
			checkboxX = displayMargin;
		}
		int checkboxY = _y + 2 * (height + labelLabelMargin);
		if (GraphicsUtils.isWithin(x, y, _x, _y - fieldPadding, fieldWidth, fieldHeight)) {
			if (selected != OBJECT_USERNAME) {
				repaint();
				selected = OBJECT_USERNAME;
			}
			return false;
		}
		if (GraphicsUtils.isWithin(x, y, _x, _y - fieldPadding + height + labelLabelMargin, fieldWidth, fieldHeight)) {
			if (selected != OBJECT_PASSWORD) {
				repaint();
				selected = OBJECT_PASSWORD;
			}
			return false;
		}
		if (GraphicsUtils.isWithin(x, y, checkboxX, checkboxY, checkboxWidth, height)) {
			if (selected != OBJECT_REMEBER_PASSWORD) {
				repaint();
				selected = OBJECT_REMEBER_PASSWORD;
			}
			return false;
		}
		return true;
	}

	protected void clicked(int x, int y) {
		makeAction();
	}
	
	protected void paint(Graphics g) {
		super.paint(g);
		g.setFont(getFont());
		int fieldPadding = 3;
		int textWidth = getFont().stringWidth("XXXXXXXXXXXXXXXX");
		int fieldWidth = textWidth + 2 * fieldPadding;
		int usernameWidth = getFont().stringWidth(labelUsername + ':');
		int passwordWidth = getFont().stringWidth(labelPassword + ':');
		int rememberPasswordWidth = getFont().stringWidth(labelRememberPassword);
		int labelWidth = usernameWidth > passwordWidth ? usernameWidth : passwordWidth;
		int labelFieldMargin = 6;
		int labelLabelMargin = 10;
		int totalWidth = labelWidth + labelFieldMargin + fieldWidth;
		int displayMargin = 3;
		int maxWidth = getWidth() - 2 * displayMargin;
		if (totalWidth > maxWidth) {
			textWidth -= totalWidth - maxWidth;
			fieldWidth -= totalWidth - maxWidth;
			totalWidth = maxWidth;
		}
		int _x = (getWidth() - totalWidth) / 2 + labelWidth + labelFieldMargin;
		int _y = getHeight() / 5;
		int height = getFont().getHeight();
		int fieldHeight = height + 2 * fieldPadding;
		int checkboxWidth = height + labelFieldMargin + rememberPasswordWidth;
		int checkboxX = (getWidth() - checkboxWidth) / 2;
		if (checkboxX < displayMargin) {
			checkboxX = displayMargin;
		}
		int checkboxY = _y + 2 * (height + labelLabelMargin);
		int errorMessageY = _y + 3 * (height + labelLabelMargin);
		g.setColor(0xffffff);
		GraphicsUtils.fillRect(g, _x, _y - fieldPadding, fieldWidth, fieldHeight);
		GraphicsUtils.fillRect(g, _x, _y - fieldPadding + height + labelLabelMargin, fieldWidth, fieldHeight);
		GraphicsUtils.fillRect(g, checkboxX, checkboxY, height, height);
		if (selected == OBJECT_USERNAME) {
			g.setColor(0x0000ff);
		} else {
			g.setColor(0x000000);
		}
		GraphicsUtils.drawRect(g, _x, _y - fieldPadding, fieldWidth, fieldHeight);
		if (selected == OBJECT_PASSWORD) {
			g.setColor(0x0000ff);
		} else {
			g.setColor(0x000000);
		}
		GraphicsUtils.drawRect(g, _x, _y - fieldPadding + height + labelLabelMargin, fieldWidth, fieldHeight);
		if (selected == OBJECT_REMEBER_PASSWORD) {
			g.setColor(0x0000ff);
		} else {
			g.setColor(0x000000);
		}
		GraphicsUtils.drawRect(g, checkboxX, checkboxY, height, height);
		g.setColor(0x000000);
		g.drawString(labelUsername + ':', _x - labelFieldMargin, _y, Graphics.RIGHT | Graphics.TOP);
		g.drawString(labelPassword + ':', _x - labelFieldMargin, _y + height + labelLabelMargin, Graphics.RIGHT | Graphics.TOP);
		g.drawString(labelRememberPassword, checkboxX + height + labelFieldMargin, checkboxY, Graphics.LEFT | Graphics.TOP);
		if (getErrorMessage() != null) {
			String[] lines = GraphicsUtils.calculateLines(getErrorMessage(), getFont(), maxWidth);
			for (int i = 0; i < lines.length; i++) {
				g.drawString(lines[i], getWidth() / 2, errorMessageY, Graphics.HCENTER | Graphics.TOP);
				errorMessageY += height;
			}
		}
		if (getRememberPassword()) {
			GraphicsUtils.fillRect(g, checkboxX + 2, checkboxY + 2, height - 4, height - 4);
		}
		g.setClip(_x + fieldPadding, _y, textWidth, height);
		g.drawString(getUsername(), _x + fieldPadding, _y, Graphics.LEFT | Graphics.TOP);
		g.setClip(_x + fieldPadding, _y + height + labelLabelMargin, textWidth, height);
		String hidden = StringUtils.EMPTY;
		int length = getPassword().length();
		while (length > 0) {
			hidden += '*';
			length--;
		}
		g.drawString(hidden, _x + fieldPadding, _y + height + labelLabelMargin, Graphics.LEFT | Graphics.TOP);
	}

	protected void paintBackground(Graphics g) {
		g.setColor(0xcccccc);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public Font getFont() {
		if (font == null) {
			font = Font.getDefaultFont();
		}
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
