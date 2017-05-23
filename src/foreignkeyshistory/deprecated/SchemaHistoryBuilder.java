package foreignkeyshistory.deprecated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.SchemaTransition;
import foreignkeyshistory.SchemaTransitionChange;
import foreignkeyshistory.Table;

/**
 * This is a helper utility class to build a SchemaHistory object.
 * 
 * Care must be taken by the client of this class in the order that it calls the methods (the class doesn't enforce the correct order).
 * 
 * Step 1: Insert the schemas in order using the appendSchemaVersion() method.
 * Step 2: Insert the transitions (in any order) using the addSchemaTransition() method.
 */
public class SchemaHistoryBuilder {
	private SchemaHistory schemaHistory = null;
	
	private List<Schema> schemaVersions = null;
	private List<Set<SchemaTransition>> schemaTransitions = null;
	
	public SchemaHistoryBuilder()
	{
		schemaHistory = new SchemaHistory();
		schemaVersions = new ArrayList<>();
		schemaTransitions = new ArrayList<>();
	}
	
	/**
	 * Appends the input schema (which must be non-null) to the schema versions (that is, this is the last version).
	 */
	public void appendSchemaVersion(Schema schema)
	{
		assert schema != null;
		
		// If i already have a previous schema then there exists a transition from the previous schema to the new schema appended. Hence i create a new set for transitions
		if (schemaVersions.size() > 0 )
		{
			schemaTransitions.add(new HashSet<SchemaTransition>());
		}
		schemaVersions.add(schema);
	}
	
	/**
	 * Registers the transition type that affected the specific affectedTable from the oldVersion to the newVersion.
	 * 
	 * If the versions are out of bound (or if newVersion != oldVersion + 1) then an IndexOutOfBoundsException is thrown.
	 * The parameter affectedTable must be non-null.
	 * If the affectedTable doesn't exist for the specific transition type then an IllegalArgumentException is thrown.
	 */
	public void addSchemaTransition(int oldVersion, int newVersion, String affectedTableName, SchemaTransitionChange transitionType)
	{
		if (oldVersion < 0 || oldVersion >= schemaVersions.size() - 1 || newVersion != oldVersion + 1)
		{
			throw new IndexOutOfBoundsException("invalid version numbers");
		}
		assert affectedTableName != null;
		assert transitionType != null;

		Schema oldSchema = schemaVersions.get(oldVersion);
		Schema newSchema = schemaVersions.get(newVersion);
		Table affectedTable = null;

		switch (transitionType)
		{
		case TABLE_CREATED:
			affectedTable = newSchema.getTableWithName(affectedTableName);
			break;
		case TABLE_DELETED:
			affectedTable = oldSchema.getTableWithName(affectedTableName);
			break;
		case TABLE_UPDATED:
			affectedTable = newSchema.getTableWithName(affectedTableName);
			break;
		default:
			assert true;
		}
		
		if (affectedTable == null)
		{
			throw new IllegalArgumentException("affected table not present");
		}
		
		SchemaTransition transition = new SchemaTransition(oldSchema, newSchema, affectedTable, transitionType);
		
		// Add this transition as an oldVersion-th transition.
		schemaTransitions.get(oldVersion).add(transition);
	}	

	
	
	/**
	 * Returns the SchemaHistory object as constructed so far. If the SchemaHistory is in an invalid state, then 
	 * the method throws an IllegalStateException.
	 */
	public SchemaHistory constructSchemaHistory()
	{
		if (!isSchemaHistoryValid())
		{
			throw new IllegalStateException("SchemaHistory object not constructed properly");
		}
		
		
		schemaHistory.addSchemaVersions(schemaVersions);
		schemaHistory.addSchemaTransitions(schemaTransitions);
		
		return schemaHistory;
	}
	
	// XXX: not implemented yet
	private boolean isSchemaHistoryValid()
	{
		return true;
	}
}
