package foreignkeyshistory.visualization.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLWriter;
import foreignkeyshistory.Schema;
import foreignkeyshistory.utils.Utils;
import foreignkeyshistory.visualization.TableLayout;

public class GraphMLTableLayoutExporter implements TableLayoutExporter{

	@Override
	public void exportTableLayout(TableLayout tableLayout, Schema schema, Path output) throws IOException {
		Graph<String, String> graph = Utils.getGraph(schema);
		
		GraphMLWriter<String,String> graphWriter = new GraphMLWriter<String, String> ();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output.toString())));

		final TableLayout layout = tableLayout;
		
		graphWriter.addVertexData("x", null, "0",
			    new Transformer<String, String>() {
			        public String transform(String v) {
			        		return Double.toString(layout.getCoordinates(v).getX());
			        	}		        	
			        }		    
			);
		
		
		graphWriter.addVertexData("y", null, "0",
			    new Transformer<String, String>() {
			        public String transform(String v) {
			        	return Double.toString(layout.getCoordinates(v).getY());			        		
			        	}			        	
			        }		    
			);

		graphWriter.save(graph, out);
	}

}
