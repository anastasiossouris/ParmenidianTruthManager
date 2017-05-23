package foreignkeyshistory.deprecated;

import foreignkeyshistory.visualization.load.GraphMLTableLayoutLoader;
import foreignkeyshistory.visualization.load.TableLayoutLoader;

public class GraphMLTableLayoutLoadService extends TableLayoutLoadService{

	@Override
	protected TableLayoutLoader getTableLayoutLoader() {
		return new GraphMLTableLayoutLoader();
	}

}
