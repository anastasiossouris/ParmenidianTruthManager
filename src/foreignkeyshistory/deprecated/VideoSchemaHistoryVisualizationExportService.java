package foreignkeyshistory.deprecated;

import java.nio.file.Path;

import foreignkeyshistory.visualization.export.SchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.VideoSchemaHistoryVisualizationExport;

public class VideoSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport(Path previousPath) {
		VideoSchemaHistoryVisualizationExport export = new VideoSchemaHistoryVisualizationExport();
		
		export.setPreviousPPTPath(previousPath);
		
		return export;
	}

}
