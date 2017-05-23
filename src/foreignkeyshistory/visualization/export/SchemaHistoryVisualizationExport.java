package foreignkeyshistory.visualization.export;

import java.io.IOException;
import java.nio.file.Path;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.visualization.TableLayout;

public interface SchemaHistoryVisualizationExport {

	
	public void exportSchemaHistory(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output) throws IOException;
}
