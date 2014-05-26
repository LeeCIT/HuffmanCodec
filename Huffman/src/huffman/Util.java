


package huffman;



/**
 * Misc utility functions.
 * @author Lee Coakley
 */
public final class Util
{
	private Util() {}
	
	
	
	protected static void cyclicRedundancyCheck( byte[] data, byte[] decoded ) {
		System.out.println();
		
		for (int i=0; i<data.length; i++)
			if (data[i] - decoded[i] != 0)
				System.out.println( "CRC fail at byte #" + i );
		
		if (data.length != decoded.length)
			System.out.println( "CRC fail: Size mismatch." );
		
		System.out.println( "CRC OK" );
	}
	
	
	
	protected static byte[] arrayConcat( byte[] a, byte[] b ) {
		int    lenA = a.length;
		int    lenB = b.length;
		byte[] out  = new byte[ lenA + lenB ];
		
		System.arraycopy( a, 0, out, 0,    lenA );
		System.arraycopy( b, 0, out, lenA, lenB );
		
		return out;
	}
}
