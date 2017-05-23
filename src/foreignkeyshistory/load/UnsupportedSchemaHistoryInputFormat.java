package foreignkeyshistory.load;

/**
 * This class is throwed by history loaders that cannot handle their input formats.
 */
public class UnsupportedSchemaHistoryInputFormat extends Exception{

	public UnsupportedSchemaHistoryInputFormat(String msg)
	{
		super(msg);
	}
}
