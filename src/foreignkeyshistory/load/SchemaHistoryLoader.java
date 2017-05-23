package foreignkeyshistory.load;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.load.InvalidSchemaHistoryException;
import foreignkeyshistory.load.UnsupportedSchemaHistoryInputFormat;

/**
 * This interface shall be implemented by any class that is capable of loading a SchemaHistory object
 * from a given input source represented as a Path object.
 * 
 */
public interface SchemaHistoryLoader {

	/**
	 * This method shall attempt to construct a SchemaHistory object from the given input sources represented as a Path objects.
	 * 
	 * @param input 								The input sources from where the schema history shall be loaded.
	 * @return										The constructed SchemaHistory object
	 * @throws IOException							The loader shall throw this exception if an input error occurs
	 * @throws UnsupportedSchemaHistoryInputFormat	The loader shall throw this exception if it cannot handle the given input source
	 * @throws InvalidSchemaHistoryException		The loader shall throw this exception if it encounters a schema that is ill-formed
	 */
	SchemaHistory loadSchemaHistory(Path [] input) throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException;

}
