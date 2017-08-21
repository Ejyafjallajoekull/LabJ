package labjframework.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class ByteUtilities {
// basic byte operations
	
	// create an int from a byte array
	public static final int toInt(byte[] bytes) throws IndexOutOfBoundsException{
		if (bytes.length >= Integer.BYTES) { // do not check this since an exception should be thrown when passing less than 4 bytes
			return ByteBuffer.wrap(bytes).getInt();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	// create a byte array from an int
	public static final byte[] integerToByte(int integer) {
		return (ByteBuffer.allocate(Integer.BYTES).putInt(integer)).array();
	}
	
	// return the byte representation length of a specified string
	public static final byte[] stringLengthToByte(String string, Charset encoding) throws NullPointerException{
		if (string != null) {
			return integerToByte(string.getBytes(encoding).length);
		} else {
			throw new NullPointerException();
		}
	}
	
	// makes the input stream read the next bytes as integer
	public static final int readInteger(InputStream inputStream) throws IOException, NullPointerException {
		if (inputStream != null) {
			byte[] intBytes = new byte[Integer.BYTES];
			inputStream.read(intBytes);
			return toInt(intBytes);
		} else {
			throw new NullPointerException();
		}
	}
	
	// makes the input stream read the next bytes as s string with the specified length
		public static final String readString(InputStream inputStream, int length, Charset encoding) throws IOException, NullPointerException {
			if (inputStream != null) {
				byte[] stringBytes = new byte[length];
				inputStream.read(stringBytes);
				return new String(stringBytes, encoding);	
			} else {
				throw new NullPointerException();
			}
		}
}
