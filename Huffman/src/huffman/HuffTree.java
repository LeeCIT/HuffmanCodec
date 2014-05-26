


package huffman;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



/**
 * A huffman tree.
 * Contains nodes arranged in a binary tree according to the sum of frequencies for each node.
 * Going left is 0, going right is 1.
 */
public class HuffTree
{
	private HuffNode   root;
	private HuffNode[] lookupTable;
	
	
	
	public HuffTree( HuffTable table ) {
		this.root = genTree( table );
		genCodes();
		genLookupTable();
	}
	
	
	
	public void print() {
		root.print();
	}
	
	
	
	/**
	 * Get the prefix code for the given byte.
	 */
	public String getCode( byte b ) {
		int u = b & 0xFF;
		return lookupTable[ u ].code;
	}
	
	
	
	/**
	 * Get the prefix code for the given char.
	 * @param hc
	 * @return
	 */
	public String getCode( HuffChar hc ) {
		int u = hc.index;
		return lookupTable[ u ].code;
	}
	
	
	
	/**
	 * Traverse each node of the tree.
	 */
	public void traverse( Traverser<HuffNode> trav ) {
		root.traverse( trav );
	}
	
	
	
	/**
	 * Huffman code the data.
	 */
	public byte[] encode( byte[] data ) {
		BitStream bs = new BitStream();
		
		for (byte b: data)
			bs.add( getCode(b) );
		
		return bs.toArray();
	}
	
	
	
	/**
	 * Decode huffman encoded data, giving back its original form.
	 */
	public byte[] decode( byte[] data, int byteOffs ) {
		BitStream  bs      = new BitStream( data );
		ByteVector out     = new ByteVector();
		int        bitOffs = byteOffs * 8;
		int        freqs   = root.getFreqSum();
		
		readPrefixes( bs, bitOffs, out, freqs );
		
		return out.toArray();
	}
	
	
	
	private void readPrefixes( BitStream bs, int bitOffs, ByteVector out, int freqs ) {
		for (int i=0; i<freqs; i++)
			bitOffs += readPrefixFrom( bs, bitOffs, out );
	}



	private int readPrefixFrom( BitStream bs, int bitOffs, ByteVector out ) {
		HuffNode node = root;
		
		for (int i=0; ; i++) {
			int bit = bs.get( bitOffs + i );
			
			if (bit == 1)
				 node = node.right;
			else node = node.left;
			
			if (node.hasChar()) {
				out.add( (byte) node.hc.index );
				return i + 1;
			}
		}
	}



	private void genCodes() {
		String str = "";
		genCodes( root, str );
	}
	
	
	
	private void genCodes( HuffNode node, String str ) {
		if (node.hasChar())  node.code = str;
		if (node.hasLeft())  genCodes( node.left,  str + "0" );
		if (node.hasRight()) genCodes( node.right, str + "1" );
	}
	
	
	
	private void genLookupTable() {
		lookupTable = new HuffNode[ 256 ];
		
		traverse( new Traverser<HuffNode>() {
			public void process( HuffNode node ) {
				if (node.hasChar())
					lookupTable[ node.hc.index ] = node;
			}
		});
	}
	
	
	
	private HuffNode genTree( HuffTable table ) {
		PriorityQueue<HuffNode> nodes = genNodePriQueue( table );
		HuffNode root = null;
		
		if (nodes.size() == 1)
			return new HuffNode( null, nodes.poll(), null ); // Has to be at least two nodes
		
		while (nodes.size() >= 2) {
			HuffNode left  = nodes.poll();
			HuffNode right = nodes.poll();
			         root  = new HuffNode( null, left, right );
			
			nodes.add( root );
		}
		
		if ( ! nodes.isEmpty()
		&&     nodes.peek().hasChar())
			throw new RuntimeException( "Tree gen failed: skipped " + nodes.peek().hc );
		
		return root;
	}
	
	
	
	private PriorityQueue<HuffNode> genNodePriQueue( HuffTable table ) {
		PriorityQueue<HuffNode> pq = new PriorityQueue<>( table.size() );
		
		for (HuffChar hc: table)
			pq.add( new HuffNode(hc,null,null) );
		
		return pq;
	}
	
	
	
	public void printCodes() {
		final List<HuffNode> nodes = new ArrayList<>();
		
		traverse( new Traverser<HuffNode>() {
			public void process( HuffNode node ) {
				if (node.hasChar())
					nodes.add( node );
			}
		});
		
		Collections.sort( nodes, new Comparator<HuffNode>() {
			public int compare( HuffNode a, HuffNode b ) {
				return a.code.length() - b.code.length();
			}
			
		});
		
		System.out.println( "\n\nCodes:" );
		for (HuffNode n: nodes)
			System.out.println( "" + n.hc + ": \t" + n.code );
	}
}































