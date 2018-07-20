package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class converter {
	public static void main(String[] args) throws IOException {
		
		File file = new File("C:\\\\Users\\\\ivanm\\\\Documents\\\\school\\\\TEC\\\\Delfin\\data.xml");
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
