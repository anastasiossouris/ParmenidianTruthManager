package foreignkeyshistory.utils;

import java.util.Iterator;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.Table;

public class Utils {
	
	public static Graph<String, String> getGraph(Schema schema)
	{
		Graph<String, String> graph = new DirectedSparseGraph<String, String>();
		
		addNodes(schema, graph);
		addEdges(schema, graph);
		
		return graph;
	}
	
	private static void addNodes(Schema schema, Graph<String, String> graph) {
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			String tableName = table.getName();
			
			graph.addVertex(tableName);
		}
	}

	private static void addEdges(Schema schema, Graph<String, String> graph) {	
		Iterator<ForeignKey> foreignKeysIterator = schema.getForeignKeyIterator();
		
		while (foreignKeysIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeysIterator.next();
			
			graph.addEdge(getEdge(foreignKey), getVertex(foreignKey.getSourceTable()), getVertex(foreignKey.getTargetTable()));
		}
	}
	
	public static String getVertex(Table table){ return table.getName(); }
	public static String getEdge(ForeignKey foreignKey){ return foreignKey.getSourceTable().getName() + "|" + foreignKey.getTargetTable().getName(); }
}
