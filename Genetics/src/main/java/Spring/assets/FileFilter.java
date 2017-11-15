package Spring.assets;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter {

	public String[] getDirectories(String dir){
		File file = new File(dir);
		return file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
	}

	public File[] getOwlFiles(String dirName){
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename)
			{ return filename.endsWith(".owl"); }
		} );
	}
}
