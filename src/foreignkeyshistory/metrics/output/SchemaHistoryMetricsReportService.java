package foreignkeyshistory.metrics.output;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.metrics.SchemaMetricsCalculator;
import foreignkeyshistory.metrics.SchemaMetricsCalculatorService;

public abstract class SchemaHistoryMetricsReportService {	
	protected SchemaHistory schemaHistory;
	protected Schema diachronicSchema;
		
	// For each schema (both the versions as well as the diachronic schema) we need to access a SchemaMetricsCalculator object for that schema
	private Map<Schema, SchemaMetricsCalculator> schemaMetricsCalculators = new HashMap<>();
	
	
	
	public SchemaHistoryMetricsReportService(SchemaHistory schemaHistory, Schema diachronicSchema, SchemaMetricsCalculatorService schemaMetricsCalculatorService)
	{
		assert schemaHistory != null;
		assert diachronicSchema != null;
		assert schemaMetricsCalculatorService != null;
		
		this.schemaHistory = schemaHistory;
		this.diachronicSchema = diachronicSchema;
		
		findSchemaMetricsCalculators(schemaMetricsCalculatorService);
	}
		
	// Change the interface instead of taking a Path object to take a WritableByteChannel
	public abstract void makeClusteringCoefficientReport(Path output) throws IOException;
	public abstract void makeConnectedComponentsCountReport(Path output) throws IOException;
	public abstract void makeForeignKeyCountReport(Path output) throws IOException;
	public abstract void makeForeignKeyCountReportForGcc(Path output) throws IOException;
	public abstract void makeTableCountReport(Path output) throws IOException;
	public abstract void makeTableCountReportForGcc(Path output) throws IOException;
	public abstract void makeTableOutDegreeReport(Path output) throws IOException;
	public abstract void makeTableInDegreeReport(Path output) throws IOException;
	public abstract void makeSchemaDiameterReport(Path output) throws IOException;
	public abstract void makeForeignKeyBetweennessReport(Path output) throws IOException;
	public abstract void makeTableBetweennessReport(Path output) throws IOException;
	public abstract void makeTableDegreeReport(Path output) throws IOException;	

	// Create a SchemaMetricsCalculator object for each schema
	private void findSchemaMetricsCalculators(SchemaMetricsCalculatorService schemaMetricsCalculatorService)
	{
		schemaMetricsCalculators.put(diachronicSchema, schemaMetricsCalculatorService.getSchemaMetricsCalculator(diachronicSchema));
		
		Iterator<Schema> schemaIterator = schemaHistory.getSchemaVersionsIterator();
		
		while (schemaIterator.hasNext())
		{
			Schema schema = schemaIterator.next();
			SchemaMetricsCalculator calc = schemaMetricsCalculatorService.getSchemaMetricsCalculator(schema);
			
			schemaMetricsCalculators.put(schema, calc);
		}
		
	}
	
	// Subclasses use this method to access a SchemaMetricsCalculator object for a specific schema
	protected SchemaMetricsCalculator getSchemaMetricsCalculator(Schema schema)
	{
		SchemaMetricsCalculator calc = schemaMetricsCalculators.get(schema);
		
		assert calc != null;
		
		return calc;
	}
}
