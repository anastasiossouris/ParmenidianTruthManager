package foreignkeyshistory.gui.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Iterator;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import foreignkeyshistory.Schema;
import foreignkeyshistory.Table;
import foreignkeyshistory.utils.Utils;
import foreignkeyshistory.visualization.TableLayout;

public class SchemaView {
	private Schema schema = null;
	private TableLayout tableLayout = null;
	private  VisualizationViewer<String, String> visualizationViewer = null;
	private SpringLayout2<String, String> layout;
	private static DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
	private Dimension universalFrame;
	private Transformer edgeTypeTransformer;

	private int edgeType;
	
	/**
	 * 
	 * @param schemaPresenter
	 * @param tableLayout	The TableLayout that provides the coordinates for the tables, or null if the view should compute its own coordinates
	 */
	public SchemaView(Schema schema, TableLayout tableLayout, int edgeType)
	{
		this.schema = schema;
		this.tableLayout = tableLayout;
		this.edgeType = edgeType;
		
		createGUI();
	}

	public void stop()
	{
		layout.lock(true);
	}
	
	public Component getViewer()
	{
		return visualizationViewer;
	}
	
	public void show()
	{
		visualizationViewer.repaint();
	}
	
	public int getEdgeType()
	{
		return edgeType;
	}
	
	public TableLayout getTableLayout()
	{
		return tableLayout;
	}
	
	private void createGUI()
	{
		Graph<String, String> graph = Utils.getGraph(schema);
		int numNodes = graph.getVertexCount();
		
		edgeTypeTransformer = edgeType == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		layout= new SpringLayout2<String, String>(graph);
		universalFrame =new Dimension(numNodes*26, numNodes*26);
		layout.setSize(universalFrame);
		visualizationViewer = new VisualizationViewer<String, String>(layout);
		visualizationViewer.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));
		
		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		visualizationViewer.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(visualizationViewer.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		visualizationViewer.setBackground(Color.WHITE);
		visualizationViewer.getRenderContext().setEdgeShapeTransformer(edgeTypeTransformer);

		// ---------------default graph moving
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);

		if(tableLayout != null){			
			for (String vertex : graph.getVertices())
			{
				layout.setLocation(vertex, tableLayout.getCoordinates(vertex));
				layout.lock(vertex, true);
			}
		}
		else
		{
			tableLayout = new TableLayout();
			updateTableLayout();
		}
	}
	
	public TableLayout updateTableLayout() {
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			tableLayout.setCoordinates(table.getName(), new Point2D.Double(layout.getX(table.getName()),layout.getY(table.getName())));
		}
		
		return tableLayout;
	}

	public void setTransformingMode(){
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);	
	}
	
	public void setPickingMode(){
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		visualizationViewer.setGraphMouse(graphMouse);		
	}
}
