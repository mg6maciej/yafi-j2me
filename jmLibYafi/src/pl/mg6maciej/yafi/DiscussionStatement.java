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

import java.util.Date;

/**
 * @author mg6maciej
 */
public class DiscussionStatement {
	
	String id;
	String handle;
	String message;
	Date date;
	int type;
	
	public static final int ANNOUNCEMENT = 0;
	public static final int TELL = 8;
	public static final int PARTNER_TELL = 9;
	public static final int CHANNEL_TELL = 16;
	public static final int SHOUT = 24;
	public static final int IT_SHOUT = 25;
	public static final int CHESS_SHOUT = 26;
	public static final int SAY = 32;
	public static final int KIBITZ = 33;
	public static final int WHISPER = 34;

	public DiscussionStatement(String id, String handle, String message, int type) {
		this.id = id;
		this.handle = handle;
		this.message = message;
		this.type = type;
		this.date = new Date();
	}

	public String getId() {
		return id;
	}

	public String getHandle() {
		return handle;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}

	public int getType() {
		return type;
	}
}
