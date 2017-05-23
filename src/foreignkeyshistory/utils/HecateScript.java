package foreignkeyshistory.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import externalTools.Deletion;
import externalTools.Delta;
import externalTools.HecateParser;
import externalTools.Insersion;
import externalTools.Schema;
import externalTools.TransitionList;
import externalTools.Transitions;
import externalTools.Update;

public class HecateScript {	
	private File selectedDirectory;
	private File[] sqlFiles;
	
	public HecateScript(File folder) throws Exception{
		Exception wrong = new Exception();

		selectedDirectory=folder;
		sqlFiles = selectedDirectory.listFiles(new SQLFileFilter());
		
		if(sqlFiles.length==0){
			throw wrong;
		}
		
	}
	
	public void createTransitions(){	
		HecateParser parser= new HecateParser();
		Schema currentSchema;
		ArrayList<Schema> schemata= new ArrayList<Schema>();

		//create schema per sql and store them
		for(int i=0;i<sqlFiles.length;++i){			
			currentSchema=parser.parse(sqlFiles[i].getAbsolutePath());
			currentSchema.setTitle(String.valueOf(i));
			schemata.add(currentSchema);
		}
	
		Delta delta = new Delta();
		TransitionList tl;
		Transitions trs = new Transitions();
	
		for(int i=0;i<schemata.size()-1;++i){
			tl=(delta.minus(schemata.get(i),schemata.get(i+1))).tl;
			trs.add(tl);			
		}
	
		marshal(trs,selectedDirectory);
	}


	private void marshal(Transitions trs,File selectedDirectory) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(trs, new File(selectedDirectory +"\\transitions.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public class SQLFileFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".sql"))
				return true;
			return false;
			
		}
		
	}
}
