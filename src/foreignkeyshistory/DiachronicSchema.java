package foreignkeyshistory;

import java.util.Iterator;


public class DiachronicSchema extends Schema{
	public DiachronicSchema(SchemaHistory schemaHistory)
	{
		unionOfSchemas(schemaHistory);
		setVersionName("DiachronicSchema");
	}
	
	private void unionOfSchemas(SchemaHistory schemaHistory) {
		Iterator<Schema> schemaIterator = schemaHistory.getSchemaVersionsIterator();
		
		while (schemaIterator.hasNext())
		{
			Schema schema = schemaIterator.next();
			
			unionOfTables(schema);
			unionOfForeignKeys(schema);
		}
	}

	private void unionOfTables(Schema schema)
	{
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			
			addTable(table.getName());
		}
	}
	
	private void unionOfForeignKeys(Schema schema)
	{
		Iterator<ForeignKey> foreignKeyIterator = schema.getForeignKeyIterator();
		
		while (foreignKeyIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeyIterator.next();
			
			addForeignKey(foreignKey.getSourceTable().getName(), foreignKey.getTargetTable().getName());
		}
	}
}
