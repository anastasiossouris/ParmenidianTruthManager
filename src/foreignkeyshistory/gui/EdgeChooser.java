package foreignkeyshistory.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EdgeChooser extends JDialog {
	private ButtonGroup buttons = new ButtonGroup();
	private JRadioButton linearButton = new JRadioButton("Linear");
	private JRadioButton orthogonalButton = new JRadioButton("Orthogonal");
	private final JLabel lblNewLabel_1 = new JLabel("");
	private int edgeType;

	public EdgeChooser(Component parent) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setMinimumSize(new Dimension(270, 150));

		buttons.add(linearButton);
		buttons.add(orthogonalButton);
		setResizable(false);
		setTitle("Select Edge Type");
		getContentPane().setLayout(null);
		linearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				edgeType = 0;
				dispose();
			}
		});

		linearButton.setBounds(98, 49, 109, 23);
		getContentPane().add(linearButton);
		orthogonalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeType = 1;
				dispose();
			}
		});

		orthogonalButton.setBounds(98, 75, 109, 23);
		getContentPane().add(orthogonalButton);

		JLabel lblNewLabel = new JLabel("Type of Edges:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(98, 28, 109, 14);
		getContentPane().add(lblNewLabel);
		lblNewLabel_1.setIcon(new ImageIcon(EdgeChooser.class
				.getResource("/icons/questionmark.png")));
		lblNewLabel_1.setBounds(10, 28, 69, 59);

		getContentPane().add(lblNewLabel_1);

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);

	}

	public int getEdgeType() {
		return edgeType;
	}
}
