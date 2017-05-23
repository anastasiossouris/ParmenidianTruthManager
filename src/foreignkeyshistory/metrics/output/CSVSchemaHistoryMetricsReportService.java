package foreignkeyshistory.metrics.output;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.Table;
import foreignkeyshistory.metrics.SchemaMetricsCalculatorService;

public class CSVSchemaHistoryMetricsReportService extends SchemaHistoryMetricsReportService{

	public CSVSchemaHistoryMetricsReportService(SchemaHistory schemaHistory, Schema diachronicSchema,
			SchemaMetricsCalculatorService schemaMetricsCalculatorService) {
		super(schemaHistory, diachronicSchema, schemaMetricsCalculatorService);
	}

	@Override
	public void makeClusteringCoefficientReport(Path output) throws IOException {	
		File reportFile = new File(output.toString());	
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = diachronicSchema.getNumberOfTables() + 1;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
	
		fillFirstLine(report);
		fillFistColumnWithTables(report);
		
		for(int i=1;i<columns;i++)
		{
			Schema schema = (i == 1) ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			Map<Table,Double> c = getSchemaMetricsCalculator(schema).getClusteringCoefficient();
			for(int j=1;j<lines;j++)
			{
				String tableName = getTableNameFromReport(report, j);
				Table table = schema.getTableWithName(tableName);
				String clusteringCoefficientString = "*";
				if (table != null)
				{
					Double clusteringCoefficient = c.get(table);
					clusteringCoefficientString = String.valueOf(clusteringCoefficient);
				}
				report[j][i] = clusteringCoefficientString + ",";
			}
		}
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeConnectedComponentsCountReport(Path output) throws IOException {	
		File reportFile = new File(output.toString());		
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
				
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="# of connected-components ,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			report[1][i] = getSchemaMetricsCalculator(schema).getNumberOfConnectedComponents() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeForeignKeyCountReport(Path output) throws IOException {
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
		
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="# of Edges ,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{			
			Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			report[1][i] = getSchemaMetricsCalculator(schema).getForeignKeyCount() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeForeignKeyCountReportForGcc(Path output) throws IOException {
		File reportFile = new File(output.toString());
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
				
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="# of Edges in gcc,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{	
			Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			report[1][i] = getSchemaMetricsCalculator(schema).getForeignKeyCountForGcc() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableCountReport(Path output) throws IOException {
		File reportFile = new File(output.toString());
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
				
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="# of Vertices in graph,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{	
			Schema schema = i == 1? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			report[1][i] = getSchemaMetricsCalculator(schema).getTableCount() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableCountReportForGcc(Path output) throws IOException {
		File reportFile = new File(output.toString());
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
				
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="# of Vertices in gcc,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{	
			Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
			report[1][i] = getSchemaMetricsCalculator(schema).getTableCountForGcc() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableOutDegreeReport(Path output) throws IOException {
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = diachronicSchema.getNumberOfTables() + 1;
		int columns =schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
		
		fillFirstLine(report);
		fillFistColumnWithTables(report);
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			for(int j=1;j<lines;j++)
			{				
				Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				Table table = schema.getTableWithName(getTableNameFromReport(report, j));
				String tableOutDegreeString = "*";
				if (table != null)
				{
					tableOutDegreeString = String.valueOf(getSchemaMetricsCalculator(schema).getTableOutDegree(table));
				}
				report[j][i] = tableOutDegreeString + ",";
			}
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableInDegreeReport(Path output) throws IOException {
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = diachronicSchema.getNumberOfTables() + 1;
		int columns =schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];

		fillFirstLine(report);		
		fillFistColumnWithTables(report);
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			for(int j=1;j<lines;j++)
			{				
				Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				Table table = schema.getTableWithName(getTableNameFromReport(report, j));
				String tableInDegreeString = "*";
				if (table != null)
				{
					tableInDegreeString = String.valueOf(getSchemaMetricsCalculator(schema).getTableInDegree(table));
				}
				report[j][i] = tableInDegreeString + ",";				
			}
		}

		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeSchemaDiameterReport(Path output) throws IOException {
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = 2;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];
		
		fillFirstLine(report);
		
//		create 1st column		
		report[1][0]="Graph Diameter,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{				
			Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				
			report[1][i] = getSchemaMetricsCalculator(schema).getSchemaDiameter() + ",";
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeForeignKeyBetweennessReport(Path output) throws IOException {
		File reportFile = new File(output.toString());
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = diachronicSchema.getNumberOfForeignKeys() + 1;
		int columns = schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];

		fillFirstLine(report);
		fillFirstColumnWithForeignKeys(report);
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			for(int j=1;j<lines;j++)
			{
				Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				Table source = schema.getTableWithName(getSourceFromStringForeignKey(getForeignKeyNameFromReport(report,j)));
				Table target = schema.getTableWithName(getTargetFromStringForeignKey(getForeignKeyNameFromReport(report,j)));
				ForeignKey foreignKey = schema.getForeignKey(source, target);
				String foreignKeyBetweennessString = "*";
				if (foreignKey != null)
				{
					foreignKeyBetweennessString = String.valueOf(getSchemaMetricsCalculator(schema).getForeignKeyBetweenness(foreignKey));
				}
				report[j][i] = foreignKeyBetweennessString + ",";
			}
		}
		
		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableBetweennessReport(Path output) throws IOException {
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = diachronicSchema.getNumberOfTables() + 1;
		int columns =schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];

		fillFirstLine(report);		
		fillFistColumnWithTables(report);
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			for(int j=1;j<lines;j++)
			{				
				Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				Table table = schema.getTableWithName(getTableNameFromReport(report, j));
				String tableBetweennessString = "*";
				if (table != null)
				{
					tableBetweennessString = String.valueOf(getSchemaMetricsCalculator(schema).getTableBetweenness(table));
				}
				report[j][i] = tableBetweennessString + ",";
			}
		}


		writeArray(report, lines, columns, writer);
	}

	@Override
	public void makeTableDegreeReport(Path output) throws IOException {		
		File vertexReport = new File(output.toString());
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = diachronicSchema.getNumberOfTables() + 1;
		int columns =schemaHistory.getNumberOfSchemaVersions() + 2;
		
		String[][] report= new String[lines][columns];

		fillFirstLine(report);		
		fillFistColumnWithTables(report);
		
//		fill in the rest
		for(int i=1;i<columns;i++)
		{
			for(int j=1;j<lines;j++)
			{
				Schema schema = i == 1 ? diachronicSchema : schemaHistory.getSchemaVersion(i-2);
				Table table = schema.getTableWithName(getTableNameFromReport(report, j));
				String tableDegreeString = "*";
				if (table != null)
				{
					tableDegreeString = String.valueOf(getSchemaMetricsCalculator(schema).getTableDegree(table));
				}
				report[j][i] = tableDegreeString + ",";
			}
		}


		writeArray(report, lines, columns, writer);
	}


	private void fillFirstColumnWithForeignKeys(String[][] report)
	{
		//		create 1st column		
		/*for(int i=0; i < diachronicSchema.getNumberOfForeignKeys(); i++)
		{
			ForeignKey foreignKey = diachronicSchema.getForeignKey(i);
			String foreignKeyString = getForeignKeyString(foreignKey);
			
			report[i+1][0] = foreignKeyString + ",";
		}*/
		
		int i = 0;
		Iterator<ForeignKey> foreignKeysIterator = diachronicSchema.getForeignKeyIterator();
		while (foreignKeysIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeysIterator.next();
			String foreignKeyString = getForeignKeyString(foreignKey);
			
			report[i+1][0] = foreignKeyString + ",";
			i = i + 1;
		}
	}
	

	private void fillFistColumnWithTables(String[][] report)
	{
		/*for (int i = 0; i < diachronicSchema.getNumberOfTables(); ++i)
		{
			report[i+1][0] = diachronicSchema.getTable(i).getName() + ",";
		}*/
		
		int i = 0;
		Iterator<Table> tableIterator = diachronicSchema.getTableIterator();
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			report[i+1][0] = table.getName() + ",";
			i = i + 1;
		}
	}

	private void fillFirstLine(String[][] report)
	{
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		

		for(int i=0; i < schemaHistory.getNumberOfSchemaVersions(); i++)			
		{
			report[0][i+2] = schemaHistory.getSchemaVersion(i).getVersionName() + ",";
		}
	}
	
	// Write the array with the metrics in csv format called report into the ouput using the given writer
	private void writeArray(String[][] report, int lines, int columns, PrintWriter writer)
	{
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
	}


	private String getForeignKeyString(ForeignKey foreignKey) {
		return foreignKey.getSourceTable().getName() + "|" + foreignKey.getTargetTable().getName();
	}

	
	private String getSourceFromStringForeignKey(String foreignKey)
	{
		int index = foreignKey.indexOf('|');
		return foreignKey.substring(0, index);
	}
	
	private String getTargetFromStringForeignKey(String foreignKey)
	{
		int index = foreignKey.indexOf('|');
		return foreignKey.substring(index+1);	
	}
	
	private String getTableNameFromReport(String [][] report, int i)
	{
		int index = report[i][0].indexOf(",");
		return report[i][0].substring(0, index);
	}
	
	private String getForeignKeyNameFromReport(String [][] report, int i)
	{
		int index = report[i][0].indexOf(",");
		return report[i][0].substring(0, index);
	}
}
