package Test;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.BorderLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.CharacterData;

/**
 * A class to launch the program that solves Line Balancing Problems
 * <p>
 * It implements a graphical interface that shows input data and displays
 * solutions in the same frame.
 * 
 * @author Ulises Martinez
 */
public class Main {

	/**
	 * A variable to store and retrieve the problem's parameters
	 * 
	 * @see LBPParameters
	 */
	LBPParameters params;

	/**
	 * The program's main window. It contains {@link #table}, {@link #table_1},
	 * {@link #table_2}
	 */
	private JFrame frame;

	/**
	 * An int variable that stores the amount of tasks that has the problem
	 */
	private int tasks;

	/**
	 * A table in which the user inputs each task's data
	 */
	private JTable table;

	/**
	 * An ArrayList that stores the problem's tasks's data
	 */
	ArrayList<Task> taskArray;

	/**
	 * A File used to load/save data to/from the program
	 */
	File file;

	/**
	 * A table to display {@link #chromos}'s data
	 */
	private JTable table_1;

	/**
	 * A table to display a chromosome's solution to the Line Balancing Problem
	 */
	private JTable table_2;

	/**
	 * A Chromosome array thet stores the chromosomes generated by the programs last
	 * iteration
	 * 
	 * @see Chromosome
	 */
	private Chromosome[] chromos;
	
	/**
	 * An array storing all created chromosomes to avoid repetition
	 */
	private ArrayList<Chromosome> allChromos;

	/**
	 * A long variable that stores the solution's global start time used to
	 * calculate a chromosome's computational time
	 */
	private long STime;

	/**
	 * An int variable used to identify a chromosome's birth generation
	 */
	int generationCounter;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            - The programs starting arguments. Unused
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Main constructor, initializes the application.
	 */
	public Main() {
		params = new LBPParameters();
		params.setIterations(50);
		params.setProbability(0.95);
		initialize();
	}

	/**
	 * A method that solves the given Line Balancing Problem and displays it into a
	 * table
	 */
	void generateSolution() {
		
		int generations = params.getIterations();
		configureTables(1);

		for (int l = 0; l < generations; l++) {
			System.out.println(l);

			// if generation L is the first then initialize chromosomes
			if (l == 0) {
				STime = System.nanoTime();
				generationCounter = 1;
				for (int j = 0; j < params.getNumChromosomes(); j++) {
					int count = 0;
					do {
						chromos[j] = getNewChromosome();
						count++;
					} while( count < 100 && allChromos.contains( chromos[j] ) );
					allChromos.add(chromos[j]);
				}
			}

			// if generation L is NOT the first
			if (l != 0) {
				chromos = setNextGeneration();
			}
		}

		// Fill Table with chromosome data
		for (int j = 0; j < params.getNumChromosomes(); j++) {
			fillTableRow(j);
		}
	}
	
	/**
	 * A method to dump {@link #table}'s data into {@link #taskArray}
	 */
	void tableToArray () {
		taskArray = new ArrayList<Task>();

		// for Cycle for each Task
		for (int i = 0; i < tasks; i++) {
			
			Task temp = new Task(i);
			
			if( table.getValueAt(i, 1) instanceof Double) {
				temp.setTime( (Double) table.getValueAt(i, 1));
			} else {
				if ( table.getValueAt(i, 1) instanceof String) {
					temp.setTime( Double.parseDouble((String) table.getValueAt(i, 1)) );
				}
			}

			if( table.getValueAt(i, 2) instanceof Double) {
				temp.setStdDev( (Double) table.getValueAt(i, 2));
			} else {
				if ( table.getValueAt(i, 2) instanceof String) {
					temp.setStdDev( Double.parseDouble((String) table.getValueAt(i, 2)) );
				}
			}
			
			if (table.getValueAt(i, 3) != null && table.getValueAt(i, 3) != "") {
				// then create string array with precedence
				String[] a = ((String) table.getValueAt(i, 3)).split(",");
				// create integer array to storage each precedence
				int[] vals = new int[a.length];

				// For cycle to storage
				for (int k = 0; k < a.length; k++) {
					// storage in vals each precedence
					vals[k] = Integer.parseInt(a[k]);
					temp.setPrecedences(vals);
				}
			} else {
				// if it was not filled by user assign 0 to precedence
				int[] vals = { 0 };
				temp.setPrecedences(vals);
			}
			
			taskArray.add(temp);
			
		}
	}

