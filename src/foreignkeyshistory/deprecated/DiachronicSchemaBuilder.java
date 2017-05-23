package foreignkeyshistory.deprecated;

import java.util.Iterator;

import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.Table;

/**
 * This is a builder class that constructs the diachronic schema from a schema history.
 */
public class DiachronicSchemaBuilder {
	private SchemaHistory schemaHistory = null;
	
	public DiachronicSchemaBuilder(SchemaHistory schemaHistory)
	{
		assert schemaHistory != null;
		
		this.schemaHistory = schemaHistory;
	}
	
	public Schema constructDiachronicSchema()
	{
		SchemaBuilder diachronicSchemaBuilder = new SchemaBuilder();
		
		unionOfSchemas(diachronicSchemaBuilder);
		diachronicSchemaBuilder.setVersionName("Diachronic Schema");
		
		return diachronicSchemaBuilder.buildSchema();
	}
	
	private void unionOfSchemas(SchemaBuilder diachronicSchemaBuilder) {
		Iterator<Schema> schemaIterator = schemaHistory.getSchemaVersionsIterator();
		
		while (schemaIterator.hasNext())
		{
			Schema schema = schemaIterator.next();
			
			unionOfTables(diachronicSchemaBuilder, schema);
			unionOfForeignKeys(diachronicSchemaBuilder, schema);
		}
	}

	private void unionOfTables(SchemaBuilder diachronicSchemaBuilder, Schema schema)
	{
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			
			diachronicSchemaBuilder.addTable(table.getName());
		}
	}
	
	private void unionOfForeignKeys(SchemaBuilder diachronicSchemaBuilder, Schema schema)
	{
		Iterator<ForeignKey> foreignKeyIterator = schema.getForeignKeyIterator();
		
		while (foreignKeyIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeyIterator.next();
			
			diachronicSchemaBuilder.addForeignKey(foreignKey.getSourceTable().getName(), foreignKey.getTargetTable().getName());
		}
	}
}
