package foreignkeyshistory;

/**
 * Represents changes in consecutive schema versions. There is an old-version and a new-version. The changes are:
 * 				(1) A table is created in the new version (i.e it doesn't exist in the old version)
 * 				(2) A table is updated in the new version (i.e it may have an additional attribute)
 * 				(3) A table is removed from the new version (i.e it exists in the old-version and not in the new-version) 
 * 
 * A change has an affected table. The kind of changes appear in the SchemaTransitionChange enumeration.
 * If the type is TABLE_CREATED, the affected table is the Table object from the new version, the same is true
 * for the type TABLE_UPDATED.
 * If the type is TABLE_DELETED, the affected table is the Table object from the old version. 
 */
public class SchemaTransition {
	private Schema oldVersion;
	private Schema newVersion;
	private Table affectedTable;
	private SchemaTransitionChange transitionChange;
	
	public SchemaTransition(Schema oldVersion, Schema newVersion, Table affectedTable, SchemaTransitionChange transitionChange)
	{
		assert oldVersion != null;
		assert newVersion != null;
		assert affectedTable != null;
		assert transitionChange != null;
		
		this.oldVersion = oldVersion;
		this.newVersion = newVersion;
		this.affectedTable = affectedTable;
		this.transitionChange = transitionChange;
	}
	
	public Schema getOldSchemaVersion()
	{
		return oldVersion;
	}
	
	public Schema getNewSchemaVersion()
	{
		return newVersion;
	}
	
	public Table getAffectedTable()
	{
		return affectedTable;
	}
	
	public SchemaTransitionChange getSchemaTransitionChange()
	{
		return transitionChange;
	}
}
