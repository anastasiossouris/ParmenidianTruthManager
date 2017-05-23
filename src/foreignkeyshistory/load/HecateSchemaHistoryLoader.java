package foreignkeyshistory.load;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import externalTools.Attribute;
import externalTools.Deletion;
import externalTools.HecateParser;
import externalTools.Insersion;
import externalTools.Table;
import externalTools.Transition;
import externalTools.TransitionList;
import externalTools.Transitions;
import externalTools.Update;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.SchemaTransitionChange;
import foreignkeyshistory.deprecated.SchemaBuilder;
import foreignkeyshistory.deprecated.SchemaHistoryBuilder;

/**
 * An implementation of the SchemaHistoryLoader interface using the Hecate functionality.
 */
public class HecateSchemaHistoryLoader implements SchemaHistoryLoader{
	
	/**
	 * The first Path object specifies the directory that contains the sql files.
	 * The chronological order of the versions coincides with their alphabetical order.
	 * The second Path object specifies the XML file that contains the transitions.
	 */
	@Override
	public SchemaHistory loadSchemaHistory(Path [] input)
			throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException 
	{
		assert input.length == 2;
		
		SchemaHistoryBuilder schemaHistoryBuilder = new SchemaHistoryBuilder();
		
		SchemaHistory schemaHistory = new SchemaHistory();
		
		loadSchemaVersions(input[0], schemaHistory);
		loadSchemaTransitions(input[1], schemaHistory);
		
		return schemaHistory;
	}

	
		
	/**
	 * The path is the XML transitions file which is loaded and added to the schema history.
	 */
	private void loadSchemaTransitions(Path path, SchemaHistory schemaHistory) 
	{
		externalTools.Transitions t = loadTransitionsFromXML(path.toFile());
		int oldVersion = 0;
		int newVersion = 1;
		
		for (externalTools.TransitionList tl : t.getList())
		{
			for (externalTools.Transition transition : tl.getTransitionList())
			{
				String affectedTable = transition.getAffTable().getName();
				SchemaTransitionChange transitionType = getSchemaTransitionChange(transition);
				
				if (transitionType == null){ continue; }
				
				schemaHistory.addSchemaTransition(oldVersion, newVersion, affectedTable, transitionType);
			}
		
			oldVersion = oldVersion + 1;
			newVersion = newVersion + 1;
		}
	}

	
	
	private SchemaTransitionChange getSchemaTransitionChange(Transition transition) {
		SchemaTransitionChange change = null;
		
		switch (transition.getType())
		{
		case "NewTable":
			change = SchemaTransitionChange.TABLE_CREATED;
			break;
		case "DeleteTable":
			change = SchemaTransitionChange.TABLE_DELETED;
			break;
		case "UpdateTable":
			change = SchemaTransitionChange.TABLE_UPDATED;
			break;
		default:
			// i don't care for other change types.
			break;
		}
		
		return change;
	}



	private externalTools.Transitions loadTransitionsFromXML(File transition)
	{
		InputStream inputStream;
		Transitions t = null;
		try {
			inputStream = new FileInputStream(transition.getAbsolutePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			t = (Transitions)u.unmarshal( inputStream );
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	
		
	/**
	 * The path input is a directory containing all the sql versions. This method loads the Schema versions using
	 * the passed SchemaHistoryBuilder object. 
	 */
	private void loadSchemaVersions(Path input, SchemaHistory schemaHistory)
	{
		File [] versions = input.toFile().listFiles(new SQLFileFilter());
		
		// The assumption is that the chronological order of the versions coincides with their
		// alphabetical order.
		Arrays.sort(versions);
		
		for (File version : versions)
		{
			schemaHistory.appendSchemaVersion(loadSchema(version.toPath()));
		}
	}
	
	
	
	/**
	 * 
	 * @param input			The Path object representing the sql DDL file
	 * @return				A constructed Schema object from the sql file.
	 */
	private Schema loadSchema(Path input)
	{
		File file = input.toFile();
		Schema schema = new Schema();
		
		HecateParser parser = new HecateParser();
		externalTools.Schema parsedSchema = parser.parse(file.getAbsolutePath());

		addTablesToSchema(parsedSchema, schema);
		addForeignKeysToSchema(parsedSchema, schema);
		setVersionNameToSchema(input, schema);
		
		return schema; 
	}


	
	private void setVersionNameToSchema(Path input, Schema schema)
	{
		String sqlFile = input.toFile().getName();
		int index = sqlFile.indexOf(".");
		String name = sqlFile.substring(0, index);
		schema.setVersionName(name);
	}
	
	
	
	/**
	 * Adds the foreign keys in the parsed schema as ForeignKey objects using the passed schema builder.
	 */
	private void addForeignKeysToSchema(externalTools.Schema parsedSchema, Schema schema) 
	{
		TreeMap<String, externalTools.Table> versionTables = parsedSchema.getTables();
		
		for(Map.Entry<String, Table> iterator : versionTables.entrySet())
		{
			Set<Map.Entry<Attribute, Attribute>> fkList = iterator.getValue().getfKey().getForeignKeys();
			
			for (Map.Entry<Attribute, Attribute> entry : fkList) {
				Attribute or = entry.getKey();
				Attribute re = entry.getValue();
			
				schema.addForeignKey(or.getTable().getName(), re.getTable().getName());
			}
		}
	}

	

	/**
	 * Adds the tables as Table objects in the constructed Schema using the passed SchemaBuilder object.
	 */
	private void addTablesToSchema(externalTools.Schema parsedSchema, Schema schema) 
	{
		schema.addTables(parsedSchema.getAllTables());
	}
	
	
	
	private class SQLFileFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".sql"))
				return true;
			return false;
			
		}
		
	}
}
