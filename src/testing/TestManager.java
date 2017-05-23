package testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;

public class TestManager {

	public static void main(String[] args) throws Exception {
		//testVisualizationExport();
		testMetrics();
	}

	private static void testVisualizationExport() throws Exception
	{
		File datasetDirectory = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_hecate_history_loader\\datasets\\TikiWiki");
		File graphMLFile = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_hecate_history_loader\\datasets\\TikiWiki\\uber.graphml");
		File output = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_visualization_export");
		
		new VisualizationExportTester(datasetDirectory.toPath(), graphMLFile.toPath(), output.toPath());
	}
	
	private static void testHecateSchemaHistoryLoader() throws Exception
	{
		File datasetsDirectory = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_hecate_history_loader\\datasets");
		HecateSchemaHistoryLoaderTester tester = new HecateSchemaHistoryLoaderTester();
		
		boolean same = tester.areDatasetsEqual(datasetsDirectory.toPath());
		
		System.out.println(same);
	}
	
	private static void testMetrics() throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		File oldDatasetsDirectory = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_metric_reports\\old");
		File newDatasetsDirectory = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_metric_reports\\new\\ATLAS");
		File datasetDirectory = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_hecate_history_loader\\datasets\\ATLAS");
		
		new MetricsTestPreparator(newDatasetsDirectory.toPath(), datasetDirectory.toPath());
		
		System.out.println("Executing metrics test");
		File newDatasetsDirectory2 = new File("C:\\Users\\anast\\workspace\\ParmenidianTruth_Reengineered\\testing_metric_reports\\new");
		new MetricsTester(oldDatasetsDirectory.toPath(), newDatasetsDirectory2.toPath());
		System.out.println("Metrics test passed");
	}
}
