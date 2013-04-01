
package pl.mg6maciej.yafi;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import pl.mg6maciej.GraphicsUtils;
import pl.mg6maciej.ListCanvas;
import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class AliasesList extends ListCanvas {

	public AliasesList(Display display) {
		super(display);
	}
	
	private Font fontName = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);
	private Font fontCommands = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	
	protected void paintElement(Graphics g, int y, Object object, boolean selected) {
		Alias alias = (Alias) object;
		g.setColor(0x000000);
		g.setFont(fontName);
		GraphicsUtils.drawString(g, alias.getName(), 10, y, 0);
		g.setFont(fontCommands);
		GraphicsUtils.drawMultilineString(g, alias.getCommands(), 5, y + fontName.getHeight(), 0, getHeight());
	}
	
	protected int getElementHeight(Object object) {
		Alias alias = (Alias) object;
		int height = fontName.getHeight();
		int commandsCount = StringUtils.count(alias.getCommands(), '\n') + 1;
		height += fontCommands.getHeight() * commandsCount;
		return height;
	}
}
