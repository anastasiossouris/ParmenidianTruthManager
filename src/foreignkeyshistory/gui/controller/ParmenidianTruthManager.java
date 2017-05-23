package foreignkeyshistory.gui.controller;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.DiachronicSchema;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.load.HecateSchemaHistoryLoader;
import foreignkeyshistory.load.SchemaHistoryLoader;
import foreignkeyshistory.metrics.JUNGSchemaMetricsCalculatorService;
import foreignkeyshistory.metrics.SchemaMetricsCalculatorService;
import foreignkeyshistory.metrics.output.CSVSchemaHistoryMetricsReportService;
import foreignkeyshistory.metrics.output.SchemaHistoryMetricsReportService;
import foreignkeyshistory.utils.HecateScript;
import foreignkeyshistory.utils.Metric_Enums;
import foreignkeyshistory.visualization.TableLayout;
import foreignkeyshistory.visualization.export.GraphMLTableLayoutExporter;
import foreignkeyshistory.visualization.export.ImageSchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.PowerPointSchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.SchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.TableLayoutExporter;
import foreignkeyshistory.visualization.export.VideoSchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.load.GraphMLTableLayoutLoader;
import foreignkeyshistory.visualization.load.TableLayoutLoader;

public class ParmenidianTruthManager {
	private SchemaHistory schemaHistory = null;
	private Schema diachronicSchema = null;
	private TableLayout tableLayout = null;
	private SchemaView schemaView = null;
	
	public ParmenidianTruthManager(){}
	
	public void clear(){
		
		//modelManager.clear();
	}
	
	public String getTargetFolder(){
		
		//return modelManager.getTargetFolder();
		return null;
	}
	
	public void stopConvergence(){
		
		schemaView.stop();
		
	}
	
	// outputName is the name of the file to create (including previous directories)
	public void saveVertexCoordinates(String outputName) throws IOException{
		if (outputName == null)
		{
			System.err.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		}
		
		updateTableLayout();
		
		//modelManager.saveVertexCoordinates(projectIni);
		TableLayoutExporter tableLayoutExporter = new GraphMLTableLayoutExporter();
		
		File outputFile = new File(outputName);
		tableLayoutExporter.exportTableLayout(tableLayout, diachronicSchema, outputFile.toPath());
	}
	
	public void updateIniFile(String projectIni, String graphMLFile) {
		try {
			
			
			BufferedReader reader = new BufferedReader(new FileReader(projectIni));
			String line;
			String restOfFile = "";
			while((line = reader.readLine()) != null )
			{
				
				if(line.contains("Project Name") || line.contains("sql@") ||line.contains("transition@")||line.contains("output@"))
					restOfFile+=line+"\n";
			    	
			}
			
			reader.close();
			
				
				PrintWriter writer;
				
				writer = new PrintWriter(new FileWriter(projectIni));
				writer.println(restOfFile);
				writer.println("graphml@"+ graphMLFile);
				
				writer.close();				
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateTableLayout()
	{
		tableLayout = schemaView.updateTableLayout();
	}
	
	public void setTransformingMode(){
		schemaView.setTransformingMode();
	}
	
	public void setPickingMode(){
		schemaView.setPickingMode();
	}
	
	public void visualize(VisualizationViewer< String, String> vv,String projectIni,String targetFolder,int edgeType) throws IOException {
		//modelManager.visualize(vv,projectIni, targetFolder, edgeType);
		/*SchemaHistoryVisualizationExportService exportService = new ImageSchemaHistoryVisualizationExportService();
		
		File imagesFile = new File(targetFolder);
		Path imagesDir = imagesFile.toPath();
		exportService.export(schemaHistory, diachronicSchema, tableLayout, edgeType, null, imagesDir, null);*/
		
		ImageSchemaHistoryVisualizationExport export = new ImageSchemaHistoryVisualizationExport();
		
		File imagesFile = new File(targetFolder);
		Path imagesDir = imagesFile.toPath();
		export.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, null, imagesDir);
	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		File sqlFile = new File(sql);
		File xmlFile = new File(xml);
		Path[] input = new Path[2];
		input[0] = sqlFile.toPath();
		input[1] = xmlFile.toPath();

		SchemaHistoryLoader schemaHistoryLoader = new HecateSchemaHistoryLoader();
		schemaHistory = schemaHistoryLoader.loadSchemaHistory(input);
		diachronicSchema = new DiachronicSchema(schemaHistory);
		
		if (graphml != null)
		{
			TableLayoutLoader tablleLayoutLoader = new GraphMLTableLayoutLoader();			
			File graphmlFile = new File(graphml);
			tableLayout = tablleLayoutLoader.loadTableLayout(graphmlFile.toPath());
		}
		
		schemaView = new SchemaView(diachronicSchema, tableLayout, edgeType);
		
		if (tableLayout == null)
		{
			tableLayout = schemaView.getTableLayout();
		}
		
		return schemaView.getViewer();
	}
	
	public void createTransitions(File selectedFile) throws Exception{
		HecateScript hecateScript = new HecateScript(selectedFile);
		hecateScript.createTransitions();
	}
			
	public void createPowerPointPresentation(ArrayList<String> FileNames,String targetFolder,String projectName, int edgeType) throws FileNotFoundException, IOException{
		File targetDirectory = new File(targetFolder);
		File pptxFile = new File(targetDirectory, projectName + ".pptx");
		
		SchemaHistoryVisualizationExport export = new PowerPointSchemaHistoryVisualizationExport();
		
		export.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, null, pptxFile.toPath());
	}
	
