package foreignkeyshistory.load;

/**
 * This class is throwed by history loaders when they encounter an invalid schema format during parsing.
 */
public class InvalidSchemaHistoryException extends Exception{
	public InvalidSchemaHistoryException(String msg)
	{
		super(msg);
	}
}
