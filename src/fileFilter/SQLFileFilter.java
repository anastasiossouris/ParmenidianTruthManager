package fileFilter;

import java.io.File;
import java.io.FileFilter;

public class SQLFileFilter implements FileFilter {
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".sql"))
			return true;
		return false;
		
	}
	
}