	// file is the directory where we will output the video
	public void createVideo(File file, int edgeType) throws IOException{		
		Path videoDir = file.toPath();

		SchemaHistoryVisualizationExport export = new VideoSchemaHistoryVisualizationExport();
		export.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, null, videoDir);
	}

	public Component refresh(double forceMult, int repulsionRange) {
		//return modelManager.refresh(forceMult,repulsionRange);
		return null;
	}

	public void calculateMetrics(String targetFolder,ArrayList<Metric_Enums> metrics) throws IOException {
		SchemaMetricsCalculatorService schemaMetricsCalculatorService = new JUNGSchemaMetricsCalculatorService();
		SchemaHistoryMetricsReportService metricsReportService = new CSVSchemaHistoryMetricsReportService(schemaHistory, diachronicSchema, schemaMetricsCalculatorService);

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

		File newDatasetFile = new File(targetFolder);
		Path newDatasetDirectory = newDatasetFile.toPath();
		
		for(int i=0;i<metrics.size();i++)
		{
			if(metrics.get(i)==Metric_Enums.VERTEX_IN_DEGREE)
			{
				File reportOfVertexInDegreeFile = new File(newDatasetFile, reportOfVertexInDegreeName);
				if (!reportOfVertexInDegreeFile.exists())
				{
					reportOfVertexInDegreeFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfVertexInDegreeFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableInDegreeReport(reportOfVertexInDegreeFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.VERTEX_OUT_DEGREE)
			{
				File reportOfVertexOutDegreeFile = new File(newDatasetFile, reportOfVertexOutDegreeName);
				if (!reportOfVertexOutDegreeFile.exists())
				{
					reportOfVertexOutDegreeFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfVertexOutDegreeFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableOutDegreeReport(reportOfVertexOutDegreeFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.VERTEX_DEGREE)
			{
				File reportOfVertexDegreeFile = new File(newDatasetFile, reportOfVertexDegreeName);
				if (!reportOfVertexDegreeFile.exists())
				{
					reportOfVertexDegreeFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfVertexDegreeFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableDegreeReport(reportOfVertexDegreeFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.VERTEX_BETWEENNESS)
			{
				File reportOfVertexBetweenessFile = new File(newDatasetFile, reportOfVertexBetweenessName);
				if (!reportOfVertexBetweenessFile.exists())
				{
					reportOfVertexBetweenessFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfVertexBetweenessFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableBetweennessReport(reportOfVertexBetweenessFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.CLUSTERING_COEFFICIENT)
			{
				File reportOfClusterringCoefficientFile = new File(newDatasetFile, reportOfClusterringCoefficientName);
				if (!reportOfClusterringCoefficientFile.exists())
				{
					reportOfClusterringCoefficientFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfClusterringCoefficientFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeClusteringCoefficientReport(reportOfClusterringCoefficientFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.EDGE_BETWEENNESS)
			{
				File reportOfEdgeBetweenessFile = new File(newDatasetFile, reportOfEdgeBetweenessName);
				if (!reportOfEdgeBetweenessFile.exists())
				{
					reportOfEdgeBetweenessFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfEdgeBetweenessFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeForeignKeyBetweennessReport(reportOfEdgeBetweenessFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.GRAPH_DIAMETER)
			{
				File reportOfGraphDiameterFile = new File(newDatasetFile, reportOfGraphDiameterName);
				if (!reportOfGraphDiameterFile.exists())
				{
					reportOfGraphDiameterFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfGraphDiameterFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeSchemaDiameterReport(reportOfGraphDiameterFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_VERTICES)
			{
				File reportOfGraphVertexCountFile = new File(newDatasetFile, reportOfGraphVertexCountName);
				if (!reportOfGraphVertexCountFile.exists())
				{
					reportOfGraphVertexCountFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfGraphVertexCountFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableCountReport(reportOfGraphVertexCountFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_EDGES)
			{
				File reportOfGraphEdgeCountFile = new File(newDatasetFile, reportOfGraphEdgeCountName);
				if (!reportOfGraphEdgeCountFile.exists())
				{
					reportOfGraphEdgeCountFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfGraphEdgeCountFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeForeignKeyCountReport(reportOfGraphEdgeCountFile.toPath());
			}
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_CONNECTED_COMPONENTS)
			{
				File reportOfGraphConnectedComponentsFile = new File(newDatasetFile, reportOfGraphConnectedComponentsName);
				if (!reportOfGraphConnectedComponentsFile.exists())
				{
					reportOfGraphConnectedComponentsFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfGraphConnectedComponentsFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeConnectedComponentsCountReport(reportOfGraphConnectedComponentsFile.toPath());
			}
			else if(metrics.get(i)==Metric_Enums.NUMBER_OF_VERTICES_IN_GCC)
			{
				File reportOfVertexCountInGccFile = new File(newDatasetFile, reportOfVertexCountInGccName);
				if (!reportOfVertexCountInGccFile.exists())
				{
					reportOfVertexCountInGccFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfVertexCountInGccFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeTableCountReportForGcc(reportOfVertexCountInGccFile.toPath());
			}
			else if(metrics.get(i)==Metric_Enums.NUMBER_OF_EDGES_IN_GCC)
			{
				File reportOfGraphEdgeCountInGccFile = new File(newDatasetFile, reportOfGraphEdgeCountInGccName);
				if (!reportOfGraphEdgeCountInGccFile.exists())
				{
					reportOfGraphEdgeCountInGccFile.createNewFile();
				}
				else
				{
					FileChannel.open(reportOfGraphEdgeCountInGccFile.toPath()).truncate(0).close();
				}
				metricsReportService.makeForeignKeyCountReportForGcc(reportOfGraphEdgeCountInGccFile.toPath());
			}
		}
	}
}
