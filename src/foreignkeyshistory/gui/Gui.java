package foreignkeyshistory.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

//import parmenidianEnumerations.Metric_Enums;
import foreignkeyshistory.utils.Metric_Enums;
import foreignkeyshistory.gui.controller.ParmenidianTruthManager;
//import core.ParmenidianTruthManager;


import edu.uci.ics.jung.visualization.VisualizationViewer;



public class Gui extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private ArrayList<String> FileNames= new ArrayList<String>();
	private String workspace;
	private final JToolBar toolBar = new JToolBar();
	private JToggleButton mvNode = new JToggleButton("");
	private JToggleButton mvGraph = new JToggleButton("");
	private JButton button = new JButton("");
	private JButton btnNewButton_3 = new JButton("");
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ButtonGroup buttons = new ButtonGroup();
	private String targetFolder;
	private EdgeChooser edgeChooser;
	public static  Preferences prefs;
	private String projectName;
	private JFileChooser fileChooser = new JFileChooser();
	private String projectIni;
	private JRadioButton radio1 = new JRadioButton("Move Node    ");
	private JRadioButton radio2 = new JRadioButton("Move Graph");
	private JToolBar toolBar_1 = new JToolBar();
	private JPopupMenu pop = new JPopupMenu();
	private ParmenidianTruthManager manager= new ParmenidianTruthManager();
	private Component visualizationViewer;
	private String gGraphML = null;
	
	public Gui() {
		
		getContentPane().setBackground(new Color(214,217,223));
		this.workspace= prefs.get("workspace",null);

		buttons.add(mvNode);
		buttons.add(mvGraph);
		
		final JPopupMenu popupMenu = new JPopupMenu();
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/icons/omega.jpg"))); 

		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Lock / Unlock ToolBar");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(toolBar.isFloatable())
					toolBar.setFloatable(false);					
				else 
					toolBar.setFloatable(true);
			}
		});
		popupMenu.add(mntmNewMenuItem_1);
		
		JSeparator separator_5 = new JSeparator();
		popupMenu.add(separator_5);
		
		JMenuItem mntmHideToolbar = new JMenuItem("Hide ToolBar");
		mntmHideToolbar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				toolBar.setVisible(false);
				
			}
		});
		popupMenu.add(mntmHideToolbar);
		
		toolBar.add(popupMenu);

		toolBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
						if(arg0.getButton()==3){
							
							Point point = new Point(arg0.getPoint());
							
							popupMenu.setFocusable(false);
							
							popupMenu.show(toolBar, point.x,point.y);
						}
			}
		});
		toolBar.setBackground(new Color(240,240,240));
		buttonGroup.add(mvNode);
		
		
		radio1.setEnabled(false);
		radio2.setEnabled(false);
		mvNode.setEnabled(false);
		mvNode.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mvNode.setToolTipText("Move Node");
		mvNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				manager.setPickingMode();
//				wholeGraph.setPickingMode();
//				eg.setPickingMode();
				radio1.setSelected(true);
			}
		});
		mvNode.setIcon(new ImageIcon(Gui.class.getResource("/icons/click.png")));
		toolBar.add(mvNode);
		buttonGroup.add(mvGraph);
		
		mvGraph.setEnabled(false);
		mvGraph.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mvGraph.setToolTipText("Move Graph");
		mvGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				manager.setTransformingMode();
