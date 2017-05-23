package foreignkeyshistory.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Toolkit;

public class OutputChooser extends JDialog {
	
	private boolean pptxWanted=true;
	private boolean videoWanted=false;
	
	
	public OutputChooser(Component parent,final boolean[] array) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(OutputChooser.class.getResource("/icons/pi.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);

		setMinimumSize(new Dimension(280, 190));
		getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(OutputChooser.class.getResource("/icons/check.png")));
		lblNewLabel.setBounds(10, 29, 48, 69);
		getContentPane().add(lblNewLabel);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("PowerPoint Presentation");

		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(81, 50, 165, 23);
		getContentPane().add(chckbxNewCheckBox);
		
		final JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Video");
		chckbxNewCheckBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				videoWanted =!videoWanted;
			}
		});
		
		chckbxNewCheckBox_1.setBounds(81, 75, 97, 23);
		getContentPane().add(chckbxNewCheckBox_1);
		
		JLabel lblNewLabel_1 = new JLabel("Output will generate:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(81, 29, 127, 14);
		getContentPane().add(lblNewLabel_1);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				array[0]=pptxWanted;
				array[1]=videoWanted;
				
				dispose();
				
			}
		});
		btnDone.setBounds(89, 128, 89, 23);
		getContentPane().add(btnDone);
		
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				pptxWanted =!pptxWanted;
				chckbxNewCheckBox_1.setEnabled(pptxWanted);
				
			}
		});
		
		
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
		
		
	}


	public boolean isPptx() {
		return pptxWanted;
	}


	public boolean isVideo() {
		return videoWanted;
	}
}
