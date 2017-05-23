package foreignkeyshistory.metrics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.cluster.BicomponentClusterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.Table;

public class JUNGSchemaMetricsCalculator extends BicomponentClusterer implements SchemaMetricsCalculator{
	private Graph<String, String> graph = null;
	private Map<String, Table> vertexToTableMap = new HashMap<>();
	
	public JUNGSchemaMetricsCalculator(Schema schema) {
		super();
		
		graph = new DirectedSparseGraph<String, String>();
		addNodes(schema);
		addEdges(schema);
	}
	
	private void addNodes(Schema schema) {
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			String tableName = table.getName();
			
			graph.addVertex(tableName);
			vertexToTableMap.put(tableName, table);
		}
	}

	private void addEdges(Schema schema) {	
		Iterator<ForeignKey> foreignKeysIterator = schema.getForeignKeyIterator();
		
		while (foreignKeysIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeysIterator.next();
			
			graph.addEdge(getEdge(foreignKey), getVertex(foreignKey.getSourceTable()), getVertex(foreignKey.getTargetTable()));
		}
	}

	@Override
	public int getTableDegree(Table table) {
		DegreeScorer<String> ds = new DegreeScorer<>(graph);
		
		return ds.getVertexScore(getVertex(table));
	}

	@Override
	public double getTableBetweenness(Table table) {
		BetweennessCentrality<String, String> ranker = new BetweennessCentrality<>(graph);
		
		ranker.setRemoveRankScoresOnFinalize(false);
		ranker.evaluate();
		
		return ranker.getVertexRankScore(getVertex(table));
	}

	@Override
	public double getForeignKeyBetweenness(ForeignKey foreignKey) {
		BetweennessCentrality<String, String> ranker = new BetweennessCentrality<>(graph);
		
		ranker.setRemoveRankScoresOnFinalize(false);
		ranker.evaluate();

		return ranker.getEdgeRankScore(getEdge(foreignKey));
	}

	@Override
	public int getTableInDegree(Table table) {
		return graph.inDegree(getVertex(table));
	}

	@Override
	public int getTableOutDegree(Table table) {
		return graph.outDegree(getVertex(table));
	}

	@Override
	public int getTableCount() {
		return graph.getVertexCount();
	}

	@Override
	public int getTableCountForGcc() {
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		return giantConnectedComponent.getVertexCount();
	}

	@Override
	public int getForeignKeyCount() {
		return graph.getEdgeCount();
	}

	@Override
	public int getForeignKeyCountForGcc() {
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		return giantConnectedComponent.getEdgeCount();
	}

	@Override
	public double getSchemaDiameter() {
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		DirectionTransformer directionTransformer = new DirectionTransformer();
		Factory graphFactoryUndirected = UndirectedSparseGraph.getFactory();
        Factory edgeFactoryUndirected = new Factory<Integer>() {
    		Integer edgeCountUndirected=0;

        	public Integer create() { 
				return edgeCountUndirected++; 
        	} 
        }; 
		
		return ds.diameter(directionTransformer.toUndirected(giantConnectedComponent,graphFactoryUndirected,edgeFactoryUndirected,true));
	}

	@Override
	public int getNumberOfConnectedComponents() {
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		int numberOfConnectedComponents=0;

		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>1)
				numberOfConnectedComponents++;			
		}
		
		return numberOfConnectedComponents;
	}

	@Override
	public Map<Table, Double> getClusteringCoefficient() {
		Metrics metrics = new Metrics();
		
		Map<String, Double> collection = Metrics.clusteringCoefficients(graph);

		Map<Table,Double> m = new HashMap<>();
		
		for (Map.Entry<String, Double> entry : collection.entrySet())
		{
			m.put(getTable(entry.getKey()), entry.getValue());
		}
		
		return m;
	}

	private Table getTable(String vertex){ return vertexToTableMap.get(vertex); }
	private String getVertex(Table table){ return table.getName(); }
	private String getEdge(ForeignKey foreignKey){ return foreignKey.getSourceTable().getName() + "|" + foreignKey.getTargetTable().getName(); }
}