//				wholeGraph.setTransformingMode();
//				eg.setTransformingMode();
				radio2.setSelected(true);
				
			}
		});
		mvGraph.setIcon(new ImageIcon(Gui.class.getResource("/icons/handCursor.png")));
		toolBar.add(mvGraph);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setMaximumSize(new Dimension(10, 10));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_2);
		
		
		fileChooser.setCurrentDirectory(new File(workspace));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Parmenidian Project", "ini");
		fileChooser.setFileFilter(filter);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setToolTipText("Load Parmenidian Project");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				
				 if(fileChooser.showOpenDialog(Gui.this)==JFileChooser.APPROVE_OPTION){
					 try {
						loadLifetime(fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(Gui.this, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
//						panel.setVisible(true);
					}
				 }

			}
		});
		btnNewButton_2.setIcon(new ImageIcon(Gui.class.getResource("/icons/open-task.gif")));
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton_6 = new JButton("");
		btnNewButton_6.setToolTipText("Run Hecate Script");
		btnNewButton_6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createTransitions();
				
			}
		});
		btnNewButton_6.setIcon(new ImageIcon(Gui.class.getResource("/icons/icon.png")));
		toolBar.add(btnNewButton_6);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setMaximumSize(new Dimension(10, 10));
		toolBar.add(separator_3);
		
		button.setEnabled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setToolTipText("Save Layout");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					manager.saveVertexCoordinates(targetFolder + "uber.graphml");
					manager.updateIniFile(projectIni, targetFolder + "uber.graphml");
//					wholeGraph.saveVertexCoordinates(projectIni);
//					wholeGraph= eg.saveVertexCoordinates(wholeGraph,projectIni);
					JOptionPane.showMessageDialog(Gui.this,"Layout changes have been saved");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clear();

			}
		});
		btnNewButton_5.setToolTipText("Clear");
		btnNewButton_5.setIcon(new ImageIcon(Gui.class.getResource("/icons/clear.png")));
		toolBar.add(btnNewButton_5);
		button.setIcon(new ImageIcon(Gui.class.getResource("/icons/layout.png")));
		toolBar.add(button);
		
		JButton button_1 = new JButton("");
		button_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button_1.setToolTipText("Produce PowerPoint Presentation");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				loadImagesForPptx();
				createPowerPointPresentation();
				

				
			}
		});
		
		btnNewButton_3.setEnabled(false);
		btnNewButton_3.setToolTipText("Create Images Of Each Version");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				visualize(true);
				
			}
		});
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setMaximumSize(new Dimension(10, 10));
		separator_6.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_6);
		btnNewButton_3.setIcon(new ImageIcon(Gui.class.getResource("/icons/pic.png")));
		toolBar.add(btnNewButton_3);
		button_1.setIcon(new ImageIcon(Gui.class.getResource("/icons/preview.png")));
		toolBar.add(button_1);
		
		JButton btnNewButton_7 = new JButton("");
		btnNewButton_7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					createVideo();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_7.setToolTipText("Produce Video");
		btnNewButton_7.setIcon(new ImageIcon(Gui.class.getResource("/icons/Movies.png")));
		toolBar.add(btnNewButton_7);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setMaximumSize(new Dimension(10, 10));
		toolBar.add(separator_4);
		
		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_4.setToolTipText("Change Workspace");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				changeWorkspace();
			}
		});
		btnNewButton_4.setIcon(new ImageIcon(Gui.class.getResource("/icons/workspace.gif")));
		toolBar.add(btnNewButton_4);
		
