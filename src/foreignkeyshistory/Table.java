package foreignkeyshistory;

/**
 * This class represents a table (relation) in a relational schema. For the purposes of the history of foreign keys
 * the only information required for the table is its name. 
 */
public class Table {
	private String name = null;
	
	public Table(String name)
	{
		setName(name);
	}
	
	public void setName(String name)
	{
		assert name != null;
		
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
