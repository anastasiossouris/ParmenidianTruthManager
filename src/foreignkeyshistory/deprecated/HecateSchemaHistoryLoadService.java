package foreignkeyshistory.deprecated;

import foreignkeyshistory.load.HecateSchemaHistoryLoader;
import foreignkeyshistory.load.SchemaHistoryLoader;

public class HecateSchemaHistoryLoadService extends SchemaHistoryLoadService{

	@Override
	protected SchemaHistoryLoader getSchemaHistoryLoader() {
		return new HecateSchemaHistoryLoader();
	}
}
