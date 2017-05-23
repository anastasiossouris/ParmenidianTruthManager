package foreignkeyshistory.visualization;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * The visualization information is tied to the coordinates of each table. Since a specific table may be represented
 * by different table objects in a SchemaHistory, what suffices to do is to keep a map from the table's name
 * to its coordinates.
 */
public class TableLayout {
	private Map<String, Point2D> coordinates;
	
	public TableLayout()
	{
		coordinates = new HashMap<>();
	}
	
	public void setCoordinates(String table, Point2D coord)
	{
		coordinates.put(table, coord);
	}
	
	public Point2D getCoordinates(String table)
	{
		return coordinates.get(table);
	}
}
