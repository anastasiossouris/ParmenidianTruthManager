package foreignkeyshistory.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

//import export.HecateScript;

public class ProjectEditor extends JFrame {
	
	JFileChooser fileChooser = new JFileChooser();

	public ProjectEditor(final Gui parent, final String workspace, final boolean edit, String pn, String sql, String xml, String graphml, String tf,final String ini) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(405,333));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProjectEditor.class.getResource("/icons/pi.png")));
		getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel_3 = new JLabel("Project name:");
		lblNewLabel_3.setBounds(20, 65, 84, 14);
		getContentPane().add(lblNewLabel_3);
		
		final TextField textField_2 = new TextField();
		textField_2.setBounds(110, 62, 173, 22);
		getContentPane().add(textField_2);
		
		JLabel lblParmenidianProject = new JLabel("Parmenidian Project");
		lblParmenidianProject.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblParmenidianProject.setBounds(135, 11, 130, 16);
		getContentPane().add(lblParmenidianProject);
		
		JLabel lblNewLabel = new JLabel("sql folder:");
		lblNewLabel.setBounds(20, 93, 84, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Transition file:");
		lblNewLabel_1.setBounds(20, 124, 78, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblInput = new JLabel("Input");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblInput.setBounds(10, 29, 31, 14);
		getContentPane().add(lblInput);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 54, 375, 2);
		getContentPane().add(separator);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOutput.setBounds(10, 177, 46, 14);
		getContentPane().add(lblOutput);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 202, 375, 2);
		getContentPane().add(separator_1);
		
		JLabel lblNewLabel_2 = new JLabel("Output folder:");
		lblNewLabel_2.setBounds(20, 215, 84, 14);
		getContentPane().add(lblNewLabel_2);
		
		final TextField textField = new TextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				 if (arg0.getClickCount() == 2) 
					 textField.setEditable(true);
				
			}
		});
		textField.setBounds(110, 89, 173, 22);
		getContentPane().add(textField);
		
		final TextField textField_1 = new TextField();
		textField_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 if (e.getClickCount() == 2) 
					 textField_1.setEditable(true);
			}
		});
		textField_1.setBounds(110, 121, 173, 22);
		getContentPane().add(textField_1);
		
		final TextField textField_3 = new TextField();
		textField_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 if (e.getClickCount() == 2) 
					 textField_3.setEditable(true);
				 
			}
		});
		textField_3.setBounds(110, 210, 173, 22);
		getContentPane().add(textField_3);
		
		Button button = new Button("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				fileChooser.resetChoosableFileFilters();
				
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(fileChooser.showOpenDialog(ProjectEditor.this)==JFileChooser.APPROVE_OPTION){
					textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					textField.setEditable(false);
				}
				
				
			}
		});
		button.setBounds(289, 89, 23, 22);
		getContentPane().add(button);
		
		Button button_1 = new Button("...");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fileChooser.resetChoosableFileFilters();
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
				fileChooser.setFileFilter(filter);
				
				if(fileChooser.showOpenDialog(ProjectEditor.this)==JFileChooser.APPROVE_OPTION){
					textField_1.setText(fileChooser.getSelectedFile().getAbsolutePath());
					textField_1.setEditable(false);
				}
				
				
			}
		});
		button_1.setBounds(289, 121, 23, 22);
		getContentPane().add(button_1);
		
		Button button_3 = new Button("...");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fileChooser.resetChoosableFileFilters();
				
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(fileChooser.showOpenDialog(ProjectEditor.this)==JFileChooser.APPROVE_OPTION){
					textField_3.setText(fileChooser.getSelectedFile().getAbsolutePath());
					textField_3.setEditable(false);
				}
				
			}
		});
		button_3.setBounds(289, 210, 23, 22);
		getContentPane().add(button_3);
		
		final TextField textField_4 = new TextField();

		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!textField.getText().trim().equals("") && !textField_1.getText().trim().equals("") && !textField_3.getText().trim().equals("") && !textField_2.getText().trim().equals("")){
					
					String restOfFile = "";
					if(edit){
						
						try {
							BufferedReader reader;
							
								reader = new BufferedReader(new FileReader(ini));
	
							String line;
							while((line = reader.readLine()) != null )
							{
								
								if(line.contains("graphml@") || line.contains("frameX@")|| line.contains("frameY@") || line.contains("scaleX@")|| line.contains("scaleY@") || line.contains("centerX@") || line.contains("centerY@"))
										restOfFile+=line+"\n";

							}
							
							reader.close();
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						File file = new File(ini);
						file.delete();
					}
						
					
					try {
						
						PrintWriter writer;
						
						writer = new PrintWriter(workspace+"\\"+textField_2.getText().trim()+".ini", "UTF-8");
						writer.println("Project Name:"+textField_2.getText().trim());
						writer.println("sql@"+textField.getText().trim());
						writer.println("transition@"+textField_1.getText().trim());
						writer.println("output@"+textField_3.getText().trim());
						if(edit)
							writer.println(restOfFile);
						
						writer.close();
						
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						parent.loadLifetime(workspace+"\\"+textField_2.getText().trim()+".ini");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					
					
					
					
					
					
					dispose();
					
					
				}else
					JOptionPane.showMessageDialog(ProjectEditor.this,"Fill in all required fields");
					
				
			}
		});
		btnNewButton.setBounds(89, 275, 89, 23);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				dispose();
			}
		});
		btnNewButton_1.setBounds(206, 275, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					 if(fileChooser.showOpenDialog(ProjectEditor.this)==JFileChooser.APPROVE_OPTION){
						 try {
//							HecateScript hecate= new HecateScript(fileChooser.getSelectedFile());
//							hecate.createTransitions();
							 parent.getManager().createTransitions(fileChooser.getSelectedFile());
							
							JOptionPane.showMessageDialog(ProjectEditor.this,"Transition file was created successfully");
						} catch (Exception e) {
							JOptionPane.showMessageDialog(ProjectEditor.this, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
						}
					 }
					
				}
				
			
		});
		btnNewButton_2.setToolTipText("Create Transition File");
		btnNewButton_2.setIcon(new ImageIcon(ProjectEditor.class.getResource("/icons/icon.png")));
		btnNewButton_2.setBounds(318, 120, 49, 25);
		getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel_4 = new JLabel("Graphml file:");
		lblNewLabel_4.setToolTipText("Auto-generated file");
		lblNewLabel_4.setEnabled(false);
		lblNewLabel_4.setBounds(20, 152, 89, 14);
		getContentPane().add(lblNewLabel_4);
		
		textField_4.setEnabled(false);
		textField_4.setBounds(110, 149, 173, 22);
		getContentPane().add(textField_4);
		
		if(edit){
			textField_2.setText(pn);
			textField.setText(sql);
			textField_1.setText(xml);
			textField_4.setText(graphml);
			textField_3.setText(tf);			
		}
		

		
		
		setLocationRelativeTo(parent);
		setVisible(true);

	}
}
