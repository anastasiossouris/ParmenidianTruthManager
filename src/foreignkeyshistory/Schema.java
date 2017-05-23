package foreignkeyshistory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a specific relational schema. That is, its set of tables represents as Table objects and the foreign keys
 * between these objects represented as ForeignKey objects.
 * 
 * To construct a Schema object use a SchemaBuilder object. For that reason, Schema provides only an empty constructor.
 * 
 * If multiple foreign keys exist from a source to a target, then only one ForeignKey object exists.
 */
public class Schema {
	private String versionName = null;
	private Set<ForeignKey> foreignKeys = new HashSet<>();
	
	private Map<String, Table> nameToTable = new HashMap<>();

	// key: the source table name of a foreign key.
	// value: a map that has as key the target tables and as value the corresponding ForeignKey object
	// for that source, target pair.
	private Map<String, Map<String, ForeignKey> > allForeignKeys = new HashMap<>();

	public Schema()
	{}
	
	public ForeignKey getForeignKey(Table source, Table target)
	{
		for (ForeignKey foreignKey : foreignKeys)
		{
			if (foreignKey.getSourceTable() == source && foreignKey.getTargetTable() == target){ return foreignKey; }
		}
		return null;
	}
	
	public void setVersionName(String versionName)
	{
		assert versionName != null;
		
		this.versionName = versionName;
	}

	public int getNumberOfTables()
	{
		return nameToTable.size();
	}
	
	public int getNumberOfForeignKeys()
	{
		return foreignKeys.size();
	}
	
	public String getVersionName()
	{
		return versionName;
	}
	
	public void addTables(Collection<String> tableNames)
	{		
		for (String tableName : tableNames)
		{
			addTable(tableName);
		}
	}

	public void addTable(String tableName)
	{
		if (!nameToTable.containsKey(tableName))
		{
			nameToTable.put(tableName, new Table(tableName));
		}
	}

	public void addTable(Table table)
	{
		assert table != null;
		
		if (!nameToTable.containsKey(table.getName()))
		{
			nameToTable.put(table.getName(), table);
		}
	}
	
	/**
	 * If the sourceTable and targetTable tables exist and a foreignKey object doesn't already exist between them
	 * then a ForeignKey object is created. Both parameters must be non-null.
	 * Also, if at least one of the tables doesn't exist then this method throws a IllegalArgumentException.
	 */
	public void addForeignKey(String sourceTableName, String targetTableName)
	{
		Table sourceTable = nameToTable.get(sourceTableName);
		Table targetTable = nameToTable.get(targetTableName);
		
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
			ForeignKey fk = new ForeignKey(sourceTable, targetTable);
			foreignKeysWithSourceTable.put(targetTableName, fk);
			addForeignKey(fk);
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

	public void addForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.add(foreignKey);
	}
	

	
	/**
	 * Returns the table object for the table with the given name. Returns null if no such table exists.
	 * @param name
	 * @return
	 */
	public Table getTableWithName(String name)
	{
		return nameToTable.get(name);
	}
	
	
	
	/**
	 * Returns an iterator over the tables of this schema.
	 */
	public Iterator<Table> getTableIterator()
	{
		return nameToTable.values().iterator();
	}
	
	/**
	 * Returns an iterator over the foreign keys of this schema.
	 */
	public Iterator<ForeignKey> getForeignKeyIterator()
	{
		
		return foreignKeys.iterator();
	}
}
