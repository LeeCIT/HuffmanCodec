


package huffman;



/**
 * A character for huffman coding.
 * Orders by ascending frequency.
 * @author Lee Coakley
 */
public class HuffChar implements Comparable<HuffChar>
{
	public int index; // Unsigned byte value of character
	public int freq;  // Character's frequency of occurence
	
	
	
	public HuffChar( int index, int freq ) {
		this.index = index;
		this.freq  = freq;
	}
	
	
	
	public int compareTo( HuffChar c ) {
		return freq - c.freq;
	}
	
	
	
	public String toString() {
		return "" + index + "\t (" + toChar(index) + ")";
	}
	
	
	
	private char toChar( int index ) {
		char c = (char) index;
		
		if (c < 32)
			 return ' ';
		else return c;
	}
}