package foreignkeyshistory.deprecated;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.visualization.TableLayout;
import foreignkeyshistory.visualization.load.TableLayoutLoader;

public abstract class TableLayoutLoadService {

	public TableLayout loadTableLayout(Path input) throws IOException
	{
		TableLayoutLoader loader = getTableLayoutLoader();
		
		return loader.loadTableLayout(input);
	}
	
	protected abstract TableLayoutLoader getTableLayoutLoader();
}
