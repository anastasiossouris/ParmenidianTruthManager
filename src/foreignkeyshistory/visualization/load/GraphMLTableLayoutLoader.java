package foreignkeyshistory.visualization.load;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;
import foreignkeyshistory.visualization.TableLayout;

public class GraphMLTableLayoutLoader implements TableLayoutLoader{
	TableLayout tableLayout = null;
	
	@Override
	public TableLayout loadTableLayout(Path input) throws IOException {
		tableLayout = new TableLayout();
		String filename = input.toString();
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));
		
		/* Create the Graph Transformer */
		Transformer<GraphMetadata, Graph<String, String>>
		graphTransformer = new Transformer<GraphMetadata,
		                          Graph<String, String>>() {
		 
		  public Graph<String, String>
		      transform(GraphMetadata metadata) {
		        metadata.getEdgeDefault();
				if (metadata.getEdgeDefault().equals(
		        EdgeDefault.DIRECTED)) {
		            return new
		            DirectedSparseGraph<String, String>();
		        } else {
		            return new
		            UndirectedSparseGraph<String, String>();
		        }
		      }
		};
		
		Transformer<NodeMetadata, String> vertexTransformer
		= new Transformer<NodeMetadata, String>() {
		    public String transform(NodeMetadata metadata) {
		        String v = "";//  new Table(metadata.getDescription());
		        //s(metadata.getId());
		        //s(metadata.getProperty("x"));
		        //s(metadata.getProperty("y"));
		        		        
		        tableLayout.setCoordinates(metadata.getId(), new Point2D.Double(Double.valueOf(metadata.getProperty("x")),Double.valueOf(metadata.getProperty("y"))));
		        
		        return v;
		    }
		};

		/* Create the Edge Transformer */
		 Transformer<EdgeMetadata, String> edgeTransformer =
		 new Transformer<EdgeMetadata, String>() {
		     public String transform(EdgeMetadata metadata) {
		    	 String e = "";	 
		         return e;
		     }
		 };
		 
		 /* Create the Hyperedge Transformer */
		 Transformer<HyperEdgeMetadata, String> hyperEdgeTransformer
		 = new Transformer<HyperEdgeMetadata, String>() {
		      public String transform(HyperEdgeMetadata metadata) {
		          String e= "";
		          return e;
		      }
		 };

		 /* Create the graphMLReader2 */
		 GraphMLReader2<Graph<String, String>, String, String>
		 graphReader = new
		 GraphMLReader2<Graph<String, String>, String, String>
		       (fileReader, graphTransformer, vertexTransformer,
		        edgeTransformer, hyperEdgeTransformer);
		 
		 try {
			    /* Get the new graph object from the GraphML file */
			    Graph g = graphReader.readGraph();
			} catch (GraphIOException ex) {}

		return tableLayout;
	}

}
