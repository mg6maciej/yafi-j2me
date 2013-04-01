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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class BoardBackground {

	private String name;
	private int lightColor;
	private int darkColor;
	private int rsIndex;

	public BoardBackground() {
		this(StringUtils.EMPTY, 0xffffff, 0x000000);
	}

	public BoardBackground(String name, int lightColor, int darkColor) {
		this(name, lightColor, darkColor, -1);
	}

	public BoardBackground(String name, int lightColor, int darkColor, int rsIndex) {
		setName(name);
		setLightColor(lightColor);
		setDarkColor(darkColor);
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

	public int getLightColor() {
		return lightColor;
	}

	public void setLightColor(int lightColor) {
		this.lightColor = lightColor;
	}

	public int getDarkColor() {
		return darkColor;
	}

	public void setDarkColor(int darkColor) {
		this.darkColor = darkColor;
	}

	public int getRsIndex() {
		return rsIndex;
	}

	public void setRsIndex(int rsIndex) {
		this.rsIndex = rsIndex;
	}

	public String toString() {
		return super.toString();
	}
	
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);
		dos.writeUTF(getName());
		dos.writeInt(getLightColor());
		dos.writeInt(getDarkColor());
		byte[] array = buffer.toByteArray();
		buffer.close();
		return array;
	}
	
	public static BoardBackground fromByteArray(byte[] array, int rsIndex) throws IOException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(buffer);
		String name = dis.readUTF();
		int lightColor = dis.readInt();
		int darkColor = dis.readInt();
		buffer.close();
		return new BoardBackground(name, lightColor, darkColor, rsIndex);
	}
}
