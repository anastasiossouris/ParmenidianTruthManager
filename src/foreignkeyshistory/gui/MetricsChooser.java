package foreignkeyshistory.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

//import parmenidianEnumerations.Metric_Enums;
import foreignkeyshistory.utils.Metric_Enums;

public class MetricsChooser extends JDialog {
	private JCheckBox numberOfConnectedComponents;
	private JCheckBox numberOfEdges;
	private JCheckBox graphDiameter;
	private JCheckBox numberOfVertices;
	private JCheckBox edgeBetweenness;
	private JCheckBox vertexBetweenness;
	private JCheckBox outDegree;
	private JCheckBox inDegree;
	private JCheckBox vertexDegree;
	private JCheckBox clusteringCoefficient;
	private JCheckBox numberOfVerticesInGcc;
	private JCheckBox numberOfEdgesInGcc;
	
	
	public MetricsChooser(final Gui parent) {
		setResizable(false);
		setMinimumSize(new Dimension(510, 480));
		setMaximumSize(new Dimension(510, 480));
		getContentPane().setLayout(null);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		JLabel lblNewLabel = new JLabel("Metrics");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(205, 7, 49, 17);
		getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 55, 464, 2);
		getContentPane().add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Vertex Properties");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(20, 35, 404, 14);
		getContentPane().add(lblNewLabel_1);
		
		inDegree = new JCheckBox("Vertex InDegree");
		inDegree.setBounds(20, 67, 125, 23);
		getContentPane().add(inDegree);
		
		outDegree = new JCheckBox("Vertex OutDegree");
		outDegree.setBounds(168, 67, 125, 23);
		getContentPane().add(outDegree);
		
		vertexDegree = new JCheckBox("Vertex Degree");
		vertexDegree.setBounds(312, 67, 111, 23);
		getContentPane().add(vertexDegree);
		
		vertexBetweenness = new JCheckBox("Vertex Betweenness");
		vertexBetweenness.setBounds(20, 93, 146, 23);
		getContentPane().add(vertexBetweenness);
		
		clusteringCoefficient = new JCheckBox("Clustering Coefficient");
		clusteringCoefficient.setBounds(168, 93, 170, 23);
		getContentPane().add(clusteringCoefficient);
		
		JLabel lblNewLabel_2 = new JLabel("Edge Properties");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(20, 130, 97, 14);
		getContentPane().add(lblNewLabel_2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 155, 464, 2);
		getContentPane().add(separator_1);
		
		edgeBetweenness = new JCheckBox("Edge Betweenness");
		edgeBetweenness.setBounds(20, 164, 146, 23);
		getContentPane().add(edgeBetweenness);
		
		JLabel lblNewLabel_3 = new JLabel("Graph Properties (whole graph)");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.setBounds(20, 209, 273, 14);
		getContentPane().add(lblNewLabel_3);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(20, 234, 464, 2);
		getContentPane().add(separator_2);
		
		graphDiameter = new JCheckBox("Graph Diameter");
		graphDiameter.setEnabled(true);
		graphDiameter.setBounds(312, 347, 111, 23);
		getContentPane().add(graphDiameter);
		
		numberOfVertices = new JCheckBox("# of Vertices");
		numberOfVertices.setBounds(20, 243, 125, 23);
		getContentPane().add(numberOfVertices);
		
		numberOfEdges = new JCheckBox("# of Edges");
		numberOfEdges.setBounds(168, 243, 97, 23);
		getContentPane().add(numberOfEdges);
		
		numberOfConnectedComponents = new JCheckBox("# of Connected Components");
		numberOfConnectedComponents.setEnabled(true);
		numberOfConnectedComponents.setBounds(312, 243, 212, 23);
		getContentPane().add(numberOfConnectedComponents);
		
		JLabel lblNewLabel_4 = new JLabel("Graph Properties (giant connected component)");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_4.setBounds(20, 313, 273, 14);
		getContentPane().add(lblNewLabel_4);
		
		numberOfVerticesInGcc = new JCheckBox("# of Vertices");
		numberOfVerticesInGcc.setBounds(20, 347, 97, 23);
		getContentPane().add(numberOfVerticesInGcc);
		
		numberOfEdgesInGcc = new JCheckBox("# of Edges");
		numberOfEdgesInGcc.setBounds(168, 347, 97, 23);
		getContentPane().add(numberOfEdgesInGcc);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(20, 338, 464, 2);
		getContentPane().add(separator_3);
		
		JButton btnNewButton = new JButton("Calculate");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ArrayList<Metric_Enums> metrics = new ArrayList<Metric_Enums>();
				
				if(inDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_IN_DEGREE);
				if(outDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_OUT_DEGREE);
				if(vertexDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_DEGREE);
				if(vertexBetweenness.isSelected())
					metrics.add(Metric_Enums.VERTEX_BETWEENNESS);
				if(clusteringCoefficient.isSelected())
					metrics.add(Metric_Enums.CLUSTERING_COEFFICIENT);
				if(edgeBetweenness.isSelected())
					metrics.add(Metric_Enums.EDGE_BETWEENNESS);
				if(graphDiameter.isSelected())
					metrics.add(Metric_Enums.GRAPH_DIAMETER);
				if(numberOfVertices.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_VERTICES);
				if(numberOfEdges.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_EDGES);
				if(numberOfConnectedComponents.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_CONNECTED_COMPONENTS);
				if(numberOfVerticesInGcc.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_VERTICES_IN_GCC);
				if(numberOfEdgesInGcc.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_EDGES_IN_GCC);
				
				dispose();

				try {
					parent.calculateMetrics(metrics);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(183, 417, 97, 23);
		getContentPane().add(btnNewButton);
		

		
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
