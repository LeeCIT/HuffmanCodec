


package huffman;



/**
 * A node in the huffman tree.
 * @author Lee Coakley
 */
public class HuffNode implements Comparable<HuffNode>
{
	public HuffChar hc;
	public HuffNode left;
	public HuffNode right;
	public String   code;
	
	
	
	public HuffNode( HuffChar hc, HuffNode left, HuffNode right ) {
		this.hc    = hc;
		this.left  = left;
		this.right = right;
	}
	
	
	
	public int compareTo( HuffNode o ) {
		return getFreqSum() - o.getFreqSum();
	}
	
	
	
	/**
	 * Check whether the node is a character node (leaf).
	 */
	public boolean hasChar() {
		return hc != null;
	}
	
	
	
	/**
	 * Check if the node has a left child.
	 */
	public boolean hasLeft() {
		return left != null;
	}
	
	
	
	/**
	 * Check if the node has a right child.
	 */
	public boolean hasRight() {
		return right != null;
	}
	
	
	
	/**
	 * Get the sum of frequencies for the node and all its children.
	 */
	public int getFreqSum() {
		int sum = 0;
		
		if (hasChar())  sum += hc.freq;
		if (hasLeft())  sum += left .getFreqSum();
		if (hasRight()) sum += right.getFreqSum();
		
		return sum;
	}
	
	
	
	/**
	 * Visit every node using the provided traverser.
	 * Traversal is depth-first.
	 */
	public void traverse( Traverser<HuffNode> trav ) {
		trav.process( this );
		if (hasLeft())  left .traverse( trav );
		if (hasRight()) right.traverse( trav );
	}
	
	
	
	/**
	 * Print out the tree.
	 * Indentation denotes the level.
	 */
	public void print() {
		System.out.println( "\n\nTree: " );
		print( 0 );
	}
	
	
	
	private void print( int level ) {
		for (int i=0; i<level; i++)
			System.out.print( "  " );
		
		String desc = "" + getFreqSum();
		
		if (hasChar())
			desc += " (" + hc.index + ")";
		
		System.out.println( "+ " + desc );
		
		if (hasLeft())  left .print( level + 1 );
		if (hasRight()) right.print( level + 1 );
	}
}


































