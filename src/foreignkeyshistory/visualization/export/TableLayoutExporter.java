package foreignkeyshistory.visualization.export;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.Schema;
import foreignkeyshistory.visualization.TableLayout;

public interface TableLayoutExporter {
	
	// output is the name of the output file
	public void exportTableLayout(TableLayout tableLayout, Schema schema, Path output) throws IOException;
}
