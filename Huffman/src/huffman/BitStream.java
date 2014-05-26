


package huffman;
import java.util.Arrays;



/**
 * Deals with data at the level of individual bits.
 * @author Lee Coakley
 */
public class BitStream
{
	private ByteVector bytes;
	private byte       buffer;
	private int        bits;
	
	
	
	public BitStream() {
		bytes = new ByteVector();
	}
	
	
	
	public BitStream( byte[] b ) {
		bytes = new ByteVector( b );
	}
	
	
	
	/**
	 * Add up to 32 bits to the stream.
	 * Bits are added in order from MSB to LSB: the order remains the same in the stream.
	 * LSB is bit 0, bits-1 is MSB.
	 * @param bits Specifies what portion of the int to use.  8 is byte, 16 is short, etc. 
	 */
	public void add( int x, int bits ) {
		int xBits  = 32;
		int offset = (xBits - 1) - (xBits - bits);
		
		for (int i=offset; i>=0; i--)
			add( (x>>i) & 1 );
	}
	
	
	
	/**
	 * Add a string of bits.
	 * If the string contains any characters other than 0 and 1, the result is undefined.
	 */
	public void add( String bits ) {
		for (int i=0; i<bits.length(); i++)
			add( bits.charAt(i) - '0' );
	}
	
	
	
	/**
	 * Add a single bit.
	 */
	public void add( int bit ) {		
		if (bits >= 8) {
			bytes.add( buffer );
			bits   = 0;
			buffer = 0;
		}
		
		buffer <<= 1;
		buffer  |= bit;
		bits++;
	}
	
	
	
	/**
	 * Read up to 32 bits out of the stream, starting at the given offset in bits.
	 * The resulting bits are stored in the rightmost portion of the return value, in the same order.
	 * Example: get(0,16) populates the low 16 bits of the return value. 
	 */
	public int get( int msbBitOffset, int count ) {
		int v    = 0;
		int from = count - 1;
		
		for (int i=0; i<count; i++)
			v |= get(msbBitOffset + i) << (from - i);
		
		return v;
	}
	
	
	
	/**
	 * Read a single bit.
	 */
	public int get( int index ) {
		int  arrayIndex = (index / 8);
		int  bitIndex   = (index % 8);
		byte b;
		
		if (arrayIndex < bytes.size())
			 b = bytes.get( arrayIndex ); 
		else b = buffer;
		
		int basePos = 8 - 1;
		return (b >> (basePos-bitIndex)) & 1;
	}
	
	
	
	/**
	 * Get the total number of bits in the stream.
	 */
	public int getBitCount() {
		return bits + (bytes.size() * 8);
	}
	
	
	
	/**
	 * Get the number of bytes in the stream.
	 * Rounds up.  9 bits means 2 bytes.
	 */
	public int getByteCount() {
		int byteCount = bytes.size();
		
		if (bits > 0)
			 return byteCount + 1;
		else return byteCount;
	}
	
	
	
	/**
	 * Convert the bitstream to an array of bytes.
	 * Any undefined bits in the final byte will be zero.
	 */
	public byte[] toArray() {
		byte[] array = Arrays.copyOf( bytes.toArray(), getByteCount() );
		
		if (bits > 0) {
			byte last = (byte) (buffer << 8-bits);
			array[ array.length - 1 ] = last;
		}
		
		return array;
	}
}





























