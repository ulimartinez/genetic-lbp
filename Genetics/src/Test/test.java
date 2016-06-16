package Test;

import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JTextField;
import javax.swing.JLabel;

import com.sun.glass.events.WindowEvent;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.JMenu;
import javax.swing.JScrollBar;

import java.awt.Scrollbar;
import java.awt.ScrollPane;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class test {

	private JFrame frame;
	private int tasks;
	private JTable table;
	int[] times;
	int[][] precedences;
	int cycle;
	int numberOfChromosomes;
	private JTable table_1;
	private String fileName;
	JFileChooser fc;
	File file;
	private JTable table_2;
	private Chromosome[] chromos;
	private int generations = 1;
	private double childPercent = 0;
	private double mutationPercent = 0;
	private double preservedPercent = 0;
	private int maxTime = 0;
	private int minWS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test window = new test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	void setMinNumOfWS()
	{
		int sumTasks = 0;
		for (int i = 0; i < chromos[0].tasks.length; i++)
		{
			sumTasks += chromos[0].tasks[i].getTime();
		}
		minWS = (sumTasks / cycle) + 1;
	}
	boolean contains(final int[] array, final int v) 
	//checks if a number is contained in an array of numbers
	{
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}
	public Chromosome[] bestChromosomes(int amount)
	{
		int[] selected = new int[amount];
		Chromosome[] best = new Chromosome[amount];
		for (int i = 0; i < best.length; i++)
		{
			Chromosome bestSoFar = new Chromosome();
			int wasBest = 0;
			for (int j = 0; j < chromos.length; j++)
			{
				if (contains(selected, j))
				{

				}
				else if (bestSoFar.WSTimes == null || chromos[j].WSTimes.size() < bestSoFar.WSTimes.size())
				{
					bestSoFar = chromos[j];
					wasBest = j;
				}
				else if (chromos[j].WSTimes.size() == bestSoFar.WSTimes.size())
				{
					if (chromos[j].smoothness < bestSoFar.smoothness)
					{
						bestSoFar = chromos[j];
						wasBest = j;
					}
				}
			}
			best[i] = bestSoFar;
			selected[i] = wasBest;
			bestSoFar = chromos[0];
		}
		return best;
	}
	private Chromosome[] setNextGeneration()
	{
		Chromosome[] newGeneration = new Chromosome[chromos.length];
		int mutatedAmount = (int)(mutationPercent * chromos.length);
		int childAmount = amountOfChildren();
		int preservedAmount = (int)(preservedPercent * chromos.length);
		int remaining = chromos.length - (mutatedAmount + childAmount + preservedAmount);
		Chromosome[] preserve = bestChromosomes(preservedAmount);
		for (int i = 0; i < preserve.length; i++)
		{
			preserve[i].isPreserved = true;
			preserve[i].isChild = false;
			preserve[i].isMutation = false;
			preserve[i].generation++;
			newGeneration[i] = preserve[i];
		}
		Chromosome [] mutated = bestChromosomes(mutatedAmount);
		for (int i = 0; i < mutated.length; i++)
		{
			newGeneration[i + preserve.length] = mutated[i].mutate();
		}
		Chromosome[] reproduction = bestChromosomes(childAmount);
		int ind = preserve.length + mutated.length;
		for (int i = 0; i < reproduction.length; i += 2)
		{
			newGeneration[ind] = reproduction[i].crossOver(reproduction[i + 1]);
			ind++;
			newGeneration[ind] = reproduction[i + 1].crossOver(reproduction[i]);
			ind++;
		}
		for (int i = 0; i < remaining; i++)
		{
			code problem = new code(tasks);
			problem.setTasks(precedences,  times);
			int[] initialPop = problem.initialPopulation();
			problem.setChromosomes(initialPop);
			Chromosome chromo = problem.getChromosome();
			chromo.cycleTime = cycle;
			chromo.solution();
			newGeneration[i + (preserve.length + reproduction.length + mutated.length)] = chromo; 
		}
		return newGeneration;
	}
	public int amountOfChildren()
	{
		int childrenAmount = (int) (childPercent * chromos.length);
		if (childrenAmount % 2 == 0)
			return (childrenAmount);
		return (childrenAmount - 1);
	}

	/**
	 * Create the application.
	 */
	public test() {
		initialize();
	}
	public void load()
	{
		if (file != null)
		{
			try {
				int[] toTime = null;
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(file));
				sCurrentLine = br.readLine();
				sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
				sCurrentLine = sCurrentLine.replaceAll("\\s+","");
				String[] textTime = sCurrentLine.split(",");
				toTime = new int[textTime.length];
				for (int i = 0; i < textTime.length; i++)
				{
					toTime[i] = Integer.parseInt(textTime[i]);
				}
				times = toTime;
				for (int i = 0; i < times.length; i++)
				{
					if (maxTime < times[i])
					{
						maxTime = times[i];
					}
				}
				tasks = toTime.length;
				int[][] toPrecedences = new int[toTime.length][];
				toPrecedences[0] = null;
				int ind = 0;
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
					sCurrentLine = sCurrentLine.replaceAll("\\s+","");
					String[] textPre = sCurrentLine.split(",");
					int[] curr = new int[textPre.length];
					for (int i = 0; i < textPre.length; i++)
					{
						if (textPre[i].equals(""))
						{
							curr[i] = 0;
						}
						else
						{
							curr[i] = Integer.parseInt(textPre[i]);
						}
					}
					toPrecedences[ind] = curr;
					ind++;
				}
				precedences = toPrecedences;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			DefaultTableModel dtm = (DefaultTableModel) table.getModel();
			dtm.removeRow(0);
			for (int i = 0; i < times.length; i++)
			{
				dtm.addRow((Object[])null);
				dtm.setValueAt((i+1), (i), 0);
				dtm.setValueAt(times[i], (i), 1);
				String set = Arrays.toString(precedences[i]);
				set = set.substring(1, set.length() - 1);
				set = set.replaceAll("\\s+","");
				dtm.setValueAt(set, (i), 2);

			}
			table.setVisible(true);
			table.repaint();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 701, 463);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		numTasks open = new numTasks();
		open.contentPanel.setVisible(false);

		CycleTime askCycle = new CycleTime();
		askCycle.contentPanel.setVisible(false);

		numChromosomes amount = new numChromosomes();
		amount.contentPanel.setVisible(false);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOptions = new JMenuItem("New Problem");
		mnFile.add(mntmOptions);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		iterations iterations = new iterations();
		iterations.setVisible(false);

		children childrenWindow = new children();
		childrenWindow.setVisible(false);

		mutations mutationWindow = new mutations();
		mutationWindow.setVisible(false);

		preserved preservedWindow = new preserved();
		preservedWindow.setVisible(false);

		JMenuItem mntmInitialPopulation = new JMenuItem("Initial Population");
		mntmInitialPopulation.setEnabled(false);
		menuBar.add(mntmInitialPopulation);

		JMenuItem mntmGenerations = new JMenuItem("Generations");
		mntmGenerations.setEnabled(false);
		menuBar.add(mntmGenerations);
		
		JMenuItem mntmNextGeneration = new JMenuItem("Next Generation");
		mntmNextGeneration.setEnabled(false);
		menuBar.add(mntmNextGeneration);
		frame.getContentPane().setLayout(null);
		
				JPanel panel_2 = new JPanel();
				panel_2.setBounds(0, 202, 319, 198);
				frame.getContentPane().add(panel_2);
				
						table_2 = new JTable();
						table_2.setModel(new DefaultTableModel(
							new Object[][] {
							},
							new String[] {
								"Station", "Options", "Assigned", "Time Left"
							}
						));
						table_2.setVisible(false);
						panel_2.setLayout(new GridLayout(0, 1, 0, 0));
						
						JScrollPane scroll3 = new JScrollPane(table_2);
						scroll3.setViewportBorder(null);
						scroll3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
						panel_2.add(scroll3, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 248, 204);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(0, 0, 248, 204);
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));

		table = new JTable(tasks, 3);
		table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Task", "Time", "Precedences"},
			},
			new String[] {
				"Task", "Time", "Precedence"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(41);
		table.getColumnModel().getColumn(1).setPreferredWidth(47);
		table.getColumnModel().getColumn(2).setPreferredWidth(97);
		table.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(table);
		scroll.setViewportBorder(null);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_4.add(scroll, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(317, 0, 372, 400);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(0, 0, 380, 368);
		panel_1.add(panel_5);
		
				table_1 = new JTable();
				table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table_1.setToolTipText("");
				table_1.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Type", "Chromosome", "SI", "WS", "View"
					}
				) {
					Class[] columnTypes = new Class[] {
						String.class, Object.class, Object.class, Object.class, Object.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				table_1.getColumnModel().getColumn(0).setPreferredWidth(50);
				table_1.getColumnModel().getColumn(0).setMinWidth(50);
				table_1.getColumnModel().getColumn(1).setPreferredWidth(180);
				table_1.getColumnModel().getColumn(1).setMinWidth(37);
				table_1.getColumnModel().getColumn(2).setPreferredWidth(47);
				table_1.getColumnModel().getColumn(3).setPreferredWidth(38);
				table_1.setVisible(false);
				panel_5.setLayout(new GridLayout(0, 1, 0, 0));
				
				
				JScrollPane scroll2 = new JScrollPane(table_1);
				scroll2.setViewportBorder(null);
				scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				panel_5.add(scroll2);
				
				JLabel lblMinimumWorkstations = new JLabel("Minimum Workstations:");
				lblMinimumWorkstations.setBounds(32, 375, 209, 25);
				lblMinimumWorkstations.setVisible(false);
				panel_1.add(lblMinimumWorkstations);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		System.out.println("working");

		JDialog contentPanel = new JDialog();
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.getContentPane().setLayout(gbl_contentPanel);

		mntmGenerations.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				iterations.setVisible(true);
			}
		});

		iterations.addComponentListener(new ComponentAdapter(){
			public void componentHidden (ComponentEvent ce){
				generations = iterations.getIterations();
				childrenWindow.setVisible(true);
			}
		});

		childrenWindow.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent ce){
				childPercent = childrenWindow.getPercentage();
				mutationWindow.setVisible(true);
			}
		});

		mutationWindow.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent ce)
			{
				mutationPercent = mutationWindow.getPercentage();
				preservedWindow.setVisible(true);
			}
		});

		preservedWindow.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent ce){
				preservedPercent = preservedWindow.getPercentage();
				askCycle.setVisible(true);
			}
		});

		SaveAs save = new SaveAs();
		save.setVisible(false);

		mntmInitialPopulation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				chromos = null;
				table_1.setVisible(false);
				askCycle.setVisible(true);
			}
		});

		mntmOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//panel_4.add(table);
				table_1.setVisible(false);
				table_2.setVisible(false);
				table_1.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
							"Type", "Chromosome", "SI", "View"
						}
					) {
						Class[] columnTypes = new Class[] {
							String.class, Object.class, Object.class, Object.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});
				table_2.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
							"Station", "Options", "Assigned", "Time Left"
						}
					));
				table.setModel(new DefaultTableModel(
						new Object[][] {
								{"Task", "Time", "Precedences"},
						},
						new String[] {
								"Task", "Time", "Precedence"
						}
						) {
					Class[] columnTypes = new Class[] {
							String.class, Object.class, Object.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				open.setVisible(true);
				mntmGenerations.setEnabled(true);
				mntmInitialPopulation.setEnabled(true);
			}
		});

		askCycle.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent e){
				cycle = askCycle.getCycle();
				amount.setVisible(true);
			}
		});
		amount.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent ce){
				numberOfChromosomes = amount.getNumber();
				chromos = new Chromosome[numberOfChromosomes];
				for (int l = 0; l < generations; l++)
				{
					table_1.setModel(new DefaultTableModel(
							new Object[][] {
							},
							new String[] {
								"Type", "Chromosome", "SI", "WS", "View"
							}
						) {
							Class[] columnTypes = new Class[] {
								String.class, Object.class, Object.class, Object.class, Object.class
							};
							public Class getColumnClass(int columnIndex) {
								return columnTypes[columnIndex];
							}
						});
					for (int j = 0; j < numberOfChromosomes; j++)
					{
						if (file == null)
						{
							times = new int[tasks];
							precedences = new int[tasks][];
							for (int i = 0; i < tasks; i++)
							{
								times[i] = Integer.parseInt((String) table.getValueAt(i,  1));

								if (table.getValueAt(i,  2) != null && table.getValueAt(i,  2) != "")
								{
									String[] a = ((String)table.getValueAt(i,  2)).split(",");
									int[] vals = new int[a.length];
									for (int k = 0; k < a.length; k++)
									{
										vals[k] = Integer.parseInt(a[k]);
									}
									precedences[i] = vals;
								}
								else
								{
									int[] vals = {0};
									precedences[i] = vals;
								}
							}
							for (int m = 0; m < times.length; m++)
							{
								if (maxTime < times[m])
								{
									maxTime = times[m];
								}
							}
						}
						Chromosome chromo = null;
						int[] initialPop = null;
						if (l < 1)
						{
							code problem = new code(tasks);
							problem.setTasks(precedences,  times);
							initialPop = problem.initialPopulation();
							problem.setChromosomes(initialPop);
							chromo = problem.getChromosome();
							chromo.cycleTime = cycle;
							chromo.solution();
							chromos[j] = chromo;
						}
						Action solution = new AbstractAction()
						{
							public void actionPerformed(ActionEvent e)
							{
								table_2.setModel(new DefaultTableModel(
										new Object[][] {
										},
										new String[] {
											"Station", "Options", "Assigned", "Time Left"
										}
									));
								String[] toSet = chromos[Integer.valueOf(e.getActionCommand())].getSelected();
								DefaultTableModel dtm = (DefaultTableModel)table_2.getModel();
								for (int i = 0; i < toSet.length; i++)
								{
									String[] row = toSet[i].split("&");
									dtm.addRow((Object[])null);
									dtm.setValueAt(row[0], i, 0);
									dtm.setValueAt(row[1], i, 1);
									dtm.setValueAt(row[2], i, 2);
									dtm.setValueAt(row[3], i, 3);
								}
								table_2.setVisible(true);
							}
						};
						String type = "New";
						if (chromos[j].isChild)
						{
							type = "Child";
						}
						else if (chromos[j].isMutation)
						{
							type = "Mutation";
						}
						else if(chromos[j].isPreserved)
						{
							type = "Old (" + chromos[j].generation + ")";
						}
						DefaultTableModel tbl = (DefaultTableModel) table_1.getModel();
						tbl.addRow((Object[])null);
						tbl.setValueAt(type,  (j),  0);
						tbl.setValueAt(Arrays.toString(chromos[j].taskIndex), (j), 1);
						tbl.setValueAt("Solution", (j), 4);
						tbl.setValueAt(chromos[j].getSmoothness(),  (j),  2);
						tbl.setValueAt(chromos[j].WSTimes.size(), j, 3);
						Solution a = new Solution(table_1, solution, 4);
					}
					if ((l - 1) != generations && generations > 1)
					{
						chromos = setNextGeneration();
					}
				}
				table_1.repaint();
				table_1.setVisible(true);
				mntmNextGeneration.setEnabled(true);
				setMinNumOfWS();
				lblMinimumWorkstations.setText("Minimum Workstations: " + minWS);
				lblMinimumWorkstations.setVisible(true);
			}
		});
		open.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				System.out.println("working");
				tasks = open.getTasks();
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.removeRow(0);
				for (int i = 0; i < tasks; i++)
				{
					dtm.addRow((Object[])null);
					dtm.setValueAt((i+1), (i), 0);
				}
				table.setVisible(true);
				//table.repaint();
			}
		});
		mntmSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				save.setVisible(true);
				times = new int[tasks];
				precedences = new int[tasks][];
				for (int i = 0; i < tasks; i++)
				{
					times[i] = Integer.parseInt("" + (int)(table.getValueAt(i,1)));

					if (i > 0)
					{
						String[] a = ((String)table.getValueAt(i,  2)).split(",");
						int[] vals = new int[a.length];
						for (int k = 0; k < a.length; k++)
						{
							vals[k] = Integer.parseInt(a[k]);
						}
						precedences[i] = vals;
					}
				}
			}
		});
		save.addComponentListener(new ComponentAdapter(){
			public void componentHidden(ComponentEvent ce)
			{
				fileName = save.getName();
				PrintWriter write = null;
				try {
					write = new PrintWriter(fileName + ".lbp");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				write.println(Arrays.toString(times));
				for (int i = 1; i < times.length; i++)
				{
					write.println(Arrays.toString(precedences[i]));
				}
				write.close();
			}
		});
		mntmOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				mntmInitialPopulation.setEnabled(true);
				mntmGenerations.setEnabled(true);
				table.setModel(new DefaultTableModel(
						new Object[][] {
								{"Task", "Time", "Precedences"},
						},
						new String[] {
								"Task", "Time", "Precedence"
						}
						) {
					Class[] columnTypes = new Class[] {
							String.class, Object.class, Object.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Line Balancing Problem file", new String[] {"lbp"});
				fc.addChoosableFileFilter(filter);
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(frame);
				if (result == fc.APPROVE_OPTION)
				{
					file = fc.getSelectedFile();
					load();
				}
			}
		});
		
		mntmNextGeneration.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				if (childPercent == 0)
				{
					childrenWindow.setVisible(true);
				}
				else
				{
					chromos = setNextGeneration();
					for (int j = 0; j < numberOfChromosomes; j++)
					{
						Chromosome chromo = null;
						int[] initialPop = null;
						Action solution = new AbstractAction()
						{
							public void actionPerformed(ActionEvent e)
							{
								table_2.setModel(new DefaultTableModel(
										new Object[][] {
										},
										new String[] {
											"Station", "Options", "Assigned", "Time Left"
										}
									));
								String[] toSet = chromos[Integer.valueOf(e.getActionCommand())].getSelected();
								DefaultTableModel dtm = (DefaultTableModel)table_2.getModel();
								for (int i = 0; i < toSet.length; i++)
								{
									dtm.addRow((Object[])null);
									String[] row = toSet[i].split("&");
									dtm.setValueAt(row[0], i, 0);
									dtm.setValueAt(row[1], i, 1);
									dtm.setValueAt(row[2], i, 2);
									dtm.setValueAt(row[3], i, 3);
								}
								table_2.setVisible(true);
							}
						};
						String type = "New";
						if (chromos[j].isChild)
						{
							type = "Child";
						}
						else if (chromos[j].isMutation)
						{
							type = "Mutation";
						}
						else if(chromos[j].isPreserved)
						{
							type = "Old";
						}
						DefaultTableModel tbl = (DefaultTableModel) table_1.getModel();
						//tbl.addRow((Object[])null);
						tbl.setValueAt(type,  (j),  0);
						tbl.setValueAt(Arrays.toString(chromos[j].taskIndex), (j), 1);
						tbl.setValueAt("Solution", (j), 4);
						tbl.setValueAt(chromos[j].getSmoothness(),  (j),  2);
						tbl.setValueAt(chromos[j].WSTimes.size(), j, 3);
						Solution a = new Solution(table_1, solution, 4);
					}
				}
			}
		});

	}
}