	/**
	 * A method that creates a new Chromosome with birth generation equal to
	 * {@link #generationCounter}'s current value
	 * 
	 * @return - A new Chromosome for the given Line Balancing Problem
	 * @see Chromosome
	 */
	public Chromosome getNewChromosome() {

		Task[] tempArray = new Task[tasks];

		for (int i = 0; i < tasks; i++) {// for each Task
			tempArray[i] = taskArray.get(i);
		}

		Chromosome chromo = new Chromosome(tempArray);
		int[] initialPop = chromo.initialPopulation(STime);
		chromo.setChromosomes(initialPop);
		chromo.setCycleTime(params.getCycleTime());
		chromo.setGeneration(generationCounter);
		
		try {
			chromo.solution(params.getProbability());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return chromo;
	}

	/**
	 * A method that fills {@link #table_1}'s selected row
	 * 
	 * @param j
	 *            - The table's row to be filled
	 */
	void fillTableRow(int j) {
		Action solution = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				configureTables(2);
				String[] toSet = chromos[Integer.valueOf(e.getActionCommand())].getSelected();
				DefaultTableModel dtm = (DefaultTableModel) table_2.getModel();
				for (int i = 0; i < toSet.length; i++) {
					String[] row = toSet[i].split("&");
					dtm.addRow((Object[]) null);
					dtm.setValueAt(row[0], i, 0);
					dtm.setValueAt(row[1], i, 1);
					dtm.setValueAt(row[2], i, 2);
					dtm.setValueAt(row[3], i, 3);
				}
				table_2.setVisible(true);
			}
		};
		String type = "New (" + chromos[j].generation + ")";
		if (chromos[j].isChild) {
			type = "Child (" + chromos[j].generation + ")";
		} else if (chromos[j].isMutation) {
			type = "Mutation  (" + chromos[j].generation + ")";
		} else if (chromos[j].isPreserved) {
			type = "Old (" + chromos[j].generation + ")";
		}
		DefaultTableModel tbl = (DefaultTableModel) table_1.getModel();

