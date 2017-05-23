package foreignkeyshistory.deprecated;

import java.io.IOException;
import java.nio.file.Path;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.visualization.TableLayout;
import foreignkeyshistory.visualization.export.SchemaHistoryVisualizationExport;

public abstract class SchemaHistoryVisualizationExportService {
	
	public final void export(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output, Path previousPath) throws IOException
	{
		SchemaHistoryVisualizationExport exporter = getSchemaHistoryVisualizationExport(previousPath);
		
		exporter.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, output);
	}
	
	protected abstract SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport(Path previousPath);
}
