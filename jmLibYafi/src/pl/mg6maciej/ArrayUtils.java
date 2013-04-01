
package pl.mg6maciej;

/**
 * @author mg6maciej
 */
public class ArrayUtils {

	public static boolean contains(int[] array, int obj) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == obj) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(String[] array, String obj) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(obj)) {
				return true;
			}
		}
		return false;
	}

	public static byte[] intToByteArray(int i) {
		return new byte[] {
			(byte) (i >>> 24),
			(byte) (i >>> 16),
			(byte) (i >>> 8),
			(byte) i
		};
	}

	public static int byteArrayToInt(byte[] array) {
		return byteArrayToInt(array, 0);
	}

	public static int byteArrayToInt(byte[] array, int pos) {
		return (array[pos] << 24)
				| ((array[pos + 1] & 0xff) << 16)
				| ((array[pos + 2] & 0xff) << 8)
				| (array[pos + 3] & 0xff);
	}
}
