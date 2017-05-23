package testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.DiachronicSchema;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.deprecated.DiachronicSchemaBuilder;
import foreignkeyshistory.deprecated.HecateSchemaHistoryLoadService;
import foreignkeyshistory.deprecated.SchemaHistoryLoadService;
import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;
import foreignkeyshistory.metrics.JUNGSchemaMetricsCalculatorService;
import foreignkeyshistory.metrics.SchemaMetricsCalculatorService;
import foreignkeyshistory.metrics.output.CSVSchemaHistoryMetricsReportService;
import foreignkeyshistory.metrics.output.SchemaHistoryMetricsReportService;

public class MetricsTestPreparator {
	
	public MetricsTestPreparator(Path newDatasetDirectory, Path datasetDirectory) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		// Load the history
		System.out.println("Loading the history");
		SchemaHistoryLoadService schemaHistoryLoadService = new HecateSchemaHistoryLoadService();
		Path[] input = new Path[2];
		File transitionsFile = new File(datasetDirectory.toFile(), "transitions.xml");
		input[0] = datasetDirectory;
		input[1] = transitionsFile.toPath();
		SchemaHistory schemaHistory = schemaHistoryLoadService.load(input);
		System.out.println("Done!");
		
		// Export the metrics
		System.out.println("Clearing directory");
		clearDirectory(newDatasetDirectory);
		
		String reportOfClusterringCoefficientName = "Report of clustering coefficient.csv";
		String reportOfEdgeBetweenessName = "Report of edge betweenness.csv";
		String reportOfGraphDiameterName = "Report of graph diameter.csv";
		String reportOfGraphEdgeCountInGccName = "Report of graph edgeCount in gcc.csv";
		String reportOfGraphEdgeCountName = "Report of graph edgeCount.csv";
		String reportOfGraphVertexCountName = "Report of graph vertexCount.csv";
		String reportOfGraphConnectedComponentsName = "Report of graph's connected-components.csv";
		String reportOfVertexBetweenessName = "Report of vertex betweenness.csv";
		String reportOfVertexDegreeName = "Report of vertex degree.csv";
		String reportOfVertexInDegreeName = "Report of vertex inDegree.csv";
		String reportOfVertexOutDegreeName = "Report of vertex outDegree.csv";
		String reportOfVertexCountInGccName = "Report of vertexCount in gcc.csv";
		
		File newDatasetFile = newDatasetDirectory.toFile();
		
		System.out.println("Creating files");
		File reportOfClusterringCoefficientFile = new File(newDatasetFile, reportOfClusterringCoefficientName);
		File reportOfEdgeBetweenessFile = new File(newDatasetFile, reportOfEdgeBetweenessName);
		File reportOfGraphDiameterFile = new File(newDatasetFile, reportOfGraphDiameterName);
		File reportOfGraphEdgeCountInGccFile = new File(newDatasetFile, reportOfGraphEdgeCountInGccName);
		File reportOfGraphEdgeCountFile = new File(newDatasetFile, reportOfGraphEdgeCountName);
		File reportOfGraphVertexCountFile = new File(newDatasetFile, reportOfGraphVertexCountName);
		File reportOfGraphConnectedComponentsFile = new File(newDatasetFile, reportOfGraphConnectedComponentsName);
		File reportOfVertexBetweenessFile = new File(newDatasetFile, reportOfVertexBetweenessName);
		File reportOfVertexDegreeFile = new File(newDatasetFile, reportOfVertexDegreeName);
		File reportOfVertexInDegreeFile = new File(newDatasetFile, reportOfVertexInDegreeName);
		File reportOfVertexOutDegreeFile = new File(newDatasetFile, reportOfVertexOutDegreeName);
		File reportOfVertexCountInGccFile = new File(newDatasetFile, reportOfVertexCountInGccName);
		
		reportOfClusterringCoefficientFile.createNewFile();
		reportOfEdgeBetweenessFile.createNewFile();
		reportOfGraphDiameterFile.createNewFile();
		reportOfGraphEdgeCountInGccFile.createNewFile();
		reportOfGraphEdgeCountFile.createNewFile();
		reportOfGraphVertexCountFile.createNewFile();
		reportOfGraphConnectedComponentsFile.createNewFile();
		reportOfVertexBetweenessFile.createNewFile();
		reportOfVertexDegreeFile.createNewFile();
		reportOfVertexInDegreeFile.createNewFile();
		reportOfVertexOutDegreeFile.createNewFile();
		reportOfVertexCountInGccFile.createNewFile();
		
		SchemaMetricsCalculatorService schemaMetricsCalculatorService = new JUNGSchemaMetricsCalculatorService();
		Schema diachronicSchema = new DiachronicSchema(schemaHistory);
		SchemaHistoryMetricsReportService metricsReportService = new CSVSchemaHistoryMetricsReportService(schemaHistory, diachronicSchema, schemaMetricsCalculatorService);

		System.out.println("Writing report");
		metricsReportService.makeClusteringCoefficientReport(reportOfClusterringCoefficientFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeConnectedComponentsCountReport(reportOfGraphConnectedComponentsFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeForeignKeyBetweennessReport(reportOfEdgeBetweenessFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeForeignKeyCountReport(reportOfGraphEdgeCountFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeForeignKeyCountReportForGcc(reportOfGraphEdgeCountInGccFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeSchemaDiameterReport(reportOfGraphDiameterFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableBetweennessReport(reportOfVertexBetweenessFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableCountReport(reportOfGraphVertexCountFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableCountReportForGcc(reportOfVertexCountInGccFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableDegreeReport(reportOfVertexDegreeFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableInDegreeReport(reportOfVertexInDegreeFile.toPath());
		System.out.println("Writing report");
		metricsReportService.makeTableOutDegreeReport(reportOfVertexOutDegreeFile.toPath());
		
		System.out.println("Done");
	}

	private void clearDirectory(Path newDatasetDirectory) {
		File [] files = newDatasetDirectory.toFile().listFiles();
		
		for (int i = 0; i < files.length; ++i)
		{
			files[i].delete();
		}
	}
}
