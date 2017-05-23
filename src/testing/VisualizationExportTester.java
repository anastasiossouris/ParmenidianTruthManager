package testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.deprecated.DiachronicSchemaBuilder;
import foreignkeyshistory.deprecated.GraphMLTableLayoutLoadService;
import foreignkeyshistory.deprecated.HecateSchemaHistoryLoadService;
import foreignkeyshistory.deprecated.ImageSchemaHistoryVisualizationExportService;
import foreignkeyshistory.deprecated.PowerPointSchemaHistoryVisualizationExportService;
import foreignkeyshistory.deprecated.SchemaHistoryLoadService;
import foreignkeyshistory.deprecated.SchemaHistoryVisualizationExportService;
import foreignkeyshistory.deprecated.TableLayoutLoadService;
import foreignkeyshistory.deprecated.VideoSchemaHistoryVisualizationExportService;
import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;
import foreignkeyshistory.visualization.TableLayout;

public class VisualizationExportTester {
	SchemaHistory schemaHistory;
	TableLayout tableLayout;
	Path imagesDir;
	Path pptDir;
	Path videoDir;
	int edgeType = 0;
	Schema diachronicSchema;
			
	public VisualizationExportTester(Path datasetPath, Path graphMLPath, Path output) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{	
		loadSchemaHistory(datasetPath);
		System.out.println("Loading schema history done!");
		loadTableLayout(graphMLPath);
		System.out.println("Loading table layout done!");
		constructDiachronicSchema();
		makeDirs(output);
		
		System.out.println("Loading done!");
		
		//System.out.println("Exporting images");
		//exportImages();
		//System.out.println("Exporting power point");
		//exportPowerPoint();
		System.out.println("Exporting video");
		exportVideo();
	}
	
	private void constructDiachronicSchema() {
		DiachronicSchemaBuilder builder = new DiachronicSchemaBuilder(schemaHistory);
		
		diachronicSchema = builder.constructDiachronicSchema();
	}

	private void makeDirs(Path output)
	{
		File imagesDirFile = new File(output.toFile(), "images");
		imagesDirFile.mkdir();
		imagesDir = imagesDirFile.toPath();
		
		File pptDirFile = new File(output.toFile(), "ppt");
		pptDirFile.mkdir();
		pptDir = pptDirFile.toPath();
		
		File videoDirFile = new File(output.toFile(), "video");
		videoDirFile.mkdir();
		videoDir = videoDirFile.toPath();
	}
	
	private void loadSchemaHistory(Path datasetPath) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		SchemaHistoryLoadService schemaHistoryLoadService = new HecateSchemaHistoryLoadService();
		File transitionsFile = new File(datasetPath.toFile(), "transitions.xml");
		Path[] input = new Path[2];
		input[0] = datasetPath;
		input[1] = transitionsFile.toPath();

		schemaHistory = schemaHistoryLoadService.load(input);
	}
	
	private void loadTableLayout(Path graphMLPath) throws IOException
	{
		TableLayoutLoadService tableLayoutLoadService = new GraphMLTableLayoutLoadService();
		
		tableLayout = tableLayoutLoadService.loadTableLayout(graphMLPath);
	}
	
	private void exportImages() throws IOException
	{
		SchemaHistoryVisualizationExportService exportService = new ImageSchemaHistoryVisualizationExportService();
		
		exportService.export(schemaHistory, diachronicSchema, tableLayout, edgeType, null, imagesDir, null);
	}
		
	private void exportPowerPoint() throws IOException
	{
		SchemaHistoryVisualizationExportService exportService = new PowerPointSchemaHistoryVisualizationExportService();
		
		exportService.export(schemaHistory, diachronicSchema, tableLayout, edgeType, null, pptDir, imagesDir);
	}
	
	private void exportVideo() throws IOException
	{
		SchemaHistoryVisualizationExportService exportService = new VideoSchemaHistoryVisualizationExportService();
		
		exportService.export(schemaHistory, diachronicSchema, tableLayout, edgeType, null, videoDir, pptDir);
	}
}
