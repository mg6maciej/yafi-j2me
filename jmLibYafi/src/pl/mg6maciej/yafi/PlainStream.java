
package pl.mg6maciej.yafi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 * @author mg6maciej
 */
class PlainStream implements Stream {

	private SocketConnection socket;
	private InputStream istream;
	private OutputStream ostream;

	private long bytesReceived;
	private long bytesSent;

	public PlainStream(String host, int port) throws IOException {
		String url = "socket://" + host + ":" + port;
		socket = (SocketConnection) Connector.open(url);
//		System.out.println("delay: " + socket.getSocketOption(SocketConnection.DELAY));
//		System.out.println("keepalive: " + socket.getSocketOption(SocketConnection.KEEPALIVE));
//		socket.setSocketOption(SocketConnection.DELAY, 0);
//		socket.setSocketOption(SocketConnection.KEEPALIVE, 0);
		istream = socket.openInputStream();
		ostream = socket.openOutputStream();
	}

	public int read() throws IOException {
		if (connected()) {
			int b = istream.read();
			bytesReceived++;
			while (b == '\r') {
				b = istream.read();
				bytesReceived++;
			}
			return b;
		}
		return -1;
	}

	void write(int b) throws IOException {
		if (connected()) {
			ostream.write(b);
			bytesSent++;
		}
	}

	public void write(byte[] data) throws IOException {
		if (connected()) {
			ostream.write(data);
			ostream.flush();
			bytesSent += data.length;
		}
	}

	void write(byte[] data, int start, int length) throws IOException {
		if (connected()) {
			ostream.write(data, start, length);
			bytesSent += length;
		}
	}
	
	void flush() throws IOException {
		ostream.flush();
	}

	public void close() throws IOException {
		if (connected()) {
			ostream.close();
			ostream = null;
			istream.close();
			istream = null;
			socket.close();
			socket = null;
		}
	}

	public boolean connected() {
		return socket != null;
	}

	public long getBytesReceived() {
		return bytesReceived;
	}

	public long getBytesSent() {
		return bytesSent;
	}
}
