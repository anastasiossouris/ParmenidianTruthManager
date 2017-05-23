package foreignkeyshistory.visualization.load;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.visualization.TableLayout;

public interface TableLayoutLoader {

	public TableLayout loadTableLayout(Path input) throws IOException;
}
