package fileFilter;

import java.io.File;
import java.io.FileFilter;


public class GRAPHMLFileFilter implements FileFilter {
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".graphml"))
			return true;
		return false;
		
	}
	
}