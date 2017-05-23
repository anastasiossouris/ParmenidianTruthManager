package foreignkeyshistory.deprecated;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.SchemaHistoryLoader;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;

/**
 * A service that can load a SchemaHistory object from a given input source represented by a Path object.
 */
public abstract class SchemaHistoryLoadService {
	
	/**
	 * Consult the SchemaHistoryLoader interface.
	 */
	public final SchemaHistory load(Path [] input) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException
	{
		SchemaHistoryLoader loader = getSchemaHistoryLoader();
		
		return loader.loadSchemaHistory(input);
	}

	/**
	 * Implementations of this service shall implement this method in order to provide the appropriate SchemaHistoryLoader object.
	 */
	protected abstract SchemaHistoryLoader getSchemaHistoryLoader();
}
