


package huffman;



/**
 * Analyses the frequency distribution of an input and returns
 * a sorted listing.
 * @author Lee Coakley
 */
public class FrequencyAnalyser
{
	/**
	 * Generate a frequency table from the data.
	 */
	public static HuffTable analyse( byte[] data ) {
		int[] freqs = analyseFrequencies( data );
		return genSortedFreqTable( freqs );
	}
	
	
	
	private static int[] analyseFrequencies( byte[] bytes ) {
		int[] freq = new int[ 256 ];
		
		for (byte b: bytes)
			++freq[ b & 0xFF ];
			
		return freq;
	}
	
	
	
	private static HuffTable genSortedFreqTable( int[] freqs ) {
		HuffTable table = new HuffTable( freqs.length );
		
		for (short i=0; i<freqs.length; i++)
			if (freqs[i] > 0)
				table.add( new HuffChar( i, freqs[i] ) );
		
		table.sort();
		
		return table;
	}
}
