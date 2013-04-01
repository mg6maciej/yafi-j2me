
package pl.mg6maciej.yafi;

import java.io.IOException;

/**
 * @author mg6maciej
 */
interface Stream {

	int read() throws IOException;

	void write(byte[] data) throws IOException;

	void close() throws IOException;

	boolean connected();

	long getBytesReceived();

	long getBytesSent();
}