//		TransferHandler handler =   new TransferHandler() {
//
//	        @Override
//	        public boolean canImport(TransferHandler.TransferSupport info) {
//	            // we only import FileList
//	            return true;
//	        }
//
//	        @Override
//	        public boolean importData(TransferHandler.TransferSupport info) {
//	        	
//	        	Transferable t = info.getTransferable();
//	        	try {
//	        		
//
//	        		txtDropResourceFolder.setText(getRefinedText(t.getTransferData(DataFlavor.javaFileListFlavor).toString()));
//					try {
//						loadLifetime(getDnDFilename(t.getTransferData(DataFlavor.javaFileListFlavor).toString()).getAbsolutePath());
////						s(eg.getTargetFolder());
////						createUniversalGraph();
//					} catch (Exception e) {
//						e.printStackTrace();
//						JOptionPane.showMessageDialog(Gui.this, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
//						panel.setVisible(true);
//					}
//
//	        		
//				} catch (UnsupportedFlavorException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//	            return true;
//	        }
//
//	        private void displayDropLocation(String string) {
//	            System.out.println(string);
//	        }
//	    };
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar_1.setVisible(false);
		toolBar.add(toolBar_1);
		
		JLabel lblAttraction = new JLabel("Force Multiplier: ");
		lblAttraction.setFont(new Font("Tahoma", Font.BOLD, 11));
		toolBar_1.add(lblAttraction);
		
		final JSpinner repulsionRangeSpinner = new JSpinner();
		repulsionRangeSpinner.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		repulsionRangeSpinner.setMinimumSize(new Dimension(50, 20));
		repulsionRangeSpinner.setMaximumSize(new Dimension(50, 20));
		
		final JSpinner forceMultiplierSpinner = new JSpinner();
		forceMultiplierSpinner.setMinimumSize(new Dimension(100, 15));
		forceMultiplierSpinner.setModel(new SpinnerNumberModel(0.3, 0.0, 3.0, 0.10));
		forceMultiplierSpinner.setMaximumSize(new Dimension(50, 20));
		forceMultiplierSpinner.addChangeListener(new ChangeListener() {

		        @Override
		        public void stateChanged(ChangeEvent e) {
		        	
		        	getContentPane().remove(1);
		        	getContentPane().add(manager.refresh((double)forceMultiplierSpinner.getValue(),(int)repulsionRangeSpinner.getValue()));
					getContentPane().invalidate();
					getContentPane().repaint();
		        	
//					eg.updateVisualizationViewer((double)attractionSpinner.getValue(),(double)repulsionSpinner.getValue(),nowShowing);
//					getContentPane().invalidate();
//					getContentPane().repaint();
		            
		        }
		    });
		
		toolBar_1.add(forceMultiplierSpinner);
		
		JLabel lblRepulsion = new JLabel("  Repulsion Range: ");
		toolBar_1.add(lblRepulsion);
		lblRepulsion.setFont(new Font("Tahoma", Font.BOLD, 11));
		

		repulsionRangeSpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	        	
//				eg.updateVisualizationViewer((double)attractionSpinner.getValue(),(double)repulsionSpinner.getValue(),nowShowing);
//				getContentPane().invalidate();
//				getContentPane().repaint();
	            
	        }
	    });
		
		toolBar_1.add(repulsionRangeSpinner);
		
		
		JMenuItem menuItem = new JMenuItem("Spread..");		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
