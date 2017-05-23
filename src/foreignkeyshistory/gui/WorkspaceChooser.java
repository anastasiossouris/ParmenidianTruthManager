package foreignkeyshistory.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class WorkspaceChooser extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Gui parent=null;
	private JTextField textField;
	
	public WorkspaceChooser(Gui g){
		parent=g;
		init();
	}

	public WorkspaceChooser(){
		setAlwaysOnTop(true);
		
		init();
		
	}
	
	public void init(){


		setIconImage(Toolkit.getDefaultToolkit().getImage(WorkspaceChooser.class.getResource("/icons/pi.png")));
		setResizable(false);
		setTitle("Workspace Launcher");
		setMinimumSize(new Dimension(475, 260));
		getContentPane().setLayout(null);
		setLocation(new Point(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-getSize().width/2,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-getSize().height/2));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Workspace: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 37, 77, 14);
		getContentPane().add(lblNewLabel);
		
		Gui.prefs= Preferences.userRoot().node("preferences");
		
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser a = new JFileChooser(Gui.prefs.get("workspace", "66"));
				a.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(a.showOpenDialog(getContentPane())==JFileChooser.APPROVE_OPTION){
					
					String[] array ={a.getSelectedFile().getAbsolutePath()};
					DefaultComboBoxModel model = new DefaultComboBoxModel(array);
					textField.setText(model.getElementAt(0).toString());
				}


			}
		});
		btnBrowse.setBounds(364, 34, 89, 23);
		getContentPane().add(btnBrowse);
		
		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(364, 184, 89, 23);
		getContentPane().add(btnNewButton);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Use this as default");
		chckbxNewCheckBox.setBounds(6, 153, 135, 23);
		getContentPane().add(chckbxNewCheckBox);
		
		JButton btnNewButton_1 = new JButton("OK");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxNewCheckBox.isSelected())
					savePreferences(true);
				else
					savePreferences(false);
				saveWorkspace(textField.getText());
				dispose();
				if(parent==null){
					Gui r = new Gui();
					r.setVisible(true);
				}
			}
		});
		btnNewButton_1.setBounds(265, 184, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		getRootPane().setDefaultButton(btnNewButton_1);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textField.setBounds(82, 33, 272, 25);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(Gui.prefs.get("workspace",""));
		textField.setDragEnabled(true);
		textField.selectAll();
		
		TransferHandler handler =   new TransferHandler() {

	        @Override
	        public boolean canImport(TransferHandler.TransferSupport info) {
	            // we only import FileList
	            return true;
	        }

	        @Override
	        public boolean importData(TransferHandler.TransferSupport info) {
	        	
	        	Transferable t = info.getTransferable();
	        	try {
	        		
					textField.setText(getRefinedText(t.getTransferData(DataFlavor.javaFileListFlavor).toString()));
					textField.selectAll();
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return true;
	        }

	        private void displayDropLocation(String string) {
	            System.out.println(string);
	        }
	    };
	    
	    textField.setTransferHandler(handler);

		btnNewButton_1.requestFocus();

	}
	
	protected String getRefinedText(String str) {

		String [] array=str.split("\\[",2);
		array=array[1].split("\\]",2);
		return array[0];
	}

	public void saveWorkspace(String workspace){
		
	Gui.prefs= Preferences.userRoot().node("preferences");
	Gui.prefs.put("workspace",workspace);
	
	if(parent!=null)
		parent.refreshWorkspace();
		

	}
   public void savePreferences(boolean token){
	   
	   Gui.prefs= Preferences.userRoot().node("preferences");
	   Gui.prefs.putBoolean("useDefault",token);


   }
}