package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * A class containing a function to convert an *.xml file into a *.lbp file
 * @author Ulises Martinez
 * @deprecated Not used in the main class.
 */
public class converter {
	
	/**
	 * A function to convert an *.xml file into a *.lbp file
	 * @param args - args[0] is the filename (with filepath) of the *.xml file to convert; args[1] is the  filename (with filepath) of the *.lbp file to create
	 * @throws IOException - Throws exception in case the file does not exist or there is some writing error 
	 */
	public static void main(String[] args) throws IOException  {
		
		File file = new File(args[0]);
		String curr = "";
		String out = "";
		String times = "[";
		String variance = "[";
		BufferedReader br = new BufferedReader(new FileReader(file));
		br.readLine();
		br.readLine();
		while (!curr.contains("</DocumentElement>")) {
			curr = br.readLine();
			curr = curr.replaceAll("\\s+", "");
			if (curr.contains("<datos>")) {
				while (!curr.contains("</datos>")) {
					curr = br.readLine();
					curr = curr.replaceAll("\\s+", "");
					if (curr.contains("<Tiempo>")) {
						curr = curr.substring(8, curr.length() - 9);
						times += curr + ", ";
					} else if (curr.contains("<Varianza>")) {
						curr = curr.substring(10, curr.length() - 11);
						variance += curr + ", ";
					} else if (curr.contains("<Precedencia>")) {
						curr = curr.substring(13, curr.length() - 14);
						out += Arrays.toString(curr.split(",")) + "\n";
					}
				}
			}
		}
		br.close();
		times = times.substring(0, times.length() - 2) + "]\n";
		variance = variance.substring(0, variance.length() - 2) + "]\n";

		file = new File("C:\\Users\\ivanm\\Documents\\school\\TEC\\Delfin\\3\\A37.lbp");
		BufferedWriter  bw = new BufferedWriter(new FileWriter(file));
		bw.write(times);
		bw.write(variance);
		bw.write(out);
		bw.close();
	}
}
