package fileFilter;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".png") || pathname.getName().endsWith(".jpg"))
			return true;
		return false;
		
	}
	
}