package foreignkeyshistory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents a history of a relational schema as:
 * 		(1) A list of versions in chronological order (i.e from version 0 to the last version)
 * 		(2) A list of transitions between versions. If i have N versions, then there are N-1 transition objects.
 * 		from version 0 to version 1, from version 1 to version 2, ..., from version N-1 to version N.
 * 
 * Each Schema contains its own Table objects. That is, if a table appears in multiple versions, a different Table
 * object is constructed for each version. These Table objects have the same name attribute. 
 * Consequently, the name of a table is used to determine equality between tables in different versions.
 * 
 * About transitions:
 * 
 */
public class SchemaHistory {
	private List<Schema> schemaVersions = null;
	private List<Set<SchemaTransition>> schemaTransitions = null;
	
	public SchemaHistory()
	{
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

	public void addSchemaVersions(List<Schema> schemaVersions)
	{
		assert schemaVersions != null;
		
		this.schemaVersions = schemaVersions;
	}
	
	public void addSchemaTransitions(List<Set<SchemaTransition>> schemaTransitions)
	{
		assert schemaTransitions != null;
		
		this.schemaTransitions = schemaTransitions;
	}
	
	
	public int getNumberOfSchemaVersions()
	{
		return schemaVersions.size();
	}
	
	/**
	 * The schema versions range from 0 up to N-1, where N is the total number of schemas. 
	 * This method returns the schema with index = version.
	 */
	public Schema getSchemaVersion(int version)
	{
		if (version < 0 || version >= schemaVersions.size())
		{
			throw new IndexOutOfBoundsException("schema version out of range");
		}
		
		return schemaVersions.get(version);
	}
	
	/**
	 * Returns an iterator over the schema versions of this history.
	 */
	public Iterator<Schema> getSchemaVersionsIterator()
	{
		return schemaVersions.iterator();
	}
	
	/**
	 * Returns an iterator over the transitions of this history.
	 */
	public Iterator<Set<SchemaTransition>> getSchemaTransitionsIterator()
	{
		return schemaTransitions.iterator();
	}
}
