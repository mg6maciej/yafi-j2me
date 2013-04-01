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
