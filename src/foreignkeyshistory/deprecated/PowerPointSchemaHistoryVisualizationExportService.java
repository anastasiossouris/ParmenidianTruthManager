package foreignkeyshistory.deprecated;

import java.nio.file.Path;

import foreignkeyshistory.visualization.export.PowerPointSchemaHistoryVisualizationExport;
import foreignkeyshistory.visualization.export.SchemaHistoryVisualizationExport;

public class PowerPointSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport(Path previousPath) {
		PowerPointSchemaHistoryVisualizationExport export = new PowerPointSchemaHistoryVisualizationExport();
		
		export.setPreviousImagesPath(previousPath);
		
		return export;
	}

}
