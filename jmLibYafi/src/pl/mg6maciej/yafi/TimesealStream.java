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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import pl.mg6maciej.DateUtils;

/**
 * @author mg6maciej
 */
public class TimesealStream implements Stream {

	private PlainStream stream;
	private long startTime;

	public TimesealStream(String host, int port) throws IOException {
		stream = new PlainStream(host, port);
		startTime = System.currentTimeMillis();
		String hostname = System.getProperty("microedition.hostname");
		String locale = System.getProperty("microedition.locale");
		String platform = System.getProperty("microedition.platform");
		String profiles = System.getProperty("microedition.profiles");
		String configuration = System.getProperty("microedition.configuration");
		StringBuffer account = new StringBuffer("Yafi 0.2.5");
		if (hostname != null) {
			account.append(" hostname:");
			account.append(hostname);
		}
		if (locale != null) {
			account.append(" locale:");
			account.append(locale);
		}
		StringBuffer system = new StringBuffer();
		if (platform != null) {
			system.append(platform);
		}
		if (profiles != null) {
			if (system.length() > 0) {
				system.append(' ');
			}
			system.append(profiles);
		}
		if (configuration != null) {
			if (system.length() > 0) {
				system.append(' ');
			}
			system.append(configuration);
		}
		if (system.length() == 0) {
			system.append("j2me");
		}
		write(("TIMESTAMP|" + account + "|" + system +"|\n").getBytes());
	}

	private static final byte[] stamp = "\n[G]\n".getBytes();
	private static final byte[] stampReply = "\n\u00029\n".getBytes();
	private static final byte[] about ="Timestamp (FICS) v1.0 - programmed by Henrik Gram.".getBytes();

	private int readBuffer;
	private int stampIndex;
	private int stampLength;
	private ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream();
	private byte[] encodeBuffer = new byte[240];

	public int read() throws IOException {
		if (stampIndex == 0) {
			if (stampLength == 0) {
				readBuffer = stream.read();
				if (readBuffer != stamp[0]) {
					return readBuffer;
				}
				stampLength = 1;
			}
			while (stampLength < stamp.length) {
				readBuffer = stream.read();
				if (readBuffer != stamp[stampLength]) {
					break;
				}
				stampLength++;
			}
			if (stampLength == stamp.length) {
				write(stampReply);
				if (listener != null) {
					listener.debug("stamp reply " + DateUtils.formatDate(System.currentTimeMillis()));
				}
				stampLength = 0;
				return read();
			} else {
				return stamp[stampIndex++];
			}
		} else if (stampIndex == stampLength) {
			stampIndex = 0;
			if (readBuffer != stamp[0]) {
				stampLength = 0;
				return readBuffer;
			}
			stampLength = 1;
			return read();
		} else {
			return stamp[stampIndex++];
		}
	}

	private TsListener listener;

	public void setListener(TsListener listener) {
		this.listener = listener;
	}

	public synchronized void write(byte[] data) throws IOException {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == '\n') {
				if (writeBuffer.size() > 0) {
					encodeAndWrite(writeBuffer.toByteArray());
					writeBuffer.reset();
				}
			} else {
				writeBuffer.write(data[i]);
			}
		}
	}

	private void encodeAndWrite(byte[] data) throws IOException {
		long currentTime = System.currentTimeMillis();
		long diff = currentTime - startTime;
		byte[] timestamp = Long.toString(diff).getBytes();
		int length = data.length + 1 + timestamp.length;
		length += 12 - length % 12;
		if (length > encodeBuffer.length) {
			encodeBuffer = new byte[length];
		}
		int i = 0;
		System.arraycopy(data, 0, encodeBuffer, i, data.length);
		i += data.length;
		encodeBuffer[i++] = 24;
		System.arraycopy(timestamp, 0, encodeBuffer, i, timestamp.length);
		i += timestamp.length;
		encodeBuffer[i++] = 25;
		while (i < length) {
			encodeBuffer[i++] = 49;
		}
		for (i = 0; i < length; i += 12) {
			for (int j = 0; j < 6; j += 2) {
				byte tmp = encodeBuffer[i + j];
				encodeBuffer[i + j] = encodeBuffer[i + 11 - j];
				encodeBuffer[i + 11 - j] = tmp;
			}
		}
		for (i = 0; i < length; i++) {
			encodeBuffer[i] |= 128;
			encodeBuffer[i] ^= about[(i + 6) % about.length];
			encodeBuffer[i] -= 32;
		}
		stream.write(encodeBuffer, 0, length);
		stream.write(134);
		stream.write(10);
		stream.flush();
	}

	public void close() throws IOException {
		stream.close();
	}

	public boolean connected() {
		return stream.connected();
	}

	public long getBytesReceived() {
		return stream.getBytesReceived();
	}

	public long getBytesSent() {
		return stream.getBytesSent();
	}
}
