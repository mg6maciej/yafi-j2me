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
package pl.mg6maciej;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * @author mg6maciej
 */
public class GraphicsUtils {

	public static void fillRect(Graphics g, int x, int y, int w, int h) {
		g.fillRect(x, y, w, h);
	}

	public static void drawRect(Graphics g, int x, int y, int w, int h) {
		g.drawRect(x, y, w - 1, h - 1);
	}

	public static void drawRect(Graphics g, int x, int y, int w, int h, int line) {
		for (int i = 0; i < line; i++) {
			GraphicsUtils.drawRect(g, x + i, y + i, w - 2 * i, h - 2 * i);
		}
	}

	public static void drawString(Graphics g, String str, int x, int y, int anchor) {
		if (!StringUtils.isNullOrEmpty(str)) {
			g.drawString(str, x, y, anchor);
		}
	}

	public static void drawString(Graphics g, String str, int x, int y, int anchor, int width) {
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipHeight();
		g.setClip(x, y, width, g.getFont().getHeight());
		GraphicsUtils.drawString(g, str, x, y, anchor);
		g.setClip(clipX, clipY, clipW, clipH);
	}

	public static void drawMultilineString(Graphics g, String str, int x, int y, int anchor, int height) {
		String[] lines = StringUtils.split(str, '\n');
		drawStringArray(g, lines, x, y, anchor, height);
	}

	public static void drawStringArray(Graphics g, String[] array, int x, int y, int anchor, int height) {
		if (array == null) {
			return;
		}
		int fontHeight = g.getFont().getHeight();
		for (int i = 0; i < array.length; i++) {
			if (g.getTranslateY() + y + fontHeight <= 0) {
				y += fontHeight;
				continue;
			} else if (g.getTranslateY() + y - fontHeight >= height) {
				break;
			}
			drawString(g, array[i], x, y, anchor);
			y += fontHeight;
		}
	}

	public static boolean isWithin(int pointx, int pointy, int x, int y, int w, int h) {
		return pointx >= x && pointx < x + w && pointy >= y && pointy < y + h;
	}

	public static String[] calculateLines(String str, Font font, int width) {
		String[] lines = StringUtils.split(str, '\n');
		Vector v = new Vector();
		for (int i = 0; i < lines.length; i++) {
			str = lines[i];
			int start = 0;
			int length = font.stringWidth(str);
			while (length > width) {
				int count = find(str, start, font, width);
				v.addElement(str.substring(start, start + count));
				start += count;
				length = font.substringWidth(str, start, str.length() - start);
			}
			v.addElement(str.substring(start));
		}
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}
//	
//	public static void drawWrappedString(Graphics g, String str, int x, int y, int anchor, int width) {
//		Font font = g.getFont();
//		int start = 0;
//		int length = font.stringWidth(str);
//		while (length > width) {
//			int count = find(str, start, font, width);
//			g.drawSubstring(str, start, count, x, y, anchor);
//			y += font.getHeight();
//			start += count;
//			length = font.substringWidth(str, start, str.length() - start);
//		}
//		g.drawSubstring(str, start, str.length() - start, x, y, anchor);
//	}
	
	private static int find(String str, int start, Font font, int width) {
		int end = str.length();
		int length = font.substringWidth(str, start, end - start);
		while (end - start > 1 && length > width) {
			end--;
			length -= font.charWidth(str.charAt(end));
		}
		if (end != str.length()) {
			int lastSpace = str.lastIndexOf(' ', end);
			if (lastSpace > start) {
				end = lastSpace;
				while (end < str.length() && str.charAt(end) == ' ') {
					end++;
				}
			}
		}
		return end - start;
	}
}