		int rowCount = tbl.getRowCount();
		if (rowCount < j + 1) {
			tbl.addRow((Object[]) null);
		}
		tbl.setValueAt(type, (j), 0);
		tbl.setValueAt(Arrays.toString(chromos[j].taskIndex), (j), 1);
		tbl.setValueAt(chromos[j].getSmoothness(), (j), 2);
		tbl.setValueAt(chromos[j].WSTimes.size(), j, 3);
		tbl.setValueAt(chromos[j].computationalTime, (j), 4);
		new Solution(table_1, solution, 5);
	}

	/**
	 * A method to calculate {@link #chromos}' next generation and refresh
	 * {@link #table_1}
	 * 
	 * @deprecated - The program uses a fixed number of generations
	 */
	void nextGeneration() {
		try {
			chromos = setNextGeneration();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for (int j = 0; j < params.getNumChromosomes(); j++) {
			fillTableRow(j);
		}
	}

	/**
	 * A method to calculate the minimum number of workstations in a Line Balancing
	 * Problems
	 * 
	 * @return - The minimum number of workstations
	 */
	int getMinNumOfWS() {
		double sumTasks = 0;
		double cycle = params.getCycleTime();

		for (int i = 0; i < tasks; i++) {
			sumTasks +=taskArray.get(i).getTime();
		}

		int minWS = 0;

		if(sumTasks <= cycle) {
			minWS = 1;
		} else {
			if (sumTasks % cycle == 0) {
				minWS = (int) (sumTasks / cycle);
			} else {
				minWS = ((int) (sumTasks / cycle)) + 1;
			}
		}
		return minWS;
	}

	/**
	 * A method to check whether a given value exists within an array
	 * 
	 * @param array
	 *            - The array in which to look the value up
	 * @param v
	 *            - The value looked for
	 * @return - Returns true if the value is found; false otherwise.
	 */
	boolean contains(final int[] array, final int v) {
		// checks if a number is contained in an array of numbers
		for (final int e : array)
			if (e == v)
				return true;
		return false;
	}

	/**
	 * A method to obtain the best chromosomes contained in {@link #chromos}
	 * 
	 * @param amount
	 *            - The amount of chromosomes to obtain
	 * @return - A Chromosome array containing the selected chromosomes
	 */
	public Chromosome[] bestChromosomes(int amount) {
		int[] selected = new int[amount];
		Chromosome[] best = new Chromosome[amount];
		for (int i = 0; i < best.length; i++) {
			Chromosome bestSoFar = new Chromosome();
			int wasBest = 0;
			for (int j = 0; j < chromos.length; j++) {
				if (contains(selected, j)) {
					// If it's already selected, do nothing
				} else if (bestSoFar.WSTimes == null || chromos[j].WSTimes.size() < bestSoFar.WSTimes.size()) {
					bestSoFar = chromos[j];
					wasBest = j;
				} else if (chromos[j].WSTimes.size() == bestSoFar.WSTimes.size()) {
					if (chromos[j].smoothness < bestSoFar.smoothness) {
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

	/**
	 * A method to obtain {@link #chromos}' next generation
	 * 
	 * @return - A Chromosome array containing the next generation's chromosomes
	 */
	private Chromosome[] setNextGeneration() {
		generationCounter++;

		Chromosome[] newGeneration = new Chromosome[chromos.length];
		int mutatedAmount = (int) (params.getMutationsPercent() * chromos.length);
		int childAmount = amountOfChildren();
		int preservedAmount = (int) (params.getPreservedPercent() * chromos.length);
		int remaining = chromos.length - (mutatedAmount + childAmount + preservedAmount);
		Chromosome[] preserve = bestChromosomes(preservedAmount);
		for (int i = 0; i < preserve.length; i++) {
			preserve[i].isPreserved = true;
			preserve[i].isChild = false;
			preserve[i].isMutation = false;
			newGeneration[i] = preserve[i];
		}
		Chromosome[] mutated = bestChromosomes(mutatedAmount);
		for (int i = 0; i < mutated.length; i++) {
			int count = 0;
			do {
				newGeneration[i + preserve.length] = mutated[i].mutate(STime, params.getProbability(), generationCounter);
				count++;
			} while(count < 100 && allChromos.contains( newGeneration[i + preserve.length] ) );
			allChromos.add(newGeneration[i + preserve.length]);
		}
		Chromosome[] reproduction = bestChromosomes(childAmount);
		int ind = preserve.length + mutated.length;
		for (int i = 0; i < reproduction.length; i += 2) {

			/*
			do {
				newGeneration[ind] = reproduction[i].crossOver(reproduction[i + 1], params.getProbability(), STime, generationCounter);
			} while( allChromos.contains(newGeneration[ind] ) );
			allChromos.add(newGeneration[ind]);
			ind++;

			do {
				newGeneration[ind] = reproduction[i + 1].crossOver(reproduction[i], params.getProbability(), STime, generationCounter);
			} while( allChromos.contains( newGeneration[ind] ) );
			allChromos.add(newGeneration[ind]);
			ind++;
			*/
			
			newGeneration[ind] = reproduction[i].crossOver(reproduction[i + 1], params.getProbability(), STime, generationCounter);
			allChromos.add(newGeneration[ind]);
			ind++;

			newGeneration[ind] = reproduction[i + 1].crossOver(reproduction[i], params.getProbability(), STime, generationCounter);
			allChromos.add(newGeneration[ind]);
			ind++;
			
		}
		for (int i = 0; i < remaining; i++) {

			int count = 0;
			do {
				newGeneration[i + (chromos.length - remaining)] = getNewChromosome();
				count++;
			} while(count < 100 && allChromos.contains( newGeneration[i + (chromos.length - remaining)] ) );
			allChromos.add( newGeneration[i + (chromos.length - remaining)] );
		}
		return newGeneration;
	}

	/**
	 * A method to calculate the numeric value of children to obtain per generation
	 * based in its percentile value.
	 * 
	 * @return - An int value of children to create
	 */
	public int amountOfChildren() {
		double childPercent = params.getChildPercent();
		int childrenAmount = (int) (childPercent * chromos.length);
		if (childrenAmount % 2 == 0)
			return (childrenAmount);
		return (childrenAmount - 1);
	}

	/**
	 * A method to load information from a data file to the program and dump it into
	 * {@link #table}
	 */
	public void load() {
		if (file != null) {
			
			taskArray = new ArrayList<Task>();
			
			try {
				if (FilenameUtils.getExtension(file.getName()).equals("lbp")) {

					double[] toTime = null;
					double[] toStdev = null;
					int[][] toPrecedences = null;
					
					String sCurrentLine;
					BufferedReader br = new BufferedReader(new FileReader(file));
					sCurrentLine = br.readLine();
					sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
					sCurrentLine = sCurrentLine.replaceAll("\\s+", "");
					String[] textTime = sCurrentLine.split(",");
					toTime = new double[textTime.length];
					for (int i = 0; i < textTime.length; i++) {
						toTime[i] = Double.parseDouble(textTime[i]);
					}

					sCurrentLine = br.readLine();
					sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
					sCurrentLine = sCurrentLine.replaceAll("\\s+", "");
					textTime = sCurrentLine.split(",");
					toStdev = new double[textTime.length];
					for (int i = 0; i < textTime.length; i++) {
						toStdev[i] = Double.parseDouble(textTime[i]);
					}

					toPrecedences = new int[toTime.length][];
					toPrecedences[0] = null;
					int ind = 0;
					while ((sCurrentLine = br.readLine()) != null) {
						sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
						sCurrentLine = sCurrentLine.replaceAll("\\s+", "");
						String[] textPre = sCurrentLine.split(",");
						int[] curr = new int[textPre.length];
						for (int i = 0; i < textPre.length; i++) {
							if (textPre[i].equals("")) {
								curr[i] = 0;
							} else {
								curr[i] = Integer.parseInt(textPre[i]);
							}
						}
						toPrecedences[ind] = curr;
						ind++;
					}
					
					br.close();
					
					tasks = toTime.length;
					
					for(int i = 0; i< tasks; i++) {
						Task temp = new Task(i, toPrecedences[i], toTime[i], toStdev[i]);
						taskArray.add(temp);
					}
					
				} else if (FilenameUtils.getExtension(file.getName()).equals("graphml")) {
					// parse the graphml xml
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(file);

					HashMap<String, Task> tasksMap = new HashMap<String, Task>();

					// read the tasks and their time
					NodeList nodeList = doc.getElementsByTagName("node");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							int tasknum = 0;
							double tasktime = 0;
							double taskstd = 0;

							NodeList data = element.getElementsByTagName("data");
							for (int j = 0; j < data.getLength(); j++) {
								Node dataNode = data.item(j);
								if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
									Element dataElement = (Element) dataNode;
									// getting the ws time
									if (dataElement.getAttribute("key").equals("d2")) {
										CharacterData cd = (CharacterData) dataNode.getFirstChild();
										String[] csvData = cd.getData().split(",");
										tasktime = Double.parseDouble(csvData[0]);
										taskstd = Double.parseDouble(csvData[1]);
									} else if (dataElement.getAttribute("key").equals("d3")) {
										// getting the label (task number)
										NodeList nList = dataElement.getElementsByTagName("y:NodeLabel");
										Node label = nList.item(0);
										if (label.getNodeType() == Node.ELEMENT_NODE) {
											Element labelElement = (Element) label;
											if (labelElement.getFirstChild() instanceof CharacterData) {
												CharacterData cd = (CharacterData) labelElement.getFirstChild();
												tasknum = Integer.parseInt(cd.getData());
											}
										}
									}
								}
							}
							Task tmp = new Task(tasknum);
							tmp.setTime(tasktime);
							tmp.setStdDev(taskstd);
							tasksMap.put(element.getAttribute("id"), tmp);
						}
					}
					// read the precedences
					NodeList edges = doc.getElementsByTagName("edge");
					for (int i = 0; i < edges.getLength(); i++) {
						if (edges.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element edgeElem = (Element) edges.item(i);
							String target = edgeElem.getAttribute("target");
							String source = edgeElem.getAttribute("source");
							Task preTask = tasksMap.get(target);
							preTask.addPrecedence(tasksMap.get(source).getTaskNum());
						}
					}
					// save all times, std and precedences
					for (int i = 0; i < nodeList.getLength(); i++) {
						Task curr = null;
						for (Task j : tasksMap.values()) {
							if (j.getTaskNum() == i + 1) {
								curr = j;
								taskArray.add(curr);
								break;
							}
						}
						assert curr != null;
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}

			DefaultTableModel dtm = (DefaultTableModel) table.getModel();
			dtm.removeRow(0);
			for (int i = 0; i < tasks; i++) {
				dtm.addRow((Object[]) null);
				dtm.setValueAt((i + 1), (i), 0);
				dtm.setValueAt(taskArray.get(i).getTime(), (i), 1);
				dtm.setValueAt(taskArray.get(i).getStdDev(), (i), 2);
				String set = Arrays.toString(taskArray.get(i).getPrecedences());
				set = set.substring(1, set.length() - 1);
				set = set.replaceAll("\\s+", "");
				dtm.setValueAt(set, (i), 3);

			}
			table.setVisible(true);
			table.repaint();
		}
	}

	/**
	 * A method to configure a table to its default parameters
	 * 
	 * @param chosen
	 *            - The value of the table to configure, where 0 is {@link #table},
	 *            1 is {@link #table_1} and 2 is {@link #table_2}
	 */
	public void configureTables(int chosen) {

		if (chosen == 0) {
			table.setModel(new DefaultTableModel(new Object[][] { { "Task", " Time", "Variance", "Precedences" }, },
					new String[] { "Task", "Time", "Variance", "Precedence" }) {
				Class[] columnTypes = new Class[] { String.class, Object.class, Object.class, Object.class };

				@Override
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table.getColumnModel().getColumn(0).setPreferredWidth(60);
			table.getColumnModel().getColumn(0).setMinWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(60);
			table.getColumnModel().getColumn(1).setMinWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(80);
			table.getColumnModel().getColumn(2).setMinWidth(60);
			table.getColumnModel().getColumn(3).setPreferredWidth(95);
			table.getColumnModel().getColumn(3).setMinWidth(70);
		}

		if (chosen == 1) {
			table_1.setModel(new DefaultTableModel(new Object[][] {},
					new String[] { "Type", "Chromosome", "SI", "WS", "PCT", "View" }) {
				Class[] columnTypes = new Class[] { String.class, Object.class, Object.class, Object.class, long.class,
						Object.class };

				@Override
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table_1.getColumnModel().getColumn(0).setPreferredWidth(80);
			table_1.getColumnModel().getColumn(0).setMinWidth(40);
			table_1.getColumnModel().getColumn(1).setPreferredWidth(220);
			table_1.getColumnModel().getColumn(1).setMinWidth(85);
			table_1.getColumnModel().getColumn(2).setPreferredWidth(70);
			table_1.getColumnModel().getColumn(2).setMinWidth(35);
			table_1.getColumnModel().getColumn(3).setPreferredWidth(40);
			table_1.getColumnModel().getColumn(3).setMinWidth(35);
			table_1.getColumnModel().getColumn(4).setPreferredWidth(85);
			table_1.getColumnModel().getColumn(4).setMinWidth(70);
			table_1.getColumnModel().getColumn(5).setPreferredWidth(50);
			table_1.getColumnModel().getColumn(5).setMinWidth(35);
		}

		if (chosen == 2) {
			table_2.setModel(new DefaultTableModel(new Object[][] {},
					new String[] { "Station", "Options", "Assigned", "Time Left" }));

			table_2.getColumnModel().getColumn(0).setMinWidth(50);
			table_2.getColumnModel().getColumn(1).setMinWidth(50);
			table_2.getColumnModel().getColumn(2).setMinWidth(60);
			table_2.getColumnModel().getColumn(3).setMinWidth(60);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 867, 563);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize Dialogs

		final IntegerDialog askTasks = new IntegerDialog("Number of Tasks");
		askTasks.setVisible(false);

		final CycleTimeDialog askCycle = new CycleTimeDialog();
		askCycle.setVisible(false);

		// Create Menu

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		final JMenuItem mntmNew = new JMenuItem("New Problem");
		mnFile.add(mntmNew);

		final JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		final JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		final JMenuItem mntmSolution = new JMenuItem("Generate Solution");
		mntmSolution.setEnabled(false);
		menuBar.add(mntmSolution);

		frame.getContentPane().setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 250, 300, 250);
		frame.getContentPane().add(panel_2);

		table_2 = new JTable();
		configureTables(2);

		table_2.setVisible(false);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scroll3 = new JScrollPane(table_2);
		scroll3.setViewportBorder(null);
		scroll3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_2.add(scroll3, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 250);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(0, 0, 300, 250);
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));

		table = new JTable(tasks, 4);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		configureTables(0);
		table.setVisible(false);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setViewportBorder(null);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel_4.add(scroll, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(300, 0, 550, 500);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(0, 0, 550, 450);
		panel_1.add(panel_5);

		table_1 = new JTable();
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_1.setToolTipText("");
		configureTables(1);

		table_1.setVisible(false);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scroll2 = new JScrollPane(table_1);
		scroll2.setViewportBorder(null);
		scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel_5.add(scroll2);

		final JLabel lblMinimumWorkstations = new JLabel("Minimum Workstations:");
		lblMinimumWorkstations.setBounds(0, 450, 300, 50);
		lblMinimumWorkstations.setVisible(false);
		panel_1.add(lblMinimumWorkstations);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		JDialog contentPanel = new JDialog();
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.getContentPane().setLayout(gbl_contentPanel);

		// Set Listeners

		mntmSolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				table_1.setVisible(false);
				double high = 0;
				double res;
				
				tableToArray();
				for (int i = 0; i < tasks; i++) {
					res = taskArray.get(i).getMaxTime(params.getProbability());
						if (res > high) {
							high = res;
						}
				}
				params.setCycleTime(askCycle.showDialog(high));
				setParams(tasks);
				chromos = new Chromosome[params.getNumChromosomes()];
				allChromos = new  ArrayList<Chromosome>();
				generateSolution();
				table_1.repaint();
				table_1.setVisible(true);

				lblMinimumWorkstations.setText("The minimum number of workstations is: " + getMinNumOfWS());
				lblMinimumWorkstations.setVisible(true);

				frame.repaint();
				frame.setVisible(true);
			}
		});

		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				table_1.setVisible(false);
				table_2.setVisible(false);
				configureTables(0);
				configureTables(1);
				configureTables(2);
				tasks = askTasks.showDialog();
				mntmSolution.setEnabled(true);
			}
		});

		askTasks.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.removeRow(0);
				for (int i = 0; i < tasks; i++) {
					dtm.addRow((Object[]) null);
					dtm.setValueAt((i + 1), (i), 0);
				}
				table.setVisible(true);
			}
		});

		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				JFileChooser save = new JFileChooser();
				save.setDialogTitle("Guardar");
				save.setApproveButtonText("Guardar");

				int returnVal = save.showOpenDialog(null);
				PrintWriter write = null;

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = save.getSelectedFile();
					String filepath = file.toString();
					String[] sep = filepath.split(".lbp");
					if (sep.length != 1 || filepath == sep[0]) {
						filepath += ".lbp";
					}
					try {
						write = new PrintWriter(filepath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				
				tableToArray();

				double times[] = new double[tasks];
				double stdDev[] = new double[tasks];
				
				for(int i = 0; i < tasks; i++) {
					Task temp = taskArray.get(i);
					times[i] = temp.getTime();
					stdDev[i] = temp.getStdDev();
				}
				
				write.println(Arrays.toString(times));
				write.println(Arrays.toString(stdDev));
				
				for (int i = 0; i < tasks; i++) {
					write.println(Arrays.toString( taskArray.get(i).getPrecedences() ));
				}
				write.close();
			}
		});

		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Line Balancing Problem file",
						new String[] { "lbp", "graphml" });
				fc.addChoosableFileFilter(filter);
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					configureTables(0);
					load();
					mntmSolution.setEnabled(true);
				}
			}
		});

	}

	/**
	 * A method used for testing a single problem under different parameters.
	 * <p>
	 * It opens a "*.lbp" file, solves the problem under various parameters and
	 * stores the best solutions in a text file at the same location. It also takes
	 * a screenshot of the main window ath the end of each iteration using
	 * {@link #makeScreenshot(JFrame, String, String, String)}
	 * 
	 * @param filepath
	 *            - A String value that identifies the path to the problem's file.
	 *            It must end with an double inverted bar (\\). Example:
	 *            "C:\\Users\\default\\Desktop\\"
	 * @param filename
	 *            - A String value that identifies the problem's data file. It must
	 *            not include the extension. Example: "lbp_problem_1"
	 * @param time
	 *            - A double value that corresponds to the problem's cycle time
	 * @deprecated -Only developed for testing purposes.
	 */
	public void jumpstart(String filepath, String filename, double time) {

		double child[] = { 0.30, 0.20, 0.10, 0.30, 0.20, 0.10, 0.10, 0.20, 0.10, 0.20, 0.30, 0.20, 0.20, 0.20, 0.10,
				0.30, 0.10, 0.20, 0.20, 0.30, 0.30, 0.20, 0.30, 0.30, 0.20, 0.10, 0.20, 0.10, 0.30, 0.10, 0.20 };
		double mut[] = { 0.15, 0.10, 0.15, 0.15, 0.10, 0.15, 0.05, 0.10, 0.05, 0.10, 0.05, 0.10, 0.10, 0.10, 0.05, 0.05,
				0.15, 0.10, 0.05, 0.10, 0.05, 0.10, 0.15, 0.05, 0.15, 0.10, 0.10, 0.15, 0.15, 0.05, 0.10 };
		double pres[] = { 0.20, 0.15, 0.20, 0.20, 0.15, 0.20, 0.20, 0.15, 0.10, 0.15, 0.20, 0.10, 0.15, 0.15, 0.20,
				0.20, 0.10, 0.15, 0.15, 0.15, 0.10, 0.15, 0.10, 0.10, 0.15, 0.15, 0.15, 0.10, 0.10, 0.10, 0.20 };
		int chrm[] = { 20, 40, 20, 60, 40, 60, 60, 20, 20, 40, 20, 40, 60, 40, 20, 60, 60, 40, 40, 40, 60, 40, 20, 20,
				40, 40, 40, 20, 60, 60, 40 };

		PrintWriter write = null;
		try {
			write = new PrintWriter(filepath + filename + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		file = new File(filepath + filename + ".lbp");
		load();

		write.println("Problema: " + filename);

		params.setCycleTime(time);
		for (int j = 0; j < 31; j++) {

			try {

				params.setChildPercent(child[j]);
				params.setMutationsPercent(mut[j]);
				params.setPreservedPercent(pres[j]);
				params.setNumChromosomes(chrm[j]);

				chromos = new Chromosome[params.getNumChromosomes()];
				allChromos = new  ArrayList<Chromosome>();
				generateSolution();
				table_1.repaint();
				table_1.setVisible(true);

				frame.repaint();
				frame.setVisible(true);

				write.println(chromos[0].getCompTime());

				makeScreenshot(frame, filepath + "\\shots\\", filename, "" + (j + 1));
			} catch (Exception e) {
				write.println("error");
			}
		}
		write.println();
		write.close();
	}

	/**
	 * A method to calculate and set the optimal parameters to solve the given
	 * problem
	 * 
	 * @param task
	 *            - The amount of tasks un the problem
	 */
	public void setParams(int task) {
		int chromos = 1;
		double child = 1;
		double mut = 1;
		double pres = 1;
		double Zopt = Double.MAX_VALUE;
		double z;
		for (int c = 20; c <= 60; c++) {
			for (int h = 10; h <= 30; h++) {
				if ((h * (c / 100)) % 2 == 0) {
					for (int m = 5; m <= 15; m++) {
						if ((m * (c / 100)) % 1 == 0) {
							for (int p = 10; p <= 20; p++) {
								if ((p * (c / 100)) % 1 == 0) {
									z = -0.11450 + 0.005293 * c + 0.000738 * (h * (c / 100)) + 0.06750 * (m * (c / 100))
											+ 0.006830 * (p * (c / 100)) + 0.001332 * task - 0.000059 * c * c
											+ 0.001738 * (h * (c / 100)) * (h * (c / 100))
											- 0.004576 * (m * (c / 100)) * (m * (c / 100))
											- 0.002326 * (p * (c / 100)) * (p * (c / 100)) - 0.000034 * task * task
											- 0.000839 * c * (h * (c / 100))
											+ 0.000645 * (h * (c / 100)) * (m * (c / 100))
											+ 0.000254 * (h * (c / 100)) * (p * (c / 100))
											+ 0.003251 * (m * (c / 100)) * (p * (c / 100))
											- 0.000015 * (p * (c / 100)) * task
											- 0.000073 * (h * (c / 100)) * (h * (c / 100)) * (h * (c / 100))
											+ 0.000351 * (m * (c / 100)) * (m * (c / 100)) * (m * (c / 100))
											+ 0.000141 * (p * (c / 100)) * (p * (c / 100)) * (p * (c / 100))
											+ 0.000000 * task * task * task + 0.000011 * c * c * (h * (c / 100))
											- 0.000012 * c * (h * (c / 100)) * (m * (c / 100))
											+ 0.000010 * c * (m * (c / 100)) * (m * (c / 100))
											- 0.000055 * c * (m * (c / 100)) * (p * (c / 100))
											+ 0.000007 * (h * (c / 100)) * (m * (c / 100)) * (p * (c / 100))
											- 0.000016 * (h * (c / 100)) * (p * (c / 100)) * (p * (c / 100));
									if (z < Zopt) {
										chromos = c;
										child = h;
										mut = m;
										pres = p;
										Zopt = z;
									}

								}
							}
						}
					}
				}
			}
		}
		params.setChildPercent(child / 100);
		params.setMutationsPercent(mut / 100);
		params.setPreservedPercent(pres / 100);
		params.setNumChromosomes(chromos);
	}

	/**
	 * A method to take a screenshot of one of the program's frames and store it as
	 * a "*.png" file
	 * 
	 * @param argFrame
	 *            - The frame of which the screenshot will be made
	 * @param filepath
	 *            - A String value that identifies the path to the problem's file.
	 *            It must end with an double inverted bar (\\). Example:
	 *            "C:\\Users\\default\\Desktop\\screenshots\\". The filepath must
	 *            already exist; otherwise the screenshots will not be saved.
	 * @param filename
	 *            - The first part of the image's storage name. It identifies the
	 *            problem solved.
	 * @param identifier
	 *            - An extra identifier to distinguish different screenshots that
	 *            come from the same problem
	 */
	public static final void makeScreenshot(JFrame argFrame, String filename, String filepath, String identifier) {

		try {

			Rectangle rec = argFrame.getBounds();
			BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
			argFrame.paint(bufferedImage.getGraphics());

			ImageIO.write(bufferedImage, "png", new File(filepath + filename + "_" + identifier + ".png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}