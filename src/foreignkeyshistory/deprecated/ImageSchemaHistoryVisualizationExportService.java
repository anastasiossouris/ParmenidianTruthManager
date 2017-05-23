package foreignkeyshistory.deprecated;

import java.nio.file.Path;

import foreignkeyshistory.visualization.export.ImageSchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.SchemaHistoryVisualizationExport;

public class ImageSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport(Path previousPath) {
		return new ImageSchemaHistoryVisualizationExport();
	}

}
