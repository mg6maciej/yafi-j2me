
package pl.mg6maciej;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mg6maciej
 */
public class Utils {

	public static String byteArrayToString(byte[] array) throws IOException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(buffer);
		String setting = dis.readUTF();
		buffer.close();
		return setting;
	}

	public static int byteArrayToInt(byte[] array) throws IOException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(buffer);
		int i = dis.readInt();
		buffer.close();
		return i;
	}

	public static boolean byteArrayToBoolean(byte[] array) throws IOException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(buffer);
		boolean b = dis.readBoolean();
		buffer.close();
		return b;
	}

	public static byte[] toByteArray(String str) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);
		dos.writeUTF(str);
		byte[] array = buffer.toByteArray();
		buffer.close();
		return array;
	}

	public static byte[] toByteArray(int i) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);
		dos.writeInt(i);
		byte[] array = buffer.toByteArray();
		buffer.close();
		return array;
	}

	public static byte[] toByteArray(boolean b) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);
		dos.writeBoolean(b);
		byte[] array = buffer.toByteArray();
		buffer.close();
		return array;
	}
	
	public static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	public static String formatDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		StringBuffer buffer = new StringBuffer();
		if (hour < 10) {
			buffer.append('0');
		}
		buffer.append(hour);
		buffer.append(':');
		if (minute < 10) {
			buffer.append('0');
		}
		buffer.append(minute);
		buffer.append(':');
		if (second < 10) {
			buffer.append('0');
		}
		buffer.append(second);
		return buffer.toString();
	}
}
