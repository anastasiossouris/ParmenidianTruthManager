package foreignkeyshistory.metrics;

import java.util.Map;

import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Table;

public interface SchemaMetricsCalculator {
	
	public int getTableDegree(Table table) throws IllegalArgumentException;
	public double getTableBetweenness(Table table) throws IllegalArgumentException;
	public double getForeignKeyBetweenness(ForeignKey foreignKey) throws IllegalArgumentException;
	public int getTableInDegree(Table table) throws IllegalArgumentException;
	public int getTableOutDegree(Table table) throws IllegalArgumentException;
	public int getTableCount();
	public int getTableCountForGcc();
	public int getForeignKeyCount();
	public int getForeignKeyCountForGcc();
	public double getSchemaDiameter();
	public int getNumberOfConnectedComponents();
	public Map<Table,Double> getClusteringCoefficient();
}
