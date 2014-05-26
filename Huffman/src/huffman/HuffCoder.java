


package huffman;



/**
 * Huffman codec.
 * @author Lee Coakley
 */
public class HuffCoder
{
	public static void main( String[] args ) throws Exception {
		byte[] data    = IO.read( "testdata.txt" );
		byte[] encoded = encode( data    );
		byte[] decoded = decode( encoded );
		
		IO.write( "huff.encoded.txt", encoded, false );
		IO.write( "huff.decoded.txt", decoded, false );
		
		Util.cyclicRedundancyCheck( data, decoded );
	}
	
	
	
	/**
	 * Encode uncompressed data using Huffman coding.
	 */
	public static byte[] encode( byte[] data ) {
		if (data.length == 0)
			return new byte[ 0 ];
		
		HuffTable table = FrequencyAnalyser.analyse( data );		
		HuffTree  tree  = new HuffTree( table );
		
		byte[] huffTable = HuffTable.encode( table );
		byte[] huffCodes = tree.encode( data );
		
		printStats( table, tree, data, huffTable, huffCodes );
		
		return Util.arrayConcat( huffTable, huffCodes );
	}
	
	
	
	/**
	 * Decode huffman compressed data.
	 */
	public static byte[] decode( byte[] data ) {
		if (data.length == 0)
			return new byte[ 0 ];
		
		HuffTable table      = new HuffTable();
		int       byteOffset = HuffTable.decode( table, data );
		
		HuffTree tree = new HuffTree( table );
		return tree.decode( data, byteOffset );
	}
	
	
	
	private static void printStats( HuffTable table, HuffTree tree, byte[] data, byte[] huffTable, byte[] huffCodes ) {
		table.printFrequencies();
		tree .printCodes();
		printSizeInfo( data, huffTable, huffCodes );
	}
	
	
	
	private static void printSizeInfo( byte[] data, byte[] huffTable, byte[] huffCodes ) {
		int    sizeRaw         = data.length;
		int    sizeHuffActual  = huffCodes.length;
		int    sizeHuffTable   = huffTable.length;
		int    sizeHuffTotal   = sizeHuffActual + sizeHuffTable;
		double sizeRatio       = sizeHuffTotal / (double) sizeRaw;
		int    sizePercent     = (int) Math.round( sizeRatio * 100 );
		
		System.out.println( "\n\nData sizes (bytes):" );
		System.out.println( "Original:    " + sizeRaw         );
		System.out.println( "Coded:       " + sizeHuffActual  );
		System.out.println( "Table:       " + sizeHuffTable   );
		System.out.println( "Total:       " + sizeHuffTotal   );
		System.out.println( "Size factor: " + sizePercent + "%" );
	}
}








































