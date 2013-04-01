
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
