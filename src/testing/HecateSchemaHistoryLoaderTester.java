package testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.deprecated.HecateSchemaHistoryLoadService;
import foreignkeyshistory.deprecated.SchemaHistoryLoadService;
import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;
import model.DBVersion;
import model.Loader.HecateManager;

/**
 * This class is used to test the HecateSchemaHistoryLoader class.
 * 
 * The test is a black box and it operates as follows:
 * 
 * I have a directory DataSetsSourceDirectory that contains subdirectories. Each such subdirectory is a dataset that contains
 * the sql files and the transitions.xml file.
 * This test checks all datasets. 
 * For each dataset it loads the schemas and transitions using the HecateSchemaHistoryLoader class and the 
 * model.Loader package. Then it tests that the two retrieved models are the same.
 */
public class HecateSchemaHistoryLoaderTester {

	public boolean areDatasetsEqual(Path datasets) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		String [] datasetsAsStrings = datasets.toFile().list();
		
		for (String datasetName : datasetsAsStrings)
		{
			File dataset = new File(datasets.toFile(), datasetName);
			
			if (!areHistoriesEqual(dataset.toPath())){ return false; }
		}
		
		return true;
	}

	private boolean areHistoriesEqual(Path dataset) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		// The transitions file is under the dataset directory
		File transitionsFile = new File(dataset.toFile(), "transitions.xml");
		
		// First i load using the HecateSchemaHistoryLoader class.
		SchemaHistoryLoadService loadService = new HecateSchemaHistoryLoadService();
		
		Path[] input = new Path[2];
		input[0] = dataset;
		input[1] = transitionsFile.toPath();
		foreignkeyshistory.SchemaHistory schemaHistory = loadService.load(input);
		
		
		// Now i load using HecateManager
		HecateManager hecateManager = new HecateManager();
		
		ArrayList<DBVersion> lifetime = hecateManager.parseSql(dataset.toString());
		
		// i must sort the lifetime array according the version
		//Arrays.sort(lifetime);
		ArrayList<Map<String,Integer>> transitions = hecateManager.parseXml(transitionsFile.toString());
		
		return areHistoriesEqual(schemaHistory, lifetime, transitions);
	}
	
	/**
	 * This method checks if the two schema histories are the same.
	 * 
	 * The first history is represented by the SchemaHistory object.
	 * The second history is represented by the lifetime and transitions objects.
	 * 
	 * Important Assumption: The lifetime list is passed in chronological order.
	 */
	private boolean areHistoriesEqual(SchemaHistory schemaHistory, ArrayList<DBVersion> lifetime, ArrayList<Map<String,Integer>> transitions)
	{
		return areSchemasEqual(schemaHistory, lifetime) && areTransitionsEqual(schemaHistory, transitions);
	}
	
	private boolean areSchemasEqual(SchemaHistory schemaHistory, ArrayList<DBVersion> lifetime)
	{
		int numVersions = schemaHistory.getNumberOfSchemaVersions();
		
		if (numVersions != lifetime.size())
		{
			return false;
		}
		
		for (int version = 0; version < numVersions; ++version)
		{
			Schema schema1 = schemaHistory.getSchemaVersion(version);
			DBVersion schema2 = lifetime.get(version);
			
			if (!areTablesEqual(schema1, schema2) || !areForeignKeysEqual(schema1, schema2)){ return false; }
		}
		
		return true;
	}
	
	private boolean areTablesEqual(Schema schema1, DBVersion schema2)
	{
		// Every table of schema1 must belong to schema2 and the reverse
		Iterator<foreignkeyshistory.Table> schema1TableIterator = schema1.getTableIterator();
		
		while (schema1TableIterator.hasNext())
		{
			foreignkeyshistory.Table table = schema1TableIterator.next();
			
			if (!tableExists(table.getName(), schema2)){ return false; }
		}
		
		for (model.Table table : schema2.getTables())
		{
			if (!tableExists(table.getKey(), schema1)){ return false; }
		}
		
		return true;
	}
	
	private boolean tableExists(String tableName, Schema schema1)
	{
		return schema1.getTableWithName(tableName) != null;
	}
	
	private boolean tableExists(String tableName, DBVersion schema2)
	{
		for (model.Table table : schema2.getTables())
		{
			if (table.getKey().equals(tableName)){ return true; }
		}
		return false;
	}
	
	private boolean areForeignKeysEqual(Schema schema1, DBVersion schema2)
	{
		// all foreign keys in schema1 must belong to schema2 and the reverse
		Iterator<foreignkeyshistory.ForeignKey> foreignKeyIterator = schema1.getForeignKeyIterator();
		
		while (foreignKeyIterator.hasNext())
		{
			foreignkeyshistory.ForeignKey foreignKey = foreignKeyIterator.next();
			
			if (!foreignKeyExists(foreignKey.getSourceTable().getName(), foreignKey.getTargetTable().getName(), schema2)){ return false; }
		}
		
		for (model.ForeignKey foreignKey : schema2.getVersionForeignKeys())
		{
			if (!foreignKeyExists(foreignKey.getSourceTable(), foreignKey.getTargetTable(), schema1)){ return false; }
		}
		
		return true;
	}
	
	private boolean foreignKeyExists(String sourceTable, String targetTable, Schema schema1)
	{
		Iterator<foreignkeyshistory.ForeignKey> foreignKeysIterator = schema1.getForeignKeyIterator();
		
		while (foreignKeysIterator.hasNext())
		{
			foreignkeyshistory.ForeignKey foreignKey = foreignKeysIterator.next();
			
			if (foreignKey.getSourceTable().getName().equals(sourceTable) && foreignKey.getTargetTable().getName().equals(targetTable)){ return true; }
		}
		
		return false;
	}
	
	private boolean foreignKeyExists(String sourceTable, String targetTable, DBVersion schema2)
	{
		for (model.ForeignKey foreignKey : schema2.getVersionForeignKeys())
		{
			if (foreignKey.getSourceTable().equals(sourceTable) && foreignKey.getTargetTable().equals(targetTable)){ return true; }
		}
		
		return false;
	}
	
	private boolean areTransitionsEqual(SchemaHistory schemaHistory, ArrayList<Map<String,Integer>> transitions)
	{
		int numVersions = schemaHistory.getNumberOfSchemaVersions();
		
		if (numVersions != transitions.size() + 1)
		{
			return false;
		}
		
		int numTransitions = numVersions - 1;
		int transition = 0;
		Iterator<Set<foreignkeyshistory.SchemaTransition>> schemaTransitionsIterator = schemaHistory.getSchemaTransitionsIterator();
		
		while (schemaTransitionsIterator.hasNext())
		{
			Set<foreignkeyshistory.SchemaTransition> schemaTransitions = schemaTransitionsIterator.next();
			Map<String,Integer> otherTransitions = transitions.get(transition);
			
			if (!areTransitionsForSchemaEqual(schemaTransitions, otherTransitions)){ return false; }
			
			transition = transition + 1;
		}
		
		return true;
	}
	
	private boolean areTransitionsForSchemaEqual(Set<foreignkeyshistory.SchemaTransition> schemaTransitions, Map<String,Integer> otherTransitions)
	{
		// all transitions from schemaTransitions must belong to otherTransitions and the reverse
		
		for (foreignkeyshistory.SchemaTransition schemaTransition : schemaTransitions)
		{
			if (!transitionExists(schemaTransition, otherTransitions)){ return false; }
		}
		
		for (Map.Entry<String, Integer> entry : otherTransitions.entrySet())
		{
			if (!transitionExists(entry.getKey(), entry.getValue(), schemaTransitions)){ return false; }
		}
		
		return true;
	}
	
	private boolean transitionExists(foreignkeyshistory.SchemaTransition schemaTransition, Map<String,Integer> otherTransitions)
	{
		String affectedTable = schemaTransition.getAffectedTable().getName();
		if (!otherTransitions.containsKey(affectedTable))
		{
			return false;
		}
			
		Integer changeType = otherTransitions.get(affectedTable);
		
		switch(schemaTransition.getSchemaTransitionChange())
		{
		case TABLE_CREATED:
			if (changeType.intValue() != parmenidianEnumerations.Status.CREATION.getValue()){ return false; }
			break;
		case TABLE_DELETED:
			if (changeType.intValue() != parmenidianEnumerations.Status.DELETION.getValue()){ return false; }
			break;
		case TABLE_UPDATED:
			if (changeType.intValue() != parmenidianEnumerations.Status.UPDATE.getValue()){ return false; }
			break;
		default:
			assert true;
		}
		
		return true;
	}
	
	private boolean transitionExists(String affectedTable, Integer changeType, Set<foreignkeyshistory.SchemaTransition> schemaTransitions)
	{
		for (foreignkeyshistory.SchemaTransition schemaTransition : schemaTransitions)
		{
			foreignkeyshistory.Table table = schemaTransition.getAffectedTable();
			
			if (table.getName().equals(affectedTable))
			{
				if (changeType.intValue() == parmenidianEnumerations.Status.CREATION.getValue() && schemaTransition.getSchemaTransitionChange() == foreignkeyshistory.SchemaTransitionChange.TABLE_CREATED){ return true; }
				if (changeType.intValue() == parmenidianEnumerations.Status.DELETION.getValue() && schemaTransition.getSchemaTransitionChange() == foreignkeyshistory.SchemaTransitionChange.TABLE_DELETED){ return true; }
				if (changeType.intValue() == parmenidianEnumerations.Status.UPDATE.getValue() && schemaTransition.getSchemaTransitionChange() == foreignkeyshistory.SchemaTransitionChange.TABLE_UPDATED){ return true; }
			}
		}
		
		return false;
	}
}

