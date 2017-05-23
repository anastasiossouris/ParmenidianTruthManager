package foreignkeyshistory;

/**
 * This class represents a foreign key from a source table to a target table. 
 * A foreign key object is tied to a specific schema. That is, given a source Table object source_table, and a target
 * Table object target_table, a ForeignKey object relates these two objects.
 */
public class ForeignKey {
	private Table source_table = null;
	private Table target_table = null;

	public ForeignKey(Table source_table, Table target_table)
	{
		setSourceTable(source_table);
		setTargetTable(target_table);
	}
	
	public void setSourceTable(Table source_table)
	{
		assert source_table != null;
		
		this.source_table = source_table;
	}
	
	public void setTargetTable(Table target_table)
	{
		assert target_table != null;
		
		this.target_table = target_table;
	}
	
	public Table getSourceTable()
	{
		return source_table;
	}
	
	public Table getTargetTable()
	{
		return target_table;
	}
}
