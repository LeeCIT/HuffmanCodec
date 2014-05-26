

package huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;



/**
 * Provides simple IO functions.
 * @author Lee Coakley
 */
public final class IO
{
	private IO() {}
	
	
	
	public static byte[] read( String file ) throws IOException {		
		Path path = Paths.get( file );
		return Files.readAllBytes( path );
	}
	
	
	
	public static void write( String file, byte[] bytes, boolean append ) throws IOException {		
		Path path = Paths.get( file );
		
		if (append)
			 Files.write( path, bytes, StandardOpenOption.APPEND );
		else Files.write( path, bytes );
	}
}
