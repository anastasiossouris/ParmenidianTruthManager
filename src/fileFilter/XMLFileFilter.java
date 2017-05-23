package fileFilter;

import java.io.File;
import java.io.FileFilter;

public class XMLFileFilter implements FileFilter {
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".xml"))
			return true;
		return false;
		
	}
	
}