//				eg.spreadOut(nowShowing);
//				getContentPane().invalidate();
//				getContentPane().repaint();
			}
		});
		menuItem.setIcon(new ImageIcon(Gui.class.getResource("/icons/spread.png")));
		pop.add(menuItem);
		getContentPane().add(pop, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/icons/pi.png")));
		
		
		setTitle("Parmenidian Truth");
		setMinimumSize(new Dimension(1020, 600));

		setLocation(new Point(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-getSize().width/2,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-getSize().height/2));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile_1 = new JMenu("File");
		menuBar.add(mnFile_1);
		
		JMenuItem mntmLoadDbVersion = new JMenuItem("Load Parmenidian Project");
		mntmLoadDbVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				 if(fileChooser.showOpenDialog(Gui.this)==JFileChooser.APPROVE_OPTION){
				
					 
					 try {
						loadLifetime(fileChooser.getSelectedFile().getAbsolutePath());
//						 createUniversalGraph();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(Gui.this, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
//						panel.setVisible(true);
					}
					

				 }

				
			}

		});
		
		JMenuItem mntmNewParmenidianProject = new JMenuItem("New Parmenidian Project");
		mntmNewParmenidianProject.setIcon(new ImageIcon(Gui.class.getResource("/icons/create.png")));
		mntmNewParmenidianProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createNewProject();
				
			}

		});
		mnFile_1.add(mntmNewParmenidianProject);
		
		mntmLoadDbVersion.setIcon(new ImageIcon(Gui.class.getResource("/icons/open-task.gif")));
		mnFile_1.add(mntmLoadDbVersion);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Edit Parmenidian Project");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					editProject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		mntmNewMenuItem_3.setIcon(new ImageIcon(Gui.class.getResource("/icons/1425057348_create-16.png")));
		mnFile_1.add(mntmNewMenuItem_3);
		
		JSeparator separator_9 = new JSeparator();
		mnFile_1.add(separator_9);
		
		JMenuItem mntmChangeWorkspace = new JMenuItem("Change Workspace");
		mnFile_1.add(mntmChangeWorkspace);
		mntmChangeWorkspace.setIcon(new ImageIcon(Gui.class.getResource("/icons/workspace.gif")));
		mntmChangeWorkspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				changeWorkspace();

			}
		});
		
		JMenu mnFile = new JMenu("Run");
		menuBar.add(mnFile);
		
		JMenuItem mntmGenerateOutput = new JMenuItem("Generate Output");
		mntmGenerateOutput.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmGenerateOutput.setIcon(new ImageIcon(Gui.class.getResource("/icons/video.png")));
		mntmGenerateOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					batchOutput();
				} catch (IOException e) {					
					e.printStackTrace();
				}
				
			}

		});
		
		
		mnFile.add(mntmGenerateOutput);
		
		JSeparator separator_7 = new JSeparator();
		mnFile.add(separator_7);
		
		JMenuItem mntmGenerateMetrics = new JMenuItem("Generate Metrics ");
		mntmGenerateMetrics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				openMetricsPanel();
				
			}


		});
		mntmGenerateMetrics.setIcon(new ImageIcon(Gui.class.getResource("/icons/metrics.png")));
		mnFile.add(mntmGenerateMetrics);
		
		JMenu mnIndividualActions = new JMenu("Individual Actions");
		mnFile.add(mnIndividualActions);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Produce Episodes");
		mnIndividualActions.add(mntmNewMenuItem);
		mntmNewMenuItem.setIcon(new ImageIcon(Gui.class.getResource("/icons/pic.png")));
		
		JMenuItem mntmPreview = new JMenuItem("Create Presentation");
		mnIndividualActions.add(mntmPreview);
		mntmPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				loadImagesForPptx();
				createPowerPointPresentation();
				

				
			}
		});
		mntmPreview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmPreview.setIcon(new ImageIcon(Gui.class.getResource("/icons/preview.png")));
		
		JMenuItem mntmProduceVideojv = new JMenuItem("Produce Video");
		mnIndividualActions.add(mntmProduceVideojv);
		mntmProduceVideojv.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmProduceVideojv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					createVideo();
					

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mntmProduceVideojv.setIcon(new ImageIcon(Gui.class.getResource("/icons/Movies.png")));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				visualize(true);
				
			}
		});
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenu mnNewMenu = new JMenu("External Tools");
		mnNewMenu.setIcon(new ImageIcon(Gui.class.getResource("/icons/tools.png")));
		mnFile.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Generate Transitions");
		mnNewMenu.add(mntmNewMenuItem_2);
		mntmNewMenuItem_2.setIcon(new ImageIcon(Gui.class.getResource("/icons/icon.png")));
		
		JMenu mnLayout = new JMenu("Graph");
		menuBar.add(mnLayout);
		
		JMenuItem mntmSaveLayout = new JMenuItem("Save Layout");
		mntmSaveLayout.setIcon(new ImageIcon(Gui.class.getResource("/icons/layout.png")));
		mntmSaveLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					manager.saveVertexCoordinates(projectIni);
