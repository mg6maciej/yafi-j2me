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

/**
 * @author mg6maciej
 */
public class FicsUtils {

	public static boolean isValidHandle(String handle) {
		if (handle == null) {
			throw new NullPointerException("handle");
		}
		if (handle.length() < 3 || handle.length() > 17) {
			return false;
		}
		handle = handle.toLowerCase();
		for (int i = 0; i < handle.length(); i++) {
			char c = handle.charAt(i);
			if (c < 'a' || c > 'z') {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isValidTitles(String titles) {
		if (titles == null) {
			throw new NullPointerException("titles");
		}
		boolean valid = true;
		for (int i = 0; i < titles.length(); i++) {
			char c = titles.charAt(i);
			if (valid) {
				valid = false;
				if (c != '(') {
					break;
				}
			} else {
				if (c == ')') {
					valid = true;
				} else if (c != '*' && (c < 'A' || c > 'Z')) {
					break;
				}
			}
		}
		return valid;
	}
	
	public static boolean isValidChannel(String channel) {
		if (channel == null) {
			throw new NullPointerException("channel");
		}
		try {
			int ch = Integer.parseInt(channel);
			return ch >= 0 && ch < 256;
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
