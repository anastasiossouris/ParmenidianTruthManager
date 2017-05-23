package foreignkeyshistory.deprecated;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.Table;

/**
 * This is a utility class to build Schema objects.
 */
public class SchemaBuilder {
	private Schema schema = null;
	private String versionName; 
	
	// key: the name of the table, value: the Table object
	private Map<String, Table> allTables = null;
	
	// key: the source table name of a foreign key.
	// value: a map that has as key the target tables and as value the corresponding ForeignKey object
	// for that source, target pair.
	private Map<String, Map<String, ForeignKey> > allForeignKeys = null;
	
	public SchemaBuilder()
	{
		schema = new Schema();
		allTables = new HashMap<>();
		allForeignKeys = new HashMap<>();
	}
	
	
	public void setVersionName(String versionName)
	{
		assert versionName != null;
		
		this.versionName = versionName;
	}
	
	/**
	 * Adds the table names by constructing new Table objects where appropriate (that is removing duplicates).
	 * The parameter must be non-null.
	 */
	public void addTables(Collection<String> tableNames)
	{
		assert tableNames != null;
		
		for (String tableName : tableNames)
		{
			addTable(tableName);
		}
	}
	
	/**
	 * If a table with the given name doesn't exist then a Table object is constructed.
	 * The parameter tableName must be non-null.
	 */
	public void addTable(String tableName)
	{
		assert tableName != null;
		
		if (!allTables.containsKey(tableName))
		{
			allTables.put(tableName, new Table(tableName));
		}
	}
	
	/**
	 * If the sourceTable and targetTable tables exist and a foreignKey object doesn't already exist between them
	 * then a ForeignKey object is created. Both parameters must be non-null.
	 * Also, if at least one of the tables doesn't exist then this method throws a IllegalArgumentException.
	 */
	public void addForeignKey(String sourceTableName, String targetTableName)
	{
		Table sourceTable = allTables.get(sourceTableName);
		Table targetTable = allTables.get(targetTableName);
		
		if (sourceTable == null || targetTable == null)
		{
			throw new IllegalArgumentException("tables do not exist");
		}
		
		Map<String, ForeignKey> foreignKeysWithSourceTable = getForeignKeysWithSourceTable(sourceTableName);
		
		addTargetTableToForeignKeyWithSourceTable(foreignKeysWithSourceTable, targetTableName, sourceTable, targetTable);
	}
	
	/**
	 * The passed foreignKeysWithSourceTable map represents all the target tables (and the corresponding ForeignKey 
	 * objects) for a specific source table. This method checks if the targetTableName is listed there. If not,
	 * it creates a new ForeignKey object (using sourceTable and targetTable) and inserts it.
	 */
	private void addTargetTableToForeignKeyWithSourceTable(Map<String, ForeignKey> foreignKeysWithSourceTable,
			String targetTableName, Table sourceTable, Table targetTable) {
		assert foreignKeysWithSourceTable != null;
		assert targetTableName != null;
		assert sourceTable != null;
		assert targetTable != null;
		
		if (!foreignKeysWithSourceTable.containsKey(targetTableName))
		{
			foreignKeysWithSourceTable.put(targetTableName, new ForeignKey(sourceTable, targetTable));
		}
	}

	/**
	 * For the given source table return the map that gives for each target table the corresponding ForeignKey object.
	 * If this is the first time, then create the map and then return it.
	 */
	private Map<String, ForeignKey> getForeignKeysWithSourceTable(String sourceTableName)
	{
		assert sourceTableName != null;
		
		Map<String, ForeignKey> foreignKeysWithSourceTable = null;
		if (allForeignKeys.containsKey(sourceTableName))
		{
			foreignKeysWithSourceTable = allForeignKeys.get(sourceTableName);
		}
		else
		{
			foreignKeysWithSourceTable = new HashMap<>();
			allForeignKeys.put(sourceTableName, foreignKeysWithSourceTable);
		}

		return foreignKeysWithSourceTable;
	}
	
	/**
	 * Returns the Schema object as constructed so far. If the Schema is in an invalid state, then 
	 * the method throws a IllegalStateException.
	 */
	public Schema buildSchema()
	{
		if (!isSchemaValid())
		{
			throw new IllegalStateException("Schema object not constructed properly");
		}
		
		addTablesToFinalSchema();
		addForeignKeysToFinalSchema();
		schema.setVersionName(versionName);
		
		return schema;
	}
	
	private void addTablesToFinalSchema() 
	{
		for (Table table : allTables.values())
		{
			schema.addTable(table);
		}
	}

	private void addForeignKeysToFinalSchema()
	{
		for (Map<String, ForeignKey> values : allForeignKeys.values())
		{
			for (ForeignKey foreignKey : values.values())
			{
				schema.addForeignKey(foreignKey);
			}
		}
	}

	/**
	 * XXX: this is not yet implemented.
	 */
	private boolean isSchemaValid()
	{
		return true;
	}
}