//					wholeGraph.saveVertexCoordinates(projectIni);
//					wholeGraph= eg.saveVertexCoordinates(wholeGraph,projectIni);
					JOptionPane.showMessageDialog(Gui.this,"Layout changes have been saved");

				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		mnLayout.add(mntmSaveLayout);
		
		JMenu mnEditLayout = new JMenu("Edit Layout");
		mnLayout.add(mnEditLayout);
		
		
		radio1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				mvNode.doClick();
				
			}
		});
		mnEditLayout.add(radio1);
		
		radio2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mvGraph.doClick();
			}
		});
		mnEditLayout.add(radio2);
		
		ButtonGroup radio = new ButtonGroup();
		radio.add(radio1);
		radio.add(radio2);
		
		JSeparator separator_8 = new JSeparator();
		mnLayout.add(separator_8);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mnLayout.add(mntmClear);
		mntmClear.setIcon(new ImageIcon(Gui.class.getResource("/icons/clear.png")));
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clear();

			}
		});
		
		JMenu mnAbout = new JMenu("Misc");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAboutTheProject = new JMenuItem("About The Project");
		mntmAboutTheProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
			
			}
		});
		
		JMenuItem mntmToolbaronoff = new JMenuItem("ToolBar [On/Off]");
		mntmToolbaronoff.setIcon(new ImageIcon(Gui.class.getResource("/icons/toolbar.png")));
		mntmToolbaronoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(toolBar.isVisible())
					toolBar.setVisible(false);
				else
					toolBar.setVisible(true);				
				
			}
		});
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.setIcon(new ImageIcon(Gui.class.getResource("/icons/quest.png")));
		mnAbout.add(mntmHelp);
		mnAbout.add(mntmToolbaronoff);
		
		JSeparator separator = new JSeparator();
		mnAbout.add(separator);
		mntmAboutTheProject.setIcon(new ImageIcon(Gui.class.getResource("/icons/info.png")));
		mnAbout.add(mntmAboutTheProject);
		
		
		
	}
	
	protected void createVideo() throws IOException {
		
	/*	FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Presentation", "pptx");
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		
		if(chooser.showOpenDialog(Gui.this)==JFileChooser.APPROVE_OPTION){
			
//			VideoGenerator vg = new VideoGenerator(chooser.getSelectedFile());
//			vg.exportToVideo();
			manager.createVideo(chooser.getSelectedFile(), edgeChooser.getEdgeType());
			JOptionPane.showMessageDialog(Gui.this,"Video was created successfully");

		}*/
		
		manager.createVideo(new File(targetFolder), edgeChooser.getEdgeType());
		JOptionPane.showMessageDialog(Gui.this,"Video was created successfully");
	}
	
	protected void createVideo(File presentation) throws IOException{
		
//		VideoGenerator vg = new VideoGenerator(presentation);
//		vg.exportToVideo();
		manager.createVideo(presentation, edgeChooser.getEdgeType());
		JOptionPane.showMessageDialog(Gui.this,"Video was created successfully");

		
	}
	

	public void createTransitions() {

		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		 if(fileopen.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			 try {
//				HecateScript hecate= new HecateScript(fileopen.getSelectedFile());
//				hecate.createTransitions();
				 
				 manager.createTransitions(fileopen.getSelectedFile());
				
				JOptionPane.showMessageDialog(this,"Transition file was created successfully");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
			}
		 }
		
	}

	protected void loadImagesForPptx() {

		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if(fileopen.showOpenDialog(Gui.this)==JFileChooser.APPROVE_OPTION){
			File f = fileopen.getSelectedFile();
//			presentationName=f.getName();		
		
			File dir = new File(f.getPath());				
			File[] list = dir.listFiles();
			for(int i=0;i<list.length;i++){
				FileNames.add(list[i].toString());
			}
		}
		
	}
	
	protected void loadImagesForPptx(String path) {

		
			File f = new File(path);
//			presentationName=f.getName();		
		
			File dir = new File(f.getPath());				
			File[] list = dir.listFiles();
			
			for(int i=0;i<list.length;i++)
				FileNames.add(list[i].toString());
			
		
	}
	
	
	

	protected void changeWorkspace() {
		
		WorkspaceChooser c = new WorkspaceChooser(Gui.this);
		c.setVisible(true);
		
	}

	protected void s(Object string) {
		
		System.out.println(string);
		
	}

	protected File createPowerPointPresentation() {
		
		
		if(FileNames.isEmpty())
			return null;
		
//		PowerPointGenerator pptx=new PowerPointGenerator(targetFolder,projectName);
//		
//		try {
//			pptx.createPresentation(FileNames);
//			JOptionPane.showMessageDialog(Gui.this,"Powerpoint Presentation was created successfully");
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		finally{
//			FileNames.clear();
//		}
		
		
		try {
			
			manager.createPowerPointPresentation(FileNames, targetFolder, projectName, edgeChooser.getEdgeType());
			JOptionPane.showMessageDialog(Gui.this,"Powerpoint Presentation was created successfully");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			FileNames.clear();
		}
		
		
		
		return new File(targetFolder+"\\"+projectName+".pptx");

		
	}

//	protected void createDiachronicGraph(int mode, String sql,double frameX,double frameY, double scaleX,double scaleY,double centerX,double centerY) {
//		this.setResizable(true);
//		this.setLocation(0, 0);
//
//		
//		if(mode==0)
//			wholeGraph= new DiachronicGraph(lifetime,sql,targetFolder,edgeChooser.getEdgeType(),mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//		else
//			wholeGraph = new DiachronicGraph(savedChanges,sql,targetFolder,edgeChooser.getEdgeType(),mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//		
//		
//
//	}

	public void loadLifetime(String selectedFile) throws Exception {
		
		clear();
		
		projectIni=selectedFile;
		
		edgeChooser = new EdgeChooser(this);
//		manager = new ParmenidianTruthManager();
		
		String sql = null,xml = null,graphml=null;
		double frameX = 0,frameY = 0,scaleX = 0,scaleY = 0,centerX = 0,centerY = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(projectIni));
		String line;
		
		while((line = reader.readLine()) != null)
		{
			if(line.contains("Project Name:"))
				projectName=line.split(":")[1];
			else if(line.contains("sql@"))
		    	sql= line.split("@")[1];
		    else if(line.contains("transition@"))
		    	xml=line.split("@")[1];
		    else if(line.contains("graphml@"))
		    	graphml=line.split("@")[1];
		    else if(line.contains("output@"))
		    	targetFolder=line.split("@")[1];
		    else if (line.contains("frameX@"))
		    	frameX=Double.parseDouble(line.split("@")[1]);
		    else if (line.contains("frameY@"))
		    	frameY=Double.parseDouble(line.split("@")[1]);
		    else if (line.contains("scaleX@"))
		    	scaleX=Double.parseDouble(line.split("@")[1]);
		    else if (line.contains("scaleY@"))
		    	scaleY=Double.parseDouble(line.split("@")[1]);
		    else if (line.contains("centerX@"))
		    	centerX= Double.parseDouble(line.split("@")[1]);
		    else if (line.contains("centerY@"))
		    	centerY= Double.parseDouble(line.split("@")[1]);
		    	
		}
		reader.close();
		
		visualizationViewer = manager.loadProject(sql,xml,graphml,frameX,frameY,scaleX,scaleY,centerX,centerY,targetFolder,edgeChooser.getEdgeType());
		
		visualizationViewer.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyChar()=='m' || e.getKeyChar()=='M' )
					mvGraph.doClick();
				else if (e.getKeyChar()=='p' || e.getKeyChar()=='p')
					mvNode.doClick();
				
			

			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				

				
			}
			
		});
		
		getContentPane().add(visualizationViewer);


		setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		

		
		button.setEnabled(true);
		btnNewButton_3.setEnabled(true);
		mvNode.setEnabled(true);
		mvGraph.setEnabled(true);
		radio1.setEnabled(true);
		radio2.setEnabled(true);
		mvGraph.doClick();
		radio2.setSelected(true);
		this.setLocation(0, 0);

		manager.stopConvergence();
		
		visualizationViewer.requestFocus();
			
		
			
	}
	
	private void batchOutput() throws IOException {
		
		if(projectName==null){
			JOptionPane.showMessageDialog(Gui.this, "There is no project loaded");
			return;
		}
			

		boolean [] array = new boolean[2];		
		OutputChooser a = new OutputChooser(this,array);		

		
			if(array[0]){
				visualize(false);

				loadImagesForPptx(manager.getTargetFolder());
				File pptx = createPowerPointPresentation();
				
				if(array[1])
					createVideo(pptx);	
				
			}		
			
	}
	
	private void openMetricsPanel() {
		
		if(projectName==null){
			JOptionPane.showMessageDialog(Gui.this, "There is no project loaded");
			return;
		}
		
		MetricsChooser m = new MetricsChooser(this);	

		
		
	}
	
	private  void visualize(boolean atomically) {
		
		try {
			
			
			
			manager.visualize((VisualizationViewer< String, String>)visualizationViewer,projectIni, targetFolder, edgeChooser.getEdgeType());
			
			if(atomically)
				JOptionPane.showMessageDialog(Gui.this,"Images of each version were created successfully");

			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createNewProject() {
		
		ProjectEditor pe = new ProjectEditor(Gui.this,Gui.this.workspace,false,null,null,null,null,null,null);
		
	}
	

	private void editProject() throws IOException {
		
		if(projectName==null){
			JOptionPane.showMessageDialog(this, "No project to edit");
			return;
		}
		
		String sql = null,xml = null,graphml=null;
		
		BufferedReader reader = new BufferedReader(new FileReader(projectIni));
		String line;
		while((line = reader.readLine()) != null)
		{
			if(line.contains("Project Name:"))
				projectName=line.split(":")[1];
			else if(line.contains("sql@"))
		    	sql= line.split("@")[1];
		    else if(line.contains("transition@"))
		    	xml=line.split("@")[1];
		    else if(line.contains("graphml@"))
		    	graphml=line.split("@")[1];
		    else if(line.contains("output@"))
		    	targetFolder=line.split("@")[1];
		    	
		}
		reader.close();
		
		ProjectEditor pe = new ProjectEditor(Gui.this,Gui.this.workspace,true,projectName,sql,xml,graphml,targetFolder,projectIni);

		
	}

	/**
	 * Clear the JFrame of any graph,return window to its former position
	 */
	private void clear() {
		
		
		
		FileNames.clear();
//		lifetime.clear();
//		transitions.clear();
//		savedChanges=null;
//		if(wholeGraph!=null)
//			wholeGraph.clear();
//		wholeGraph=null;
//		if(manager!=null)
		manager.clear();
//		manager=null;
		targetFolder=null;
		EdgeChooser edgeChooser=null;
		projectName=null;
		projectIni=null;
//		nowShowing=null;
		
		
					
//		panel.setVisible(true);
		getContentPane().removeAll();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		invalidate();
		repaint();
		setSize(1020, 600);
		setResizable(false);
		setLocation(new Point(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-getSize().width/2,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-getSize().height/2));
		button.setEnabled(false);
		btnNewButton_3.setEnabled(false);
		mvNode.setEnabled(false);
		mvGraph.setEnabled(false);
		radio1.setEnabled(false);
		radio2.setEnabled(false);
//		toolBar_1.setVisible(false);
//		txtDropResourceFolder.setText("");
		
	}

//	private void updateLifetimeWithTransitions(){
//		
//		for(int i=0;i<lifetime.size();++i)
//			if(i==0)
//				setFirstVersion(lifetime.get(i));
//			else if(i==lifetime.size()-1)
//				setFinalVersion(lifetime.get(i),i);
//			else
//				setIntermediateVersion(lifetime.get(i),i);
//	
//			
//		
//	}
	
//	/**
//	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
//	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
//	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
//	 * @param fversion :firstVersion
//	 */
//	private void setFirstVersion(DBVersion fversion){
//		
//		for(int i=0;i<fversion.getTables().size();++i)
//			if(transitions.get(0).containsKey(fversion.getTables().get(i).getKey())
//			&& transitions.get(0).get(fversion.getTables().get(i).getKey())==1)
//				fversion.getTables().get(i).setColorCode(1);		
//		
//	}
//	
//	/**
//	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
//	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
//	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
//	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
//	 * dictionary mou.
//	 * @param fversion :finalVersion
//	 * @param k :H thesh ths teleutaias Version mou sthn Lista
//	 */
//	private void setFinalVersion(DBVersion fversion,int k){
//		
//		for(int i=0;i<fversion.getTables().size();++i)
//			if(transitions.get(k-1).containsKey(fversion.getTables().get(i).getKey())
//			&& transitions.get(k-1).get(fversion.getTables().get(i).getKey())!=1)
//				fversion.getTables().get(i).setColorCode(transitions.get(k-1).get(fversion.getTables().get(i).getKey()));
//		
//	}
//	
//	private void setIntermediateVersion(DBVersion version,int k){
//		
//		for(int i=0;i<version.getTables().size();++i){
//			//koitaw to mellontiko m dictionary
//			if(transitions.get(k).containsKey(version.getTables().get(i).getKey())
//			&& transitions.get(k).get(version.getTables().get(i).getKey())==1)
//				version.getTables().get(i).setColorCode(1);
//			
//			//koitaw to palho m dictionary
//			if(transitions.get(k-1).containsKey(version.getTables().get(i).getKey())
//			&& transitions.get(k-1).get(version.getTables().get(i).getKey())!=1)
//				version.getTables().get(i).setColorCode(transitions.get(k-1).get(version.getTables().get(i).getKey()));
//		
//				
//		}
//	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					        if ("Nimbus".equals(info.getName())) {
						  UIManager.setLookAndFeel(info.getClassName());
						  break;
					        }
					    }
					} catch (Exception e) {
					    
					}
					initiate();
				} catch (Exception e) {
					WorkspaceChooser c = new WorkspaceChooser();
					c.setVisible(true);
				}
			}
		});
	}
	
	
	private static void initiate() throws FileNotFoundException{
		
		
		prefs = Preferences.userRoot().node("preferences");
		if(prefs.getBoolean("useDefault", false)){
			Gui gui = new Gui();
			gui.setVisible(true);
		}else{
			WorkspaceChooser c = new WorkspaceChooser();
			c.setVisible(true);
		}
		
	}
	
	public void refreshWorkspace(){
		
		workspace= prefs.get("workspace","66");
		fileChooser.setCurrentDirectory(new File(workspace));
		
	}
	
	private static String retrieveSelectedWorkspace() throws FileNotFoundException{
		
		return prefs.get("workspace",null);
	}
	
	protected String getRefinedText(String str) {
		
		String [] array=str.split("\\[",2);
		array=array[1].split("\\]",2);
		return array[0].split("\\\\")[array[0].split("\\\\").length-1];
	}
	
	
	protected File getDnDFilename(String str) {

		String [] array=str.split("\\[",2);
		array=array[1].split("\\]",2);
		
		return new File(array[0]);
	}

	public ParmenidianTruthManager getManager() {
		return manager;
	}



	public void calculateMetrics(ArrayList<Metric_Enums> metrics) throws IOException {
		
		try {
			manager.calculateMetrics(targetFolder,metrics);
			JOptionPane.showMessageDialog(Gui.this,"Metrics were created successfully");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}


}
