


package huffman;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



/**
 * A collection of Huffman codable characters.
 * Should be kept sorted.  Call sort() after adding chars.
 * 
 * Data format:
 * 		Header:
 *  		[uint9] number of elements in freq table
 *  		[uint5] bits per frequency
 *  		[uint2] padding
 *  		
 *  	Element:
 *  		[uint8] index
 *  		[uint?] frequency
 *
 *
 * @author Lee Coakley
 */
public class HuffTable implements Iterable<HuffChar>
{
	private static final int headerCountBits = 9;
	private static final int headerFreqBits  = 5;
	private static final int headerPadBits   = 2;
	private static final int headerBits      = headerCountBits + headerFreqBits + headerPadBits;
	private static final int elemIndexBits   = 8;
	
	private List<HuffChar> chars;
	
	
	
	public HuffTable() {
		chars = new ArrayList<>();
	}
	
	
	
	public HuffTable( int length ) {
		chars = new ArrayList<>( length );
	}
	
	
	
	public void add( HuffChar hc ) {
		chars.add( hc );
	}
	
	
	
	public HuffChar get( int index ) {
		return chars.get( index );
	}
	
	
	
	public int size() {
		return chars.size();
	}
	
	
	
	public Iterator<HuffChar> iterator() {
		return chars.iterator();
	}
	
	
	
	public void sort() {
		Collections.sort( chars );
	}
	
	
	
	/**
	 * Serialize the table to a compact form.
	 */
	public static byte[] encode( HuffTable table ) {
		BitStream bs = new BitStream();
		
		int count        = table.size();
		int bitsPerIndex = elemIndexBits;
		int bitsPerFreq  = getBitsPerFrequency( table );
		
		bs.add( count,       headerCountBits );
		bs.add( bitsPerFreq, headerFreqBits  );
		bs.add( 0,           headerPadBits   );
		
		for (HuffChar hc: table) {
			bs.add( hc.index, bitsPerIndex );
			bs.add( hc.freq,  bitsPerFreq  );
		}
		
		return bs.toArray();
	}
	
	
	
	/**
	 * Rebuild the table from a compact form.
	 * @return Size of the encoded table in bytes.  Use to advance read caret.
	 */
	public static int decode( HuffTable table, byte[] raw ) {
		checkSize( raw );
		
		BitStream bs = new BitStream( raw );
		
		int elemCount = bs.get( 0,               headerCountBits );
		int freqBits  = bs.get( headerCountBits, headerFreqBits  );
		int elemBits  = elemIndexBits + freqBits;
		int totalBits = headerBits + (elemCount * elemBits);
		
		checkFreqBits( freqBits );
		
		for (int i=0; i<elemCount; i++) {
			int offsI = headerBits + (elemBits * i);
			int offsF = offsI + elemIndexBits;
			int index = bs.get( offsI, elemIndexBits );
			int freq  = bs.get( offsF, freqBits      );
			
			table.add( new HuffChar( index, freq) );
		}
		
		return ceilToBytes( totalBits );
	}
	
	
	
	private static void checkSize( byte[] raw ) {
		if (raw.length < 2)
			throw new RuntimeException( "Bad size: " + raw.length );
	}
	
	
	
	private static void checkFreqBits( int freqBits ) {
		if (freqBits <= 0 || freqBits >= 32)
			throw new RuntimeException( "Bad freq bits: " + freqBits );
	}
	
	
	
	/**
	 * Print out the character frequencies with a nice graph.
	 */
	public void printFrequencies() {
		int lenDiv = getLengthDivider();
		
		System.out.print( "\n\nFrequencies:" );
		
		for (HuffChar hc: this) {
			int freq = hc.freq;
					
			System.out.print( "\n" + hc + ": \t" + freq + "\t " );
		
			for (int f=0; f<freq/lenDiv; f++)
				System.out.print( "|" );
		}
		
		System.out.println();
	}
	
	
	
	private int getLengthDivider() {
		int fmax = 0;
		
		for (HuffChar hc: this)
			fmax = Math.max( fmax, hc.freq );
		
		int div = fmax / 72;
		return Math.max( div, 1 );
	}
	
	
	
	private static int getBitsPerFrequency( HuffTable table ) {
		int highest = table.getHighestFrequency();
		int log2    = log2i( highest );
		return Math.max( 1, log2 );
	}
	
	
	
	private int getHighestFrequency() {
		int last = size() - 1;
		return get(last).freq;
	}
	
	
	
	private static int log2i( int x ) {
		double logx = Math.log( x );
		double log2 = Math.log( 2 );
		double log  = logx / log2;
		return (int) Math.ceil( log );
	}
	
	
	
	private static int ceilToBytes( int bits ) {
		return (int) Math.ceil( bits / 8.0 );
	}
}










































