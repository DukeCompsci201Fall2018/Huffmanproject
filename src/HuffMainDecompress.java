import java.io.File;
import java.io.*;

public class HuffMainDecompress {
	public static void main(String[] args) {
		
		System.out.println("Huffman Decompress Main");
		
		File inf = FileSelector.selectFile();
		File outf = FileSelector.saveFile();
		if (inf == null || outf == null) {
			System.err.println("input or output file cancelled");
			return;
		}
		BitInputStream bis = new BitInputStream(inf);
		BitOutputStream bos = new BitOutputStream(outf);
		HuffProcessor hp = new HuffProcessor();
		hp.decompress(bis, bos);
		System.out.printf("uncompress from %s to %s\n", 
				           inf.getName(),outf.getName());		
		
		System.out.printf("file: %d bits to %d bits\n",inf.length()*8,outf.length()*8);
		System.out.printf("read %d bits, wrote %d bits\n", 
				           bis.bitsRead(),bos.bitsWritten());
		long diff = 8*(outf.length() - inf.length());
		long diff2 = bos.bitsWritten() - bis.bitsRead();
		System.out.printf("%d compared to %d\n",diff,diff2);
	}
}
