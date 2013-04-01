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
