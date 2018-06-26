package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class converter
{
	public static void main (String[] args) throws IOException
	{
		File file = new File("Arcus-111.lta"); 
		String curr = "";
		String out = "[0]\n";
		String times = "[";
		BufferedReader br = new BufferedReader(new FileReader(file));
		br.readLine();
		br.readLine();
		while (!curr.contains("</DocumentElement>"))
		{
			curr = br.readLine();
			curr = curr.replaceAll("\\s+","");
			if (curr.contains("<datos>"))
			{
				while (!curr.contains("</datos>"))
				{
					curr = br.readLine();
					curr = curr.replaceAll("\\s+", "");
					if (curr.contains("<Tiempo>"))
					{
						curr = curr.substring(8, curr.length() - 9);
						times += curr + ", ";
					}
					else if (curr.contains("<Precedencia>"))
					{
						curr = curr.substring(13, curr.length() - 14);
						out += Arrays.toString(curr.split(",")) + "\n";
					}
				}
			}
		}
		times = times.substring(0, times.length() -2) + "]";
		System.out.println(times);
		System.out.println(out);
	}
}
