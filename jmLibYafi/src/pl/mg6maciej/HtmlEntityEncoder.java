
package pl.mg6maciej;

/**
 * @author mg6maciej
 */
public class HtmlEntityEncoder {
	
	private static final HtmlEntityEncoder instance = new HtmlEntityEncoder();
	
	public static HtmlEntityEncoder instance() {
		return instance;
	}
	
	private HtmlEntityEncoder() {
	}
	
	private static final String entity = "&#x";

	public String encode(String text) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < ' ' || c > '~' || text.startsWith(entity, i)) {
				buffer.append(entity);
				buffer.append(Integer.toHexString(c));
				buffer.append(';');
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public String decode(String text) {
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		int end = text.indexOf(entity);
		while (end != -1) {
			int hexStart = end + entity.length();
			int hexEnd = text.indexOf(';', hexStart);
			if (hexEnd == -1) {
				break;
			}
			String hex = text.substring(hexStart, hexEnd);
			try {
				char c = (char) Integer.parseInt(hex, 16);
				buffer.append(text.substring(start, end));
				buffer.append(c);
				start = hexEnd + 1;
				end = text.indexOf(entity, start);
			} catch (NumberFormatException ex) {
				end = text.indexOf(entity, hexStart);
			}
		}
		buffer.append(text.substring(start));
		return buffer.toString();
	}
}
