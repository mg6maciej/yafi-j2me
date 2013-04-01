
package pl.mg6maciej;

import java.util.Vector;

/**
 * @author mg6maciej
 */
public class StringUtils {

	public static final String EMPTY = "";

	public static String[] split(String str, char c) {
		Vector v = new Vector();
		int start = 0;
		int end = str.indexOf(c);
		while (end != -1) {
			v.addElement(str.substring(start, end));
			start = end + 1;
			end = str.indexOf(c, start);
		}
		v.addElement(str.substring(start));
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}

	public static String[] split(String str, String substr) {
		Vector v = new Vector();
		int start = 0;
		int end = str.indexOf(substr);
		while (end != -1) {
			v.addElement(str.substring(start, end));
			start = end + substr.length();
			end = str.indexOf(substr, start);
		}
		v.addElement(str.substring(start));
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}
	
	public static String join(String[] array, char c) {
		StringBuffer buffer = new StringBuffer(array[0]);
		for (int i = 1; i < array.length; i++) {
			buffer.append(c);
			buffer.append(array[i]);
		}
		return buffer.toString();
	}
	
	public static String join(String[] array, String substr) {
		StringBuffer buffer = new StringBuffer(array[0]);
		for (int i = 1; i < array.length; i++) {
			buffer.append(substr);
			buffer.append(array[i]);
		}
		return buffer.toString();
	}

	public static String replace(String str, String substr, String replacement) {
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		int end = str.indexOf(substr);
		while (end != -1) {
			buffer.append(str.substring(start, end));
			buffer.append(replacement);
			start = end + substr.length();
			end = str.indexOf(substr, start);
		}
		buffer.append(str.substring(start));
		return buffer.toString();
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNullOrEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static int count(String str, char c) {
		int qty = 0;
		int index = str.indexOf(c);
		while (index != -1) {
			qty++;
			index = str.indexOf(c, index + 1);
		}
		return qty;
	}

	public static int count(String str, String substr) {
		int qty = 0;
		int index = str.indexOf(substr);
		while (index != -1) {
			qty++;
			index = str.indexOf(substr, index + substr.length());
		}
		return qty;
	}

	public static String truncate(String str, int maxSize) {
		if (str.length() > maxSize) {
			int index = str.lastIndexOf('\n', maxSize);
			if (index == -1) {
				index = maxSize;
			}
			str = str.substring(0, index);
		}
		return str;
	}
}
