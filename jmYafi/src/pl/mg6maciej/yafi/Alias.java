
package pl.mg6maciej.yafi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class Alias {

	private String name;
	private String commands;
	private int rsIndex;

	public Alias() {
		this(StringUtils.EMPTY, StringUtils.EMPTY);
	}

	public Alias(String name, String commands) {
		this(name, commands, -1);
	}

	public Alias(String name, String commands, int rsIndex) {
		setName(name);
		setCommands(commands);
		setRsIndex(rsIndex);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.name = name;
	}

	public String getCommands() {
		return commands;
	}

	public void setCommands(String commands) {
		if (commands == null) {
			throw new NullPointerException("commands");
		}
		this.commands = commands;
	}

	public int getRsIndex() {
		return rsIndex;
	}

	public void setRsIndex(int rsIndex) {
		this.rsIndex = rsIndex;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append(": ");
		buffer.append(StringUtils.replace(commands, "\n", " | "));
		return buffer.toString();
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);
		dos.writeUTF(getName());
		dos.writeUTF(getCommands());
		byte[] array = buffer.toByteArray();
		buffer.close();
		return array;
	}

	public static Alias fromByteArray(byte[] array, int rsIndex) throws IOException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(buffer);
		String name = dis.readUTF();
		String commands = dis.readUTF();
		buffer.close();
		return new Alias(name, commands, rsIndex);
	}
}
