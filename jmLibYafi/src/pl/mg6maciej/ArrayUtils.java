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
