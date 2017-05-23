package foreignkeyshistory.visualization.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import foreignkeyshistory.ForeignKey;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.SchemaTransition;
import foreignkeyshistory.SchemaTransitionChange;
import foreignkeyshistory.Table;
import foreignkeyshistory.visualization.TableLayout;

public class ImageSchemaHistoryVisualizationExport implements SchemaHistoryVisualizationExport{
	private SchemaHistory schemaHistory;
	private Schema diachronicSchema;
	private TableLayout tableLayout;
	private int edgeType;
	private Path output;
	
	private Map<Schema, Map<String,SchemaTransitionChange>> tableStatus = new HashMap<>();
	
	class SchemaVisualizer implements Runnable
	{
		private Schema schema;
		private Path output;
		
		public SchemaVisualizer(Schema schema)
		{
			this.schema = schema;
		}
		
		@Override
		public void run() {
			visualizeSchema(schema);
		}
		
	}
	
	public void exportSchemaHistory(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output) throws IOException {		
		this.schemaHistory = schemaHistory;
		this.diachronicSchema = diachronicSchema;
		this.tableLayout = tableLayout;
		this.edgeType = edgeType;
		this.output = output;
		
		fillTableStatus();
		visualizeIndividualSchemas();
		visualizeDiachronicSchema();
	}

	private void visualizeIndividualSchemas()
	{
		Dimension universalFrame =new Dimension(diachronicSchema.getNumberOfTables()*26, diachronicSchema.getNumberOfTables()*26);
		Dimension d = new Dimension(universalFrame.width+300, universalFrame.height+300);
		
		int width = d.width;
		int height = d.height;
		
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		Iterator<Schema> schemaIterator = schemaHistory.getSchemaVersionsIterator();
		
		try{
			while (schemaIterator.hasNext())
			{
				Schema schema = schemaIterator.next();	
				SchemaVisualizer schemaVisualizer = new SchemaVisualizer(schema);
				schemaVisualizer.run();
				//exec.submit(schemaVisualizer);
			}
		} finally {
			exec.shutdown();
		}
	}
	
	private void visualizeDiachronicSchema()
	{
		Graph<String,String> graph = getGraph(diachronicSchema);
		Transformer edgeTypeTransformer = edgeType == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();;
		Layout<String, String> layout = new SpringLayout2<String, String>(graph);
		Dimension universalFrame =new Dimension(diachronicSchema.getNumberOfTables()*26, diachronicSchema.getNumberOfTables()*26);
		layout.setSize(universalFrame);

		VisualizationViewer< String, String> vv = new VisualizationViewer<String, String>(layout);
		vv.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));

		//vv.setGraphLayout(layout);

		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeTypeTransformer);
		
		
		
		File file =new File(output.toString() + "/"+	"Diachronic Graph"  + ".jpg");
		
		int width = vv.getWidth();
		int height = vv.getHeight();
		

		BufferedImage bi = new BufferedImage(width, height,	BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void visualizeSchema(Schema schema)
	{
		final Schema currentSchema = schema;
		Graph<String,String> graph = getGraph(schema);
		Transformer edgeTypeTransformer = edgeType == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();;
		Layout<String, String> layout = new StaticLayout<String, String>(graph);
		Dimension universalFrame =new Dimension(diachronicSchema.getNumberOfTables()*26, diachronicSchema.getNumberOfTables()*26);
		layout.setSize(universalFrame);
		
		VisualizationViewer< String, String> vv = new VisualizationViewer<String, String>(layout);
		vv.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));

		//vv.setGraphLayout(layout);
		
		// set the location for each vertex
		Iterator<Table> tableIterator = schema.getTableIterator();

		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String v) {
				Map<String,SchemaTransitionChange> tableChanges = tableStatus.get(currentSchema);
				SchemaTransitionChange change = tableChanges.get(v);
				
				if (change != null)
				{
					switch(change)
					{
					case TABLE_CREATED:
						return new Color(0, 255, 0, 200);
					case TABLE_DELETED:
						return new Color(255, 0, 0, 200);
					case TABLE_UPDATED:
						return new Color(255, 255, 0, 200);
					}
				}
				
				return Color.BLACK;
			}
		};

		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			
			layout.setLocation(table.getName(), tableLayout.getCoordinates(table.getName()));
			layout.lock(table.getName(), true);
		}
		
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		vv.getRenderContext().setEdgeShapeTransformer(edgeTypeTransformer);
		vv.setBackground(Color.WHITE);

		writeJPEGImage(vv,new File(output.toString() + "/"+ schema.getVersionName() + ".jpg"));
	}

	private Graph<String, String> getGraph(Schema schema)
	{
		Graph<String, String> graph = new DirectedSparseGraph<String, String>();
		
		
		addNodes(schema, graph);
		addEdges(schema, graph);
		
		return graph;
	}
	
	private void addNodes(Schema schema, Graph<String, String> graph) {
		Iterator<Table> tableIterator = schema.getTableIterator();
		
		while (tableIterator.hasNext())
		{
			Table table = tableIterator.next();
			String tableName = table.getName();
			
			graph.addVertex(tableName);
		}
	}

	private void addEdges(Schema schema, Graph<String, String> graph) {	
		Iterator<ForeignKey> foreignKeysIterator = schema.getForeignKeyIterator();
		
		while (foreignKeysIterator.hasNext())
		{
			ForeignKey foreignKey = foreignKeysIterator.next();
			
			graph.addEdge(getEdge(foreignKey), getVertex(foreignKey.getSourceTable()), getVertex(foreignKey.getTargetTable()));
		}
	}

	private String getVertex(Table table){ return table.getName(); }
	private String getEdge(ForeignKey foreignKey){ return foreignKey.getSourceTable().getName() + "|" + foreignKey.getTargetTable().getName(); }
	
	protected void writeJPEGImage(VisualizationViewer< String, String> vv,File file) {
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		
		vv.paint(graphics);
		graphics.dispose();
		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fillTableStatus()
	{
		Iterator<Schema> schemaIterator = schemaHistory.getSchemaVersionsIterator();
		
		while (schemaIterator.hasNext())
		{
			Schema schema = schemaIterator.next();
			
			tableStatus.put(schema, new HashMap<String,SchemaTransitionChange>());
		}
		
		Iterator<Set<SchemaTransition>> schemaTransitionsIterator = schemaHistory.getSchemaTransitionsIterator();
		
		while (schemaTransitionsIterator.hasNext())
		{
			Set<SchemaTransition> schemaTransitions = schemaTransitionsIterator.next();
			
			for (SchemaTransition schemaTransition : schemaTransitions)
			{
				Schema oldSchema = schemaTransition.getOldSchemaVersion();
				Schema newSchema = schemaTransition.getNewSchemaVersion();
				Table affectedTable = schemaTransition.getAffectedTable();
				SchemaTransitionChange transitionChange = schemaTransition.getSchemaTransitionChange();
				Schema schema = null;
				
				switch (transitionChange)
				{
				case TABLE_CREATED:
					schema = newSchema;
					break;
				case TABLE_DELETED:
					schema = oldSchema;
					break;
				case TABLE_UPDATED:
					schema = newSchema;
					break;
				}
				
				tableStatus.get(schema).put(affectedTable.getName(), transitionChange);
			}
		}
	}
}
