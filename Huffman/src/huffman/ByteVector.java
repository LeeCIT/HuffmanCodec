


package huffman;
import java.util.Arrays;
import java.util.Iterator;



/**
 * Simple dynamic byte array.
 * @author Lee Coakley
 */
public class ByteVector implements Iterable<Byte>
{
	private byte[] core;
	private int    caret;
	
	
	
	public ByteVector() {
		core  = new byte[ 64 ];
		caret = -1;
	}
	
	
	
	public ByteVector( byte[] bytes ) {
		core  = Arrays.copyOf( bytes, bytes.length );
		caret = core.length - 1;
	}
	
	
	
	public void add( byte b ) {
		if (isCoreFull())
			grow();
		
		core[++caret] = b;
	}
	
	
	
	public byte get( int index ) {
		return core[ index ];
	}
	
	
	
	public int size() {
		return caret + 1;
	}
	
	
	
	public byte[] toArray() {
		return Arrays.copyOf( core, size() );
	}
	
	
	
	public void shrinkToFit() {
		core  = toArray();
		caret = core.length - 1;
	}
	
	
	
	private boolean isCoreFull() {
		return size() >= core.length;
	}
	
	
	
	private void grow() {
		core = Arrays.copyOf( core, core.length * 2 );
	}
	
	
	
	public Iterator<Byte> iterator() {
		return new Iterator<Byte>() {
			private int i = -1;
			
			public boolean hasNext() {
				return (i+1) < core.length;
			}
			
			public Byte next() {
				return core[++i];
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	
	
	
	
	public static void main( String[] args ) {
		ByteVector bv = new ByteVector();
		
		for (int i=0; i<72; i++)
			bv.add( (byte) i );
		
		for (int i=0; i<72; i++)
			System.out.println( bv.get(i) );
	}
}





































