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

import java.util.Calendar;
import java.util.Date;

/**
 * @author mg6maciej
 */
public class DateUtils {

	public static String formatTime(long time) {
		StringBuffer buffer = new StringBuffer();
		if (time < 0) {
			time = -time;
			buffer.append('-');
		}
		long millisecond = time % 1000;
		time /= 1000;
		long second = time % 60;
		time /= 60;
		long minute = time % 60;
		time /= 60;
		long hour = time;
		if (hour > 0) {
			buffer.append(hour);
			buffer.append(':');
			if (minute < 10) {
				buffer.append('0');
			}
		}
		buffer.append(minute);
		buffer.append(':');
		if (second < 10) {
			buffer.append('0');
		}
		buffer.append(second);
		if (hour == 0 && minute == 0 && second < 20) {
			buffer.append('.');
			if (millisecond < 100) {
				buffer.append('0');
			}
			if (millisecond < 10) {
				buffer.append('0');
			}
			millisecond /= 10;
			if (millisecond > 0) {
				buffer.append(millisecond);
			}
		}
		return buffer.toString();
	}

	public static String formatDate(long time) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millisecond = cal.get(Calendar.MILLISECOND);
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
		buffer.append('.');
		if (millisecond < 100) {
			buffer.append('0');
			if (millisecond < 10) {
				buffer.append('0');
			}
		}
		buffer.append(millisecond);
		return buffer.toString();
	}
}
