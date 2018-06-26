package Test;

import java.awt.EventQueue;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.swing.ListSelectionModel;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.math.MathException;

public class Main {
    LBPParameters params;

    private JFrame frame;
    private ChildrenDialog childrenWindow;
	private int tasks;
	private JTable table;
	int[] times;
	int[][] precedences;
	double[] stdDev;
	private JTable table_1;
	private String fileName;
	JFileChooser fc;
	File file;
	private JTable table_2;
	private Chromosome[] chromos;
	private double maxTime = 0;
	private int minWS;
	private long STime;
	
	/**
	 * Launch the application.
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

	void generateSolution() {
	    int numberOfChromosomes = params.getNumChromosomes();
	    int generations = params.getIterations();
	    double probabilityP = params.getProbability();
	    int cycle = params.getCycleTime();
        for (int l = 0; l < generations; l++)	 {//For Cycle to run all generations L
            table_1.setModel(new DefaultTableModel(
                    new Object[][] {
                    },
                    new String[] {
                            "Type", "Chromosome", "SI", "WS","CPT", "View"
                    }
            ) {
                Class[] columnTypes = new Class[] {
                        String.class, Object.class, Object.class, Object.class, long.class
                };
                @Override
                public Class getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            }); // Create Table and design of it
            for (int j = 0; j < numberOfChromosomes; j++)	 {//	Generate # chromosomes for each generation
                if (file == null) {
                    times = new int[tasks];			//array to store time of each Task
                    precedences = new int[tasks][];	//array to store precedence of each Task
                    stdDev= new double [tasks];
                    for (int i = 0; i < tasks; i++)	 {//for Cycle for each Task
                        times[i] = Integer.parseInt((String) table.getValueAt(i,  1));  //fill array with time of each Task
                        stdDev[i] = Double.parseDouble((String)(table.getValueAt(i, 2)));
                        if (table.getValueAt(i,  3) != null && table.getValueAt(i,  3) != "")  {//if Task has precedence then
                            String[] a = ((String)table.getValueAt(i,  3)).split(",");	// create string array with precedence
                            int[] vals = new int[a.length];		//create integer array to storage each precedence
                            for (int k = 0; k < a.length; k++) {//For cycle to storage
                                vals[k] = Integer.parseInt(a[k]);		//storage in vals each precedence
                            }
                            precedences[i] = vals;		//fill precedence array with vals for each Task
                        }
                        else {
                            int[] vals = {0};		//if it was not filled by user assign 0 to precedence
                            precedences[i] = vals;
                        }
                    }  // when ends For cycle the data (times and precedences) it's already storaged
                    for (int m = 0; m < times.length; m++){//For cycle to choose Maximum Time
                        if (maxTime < times[m]) {//if maximum time registered is less than Time of Task scanned
                            maxTime = times[m];	// it will be new value until store Maximum Time
                        }
                    }  //ends For cycle to choice Maximum Time
                }
                Chromosome chromo = null;		//assigned null value to variable chromo (chromosome type)
                int[] initialPop = null;	//declare and assign null value to integer array InitialPop
                if (l < 1) {//if generation L is the first then
                    STime= System.nanoTime();
                    Code problem = new Code(tasks);   // declare a variable type Code called problem
                    problem.setTasks(precedences,  times, stdDev);  // assign info (precedence and time) in var problem
                    initialPop = problem.initialPopulation(STime);  //call to subroutine contained in Code class
                    problem.setChromosomes(initialPop);		//sets chromosomes of initial solution
                    chromo = problem.getChromosome(); //gets chromosomes of initial solution
                    chromo.cycleTime = cycle;
                    try {
                        chromo.solution(probabilityP);
                    } catch (MathException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    chromos[j] = chromo;

                }
                Action solution = new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        table_2.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                        "Station", "Options", "Assigned", "Time Left"
                                }
                        ));
                        String[] toSet = chromos[Integer.valueOf(e.getActionCommand())].getSelected();
                        DefaultTableModel dtm = (DefaultTableModel)table_2.getModel();
                        for (int i = 0; i < toSet.length; i++) {
                            String[] row = toSet[i].split("&");
                            dtm.addRow((Object[])null);
                            dtm.setValueAt(row[0], i, 0);
                            dtm.setValueAt(row[1], i, 1);
                            dtm.setValueAt(row[2], i, 2);/////.........
                            dtm.setValueAt(row[3], i, 3);
                        }
                        table_2.setVisible(true);
                    }
                };
                String type = "New";
                if (chromos[j].isChild) {
                    type = "Child";
                }
                else if (chromos[j].isMutation) {
                    type = "Mutation";
                }
                else if(chromos[j].isPreserved) {
                    type = "Old (" + chromos[j].generation + ")";
                }
                DefaultTableModel tbl = (DefaultTableModel) table_1.getModel();
                tbl.addRow((Object[])null);
                tbl.setValueAt(type,  (j),  0);
                tbl.setValueAt(Arrays.toString(chromos[j].taskIndex), (j), 1);
                tbl.setValueAt("Solution", (j), 4);
                tbl.setValueAt(chromos[j].getSmoothness(),  (j),  2);
                tbl.setValueAt(chromos[j].WSTimes.size(), j, 3);
                tbl.setValueAt(chromos[j].computationalTime,  (j),  4);
                new Solution(table_1, solution, 5);
            }
            if ((l - 1) != generations && generations > 1) {
                try {
                    chromos = setNextGeneration();
                } catch (MathException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    void nextGeneration() {
	    int numberOfChromosomes = params.getNumChromosomes();
	    double childPercent = params.getChildPercent();
        if (childPercent == 0) {
            childrenWindow.setVisible(true);
        }
        else {
            try {
                chromos = setNextGeneration();
            } catch (MathException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            for (int j = 0; j < numberOfChromosomes; j++) {
                Action solution = new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        table_2.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                        "Station", "Options", "Assigned", "Time Left"
                                }
                        ));
                        String[] toSet = chromos[Integer.valueOf(e.getActionCommand())].getSelected();
                        DefaultTableModel dtm = (DefaultTableModel)table_2.getModel();
                        for (int i = 0; i < toSet.length; i++) {
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
                if (chromos[j].isChild) {
                    type = "Child";
                }
                else if (chromos[j].isMutation) {
                    type = "Mutation";
                }
                else if(chromos[j].isPreserved) {
                    type = "Old";
                }
                DefaultTableModel tbl = (DefaultTableModel) table_1.getModel();
                //tbl.addRow((Object[])null);
                tbl.setValueAt(type,  (j),  0);
                tbl.setValueAt(Arrays.toString(chromos[j].taskIndex), (j), 1);
                tbl.setValueAt("Solution", (j), 4);
                tbl.setValueAt(chromos[j].getSmoothness(),  (j),  2);
                tbl.setValueAt(chromos[j].WSTimes.size(), j, 3);
                tbl.setValueAt((chromos[j].computationalTime),  (j),  4);
                new Solution(table_1, solution, 5);
            }
        }
    }
	void setMinNumOfWS(){
		int sumTasks = 0;
		int cycle = params.getCycleTime();
		for (int i = 0; i < chromos[0].tasks.length; i++) {
			sumTasks += chromos[0].tasks[i].getTime();
		}
		minWS = (sumTasks / cycle) + 1;
	}
	boolean contains(final int[] array, final int v) 
	 {//checks if a number is contained in an array of numbers
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}
	public Chromosome[] bestChromosomes(int amount) {
		int[] selected = new int[amount];
		Chromosome[] best = new Chromosome[amount];
		for (int i = 0; i < best.length; i++) {
			Chromosome bestSoFar = new Chromosome();
			int wasBest = 0;
			for (int j = 0; j < chromos.length; j++) {
				if (contains(selected, j)) {

				}
				else if (bestSoFar.WSTimes == null || chromos[j].WSTimes.size() < bestSoFar.WSTimes.size()) {
					bestSoFar = chromos[j];
					wasBest = j;
				}
				else if (chromos[j].WSTimes.size() == bestSoFar.WSTimes.size()) {
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
	private Chromosome[] setNextGeneration() throws MathException {
	    double mutationPercent = params.getMutationsPercent();
	    double preservedPercent = params.getPreservedPercent();
	    double probabilityP = params.getProbability();
	    int cycle = params.getCycleTime();
		
		Chromosome[] newGeneration = new Chromosome[chromos.length];
		int mutatedAmount = (int)(mutationPercent * chromos.length);
		int childAmount = amountOfChildren();
		int preservedAmount = (int)(preservedPercent * chromos.length);
		int remaining = chromos.length - (mutatedAmount + childAmount + preservedAmount);
		Chromosome[] preserve = bestChromosomes(preservedAmount);
		for (int i = 0; i < preserve.length; i++) {
			preserve[i].isPreserved = true;
			preserve[i].isChild = false;
			preserve[i].isMutation = false;
			preserve[i].generation++;
			newGeneration[i] = preserve[i];
		}
		Chromosome [] mutated = bestChromosomes(mutatedAmount);
		for (int i = 0; i < mutated.length; i++) {
			newGeneration[i + preserve.length] = mutated[i].mutate(STime,probabilityP);

		}
		Chromosome[] reproduction = bestChromosomes(childAmount);
		int ind = preserve.length + mutated.length;
		for (int i = 0; i < reproduction.length; i += 2) {
		
			newGeneration[ind] = reproduction[i].crossOver(reproduction[i + 1],probabilityP,STime);
			ind++;
			newGeneration[ind] = reproduction[i + 1].crossOver(reproduction[i],probabilityP,STime);
			ind++;
			
		}
		for (int i = 0; i < remaining; i++) {
			Code problem = new Code(tasks);
			problem.setTasks(precedences,  times, stdDev);
			int[] initialPop = problem.initialPopulation(STime);
			problem.setChromosomes(initialPop);
			Chromosome chromo = problem.getChromosome();
			chromo.cycleTime = cycle;
			chromo.solution(probabilityP);
			newGeneration[i + (preserve.length + reproduction.length + mutated.length)] = chromo; 
		}
	
		return newGeneration;
	}
	public int amountOfChildren() {
	    double childPercent = params.getChildPercent();
		int childrenAmount = (int) (childPercent * chromos.length);
		if (childrenAmount % 2 == 0)
			return (childrenAmount);
		return (childrenAmount - 1);
	}

	/**
	 * Create the application.
	 */
	public Main() {
		params = new LBPParameters();
		initialize();
	}
	public void load() {
		if (file != null) {
			try {
				int[] toTime = null;
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(file));
				sCurrentLine = br.readLine();
				sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
				sCurrentLine = sCurrentLine.replaceAll("\\s+","");
				String[] textTime = sCurrentLine.split(",");
				toTime = new int[textTime.length];
				for (int i = 0; i < textTime.length; i++) {
					toTime[i] = Integer.parseInt(textTime[i]);
				}
				
				times = toTime;
				for (int i = 0; i < times.length; i++) {
					if (maxTime < times[i]) {
						maxTime = times[i];
					}
				}
				
				double[] toStdev = null;
				sCurrentLine = br.readLine();
				sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
				sCurrentLine = sCurrentLine.replaceAll("\\s+","");
				textTime = sCurrentLine.split(",");
				toStdev = new double[textTime.length];
				for (int i = 0; i < textTime.length; i++) {
					toStdev[i] = Double.parseDouble(textTime[i]);
				}
				stdDev = toStdev;
			
				
				tasks = toTime.length;
				int[][] toPrecedences = new int[toTime.length][];
				toPrecedences[0] = null;
				int ind = 0;
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length() - 1);
					sCurrentLine = sCurrentLine.replaceAll("\\s+","");
					String[] textPre = sCurrentLine.split(",");
					int[] curr = new int[textPre.length];
					for (int i = 0; i < textPre.length; i++) {
						if (textPre[i].equals("")) {
							curr[i] = 0;
						}
						else {
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
			for (int i = 0; i < times.length; i++) {
				dtm.addRow((Object[])null);
				dtm.setValueAt((i+1), (i), 0);
				dtm.setValueAt(times[i], (i), 1);
				dtm.setValueAt(stdDev[i], (i), 2);
				String set = Arrays.toString(precedences[i]);
				set = set.substring(1, set.length() - 1);
				set = set.replaceAll("\\s+","");
				dtm.setValueAt(set, (i), 3);

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

		final TasksDialog askTasks = new TasksDialog();
		askTasks.setVisible(false);

		final CycleTimeDialog askCycle = new CycleTimeDialog();
		askCycle.setVisible(false);

		final ChromosomeDialog amountChromosomes = new ChromosomeDialog();
		amountChromosomes.setVisible(false);
		

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		final JMenuItem mntmOptions = new JMenuItem("New Problem");
		mnFile.add(mntmOptions);

		final JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		final JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		final IterationDialog iterations = new IterationDialog();
		iterations.setVisible(false);
		
		final ProbabilityDialog probabilityWindow= new ProbabilityDialog();
		probabilityWindow.setVisible(false);

		childrenWindow = new ChildrenDialog();
		childrenWindow.setVisible(false);

		final MutationsDialog mutationWindow = new MutationsDialog();
		mutationWindow.setVisible(false);

		final PreservedDialog preservedWindow = new PreservedDialog();
		preservedWindow.setVisible(false);
		
		final JMenuItem mntmInitialPopulation = new JMenuItem("Initial Population");
		mntmInitialPopulation.setEnabled(false);
		menuBar.add(mntmInitialPopulation);

		final JMenuItem mntmGenerations = new JMenuItem("Generations");
		mntmGenerations.setEnabled(false);
		menuBar.add(mntmGenerations);
		
		final JMenuItem mntmNextGeneration = new JMenuItem("Next Generation");
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

		table = new JTable(tasks, 4);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
			new Object[][] { {"Task", "Mean Time", "Variance", "Precedences"},
			},
			new String[] {
				"Task", "Mean Time", "Variance", "Precedence"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class
			};
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(21);
		table.getColumnModel().getColumn(1).setPreferredWidth(27);
		table.getColumnModel().getColumn(2).setPreferredWidth(97);
		table.getColumnModel().getColumn(3).setPreferredWidth(97);
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
				"Type", "Chromosome", "SI", "WS","PCT" ,"View"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class, long.class, Object.class
			};
			@Override
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

		final JLabel lblMinimumWorkstations = new JLabel("Minimum Workstations:");
		lblMinimumWorkstations.setBounds(32, 375, 209, 25);
		lblMinimumWorkstations.setVisible(false);
		panel_1.add(lblMinimumWorkstations);
		final JLabel lblComputationalTime = new JLabel("Computational Time:");
		 lblComputationalTime.setBounds(32, 575, 209, 25);
		lblComputationalTime.setVisible(false);
		panel_1.add(lblComputationalTime);
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
			public void actionPerformed(ActionEvent ae) {
				table_1.setVisible(false);
				params.setIterations(iterations.showDialog());
			}
		});

		iterations.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden (ComponentEvent ce){
				params.setChildPercent(childrenWindow.showDialog());
			}
		});

		childrenWindow.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent ce){
				params.setMutationsPercent(mutationWindow.showDialog());
			}
		});

		mutationWindow.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent ce) {
				params.setPreservedPercent(preservedWindow.showDialog());
			}
		});

		preservedWindow.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent ce){
				params.setProbability(probabilityWindow.showDialog());
			}
		});
		probabilityWindow.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent ce){
				params.setCycleTime(askCycle.showDialog());
			}
		});

		JFileChooser save = new JFileChooser();

		mntmInitialPopulation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				chromos = null;
				table_1.setVisible(false);
				params.setProbability(probabilityWindow.showDialog());
				
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
						@Override
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
								{"Task", "Mean Time", "Variance", "Precedences"},
						},
						new String[] {
								"Task", "Mean Time","Variance", "Precedence"
						}
						) {
					Class[] columnTypes = new Class[] {
							String.class, Object.class, Object.class, Object.class
					};
					@Override
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				askTasks.setVisible(true);
				mntmGenerations.setEnabled(true);
				mntmInitialPopulation.setEnabled(true);
			}
		});

		askCycle.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent e){
				params.setNumChromosomes(amountChromosomes.showDialog());
			}
		});
		amountChromosomes.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent ce){
				chromos = new Chromosome[params.getNumChromosomes()]; //generate array Chromos size # chromosomes required for user
                generateSolution();
				table_1.repaint();
				table_1.setVisible(true);
				mntmNextGeneration.setEnabled(true);
				setMinNumOfWS();
				lblMinimumWorkstations.setText("Minimum Workstations: " + minWS);
				lblMinimumWorkstations.setVisible(true);
				lblComputationalTime.setText("xxxxxx");
				lblComputationalTime.setVisible(true);
			}
		});
		askTasks.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				System.out.println("working");
				tasks = askTasks.getTasks();
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.removeRow(0);
				for (int i = 0; i < tasks; i++) {
					dtm.addRow((Object[])null);
					dtm.setValueAt((i+1), (i), 0);
				}
				table.setVisible(true);
				//table.repaint();
			}
		});
		mntmSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				int returnVal = save.showOpenDialog(null);
                PrintWriter write = null;

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = save.getSelectedFile();
                    String filepath = file.toString();
                    if(!filepath.contains(".lbp")){
                        filepath += ".lbp";
                    }
                    try {
                        write = new PrintWriter(filepath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
				}
				times = new int[tasks];
				precedences = new int[tasks][];
				stdDev = new double[tasks];
				for (int i = 0; i < tasks; i++) {
					times[i] = Integer.parseInt((String) (table.getValueAt(i, 1)));
					stdDev[i] = Double.parseDouble((String)(table.getValueAt(i, 2)));
					if (i >= 0) {
                        String precStr = (String) table.getValueAt(i, 3);
                        if(precStr != null && precStr.length() > 0){
                            String[] a = precStr.split(",");
                            int[] vals = new int[a.length];
                            for (int k = 0; k < a.length; k++)
                            {
                                vals[k] = Integer.parseInt(a[k]);
                            }
                            precedences[i] = vals;
                        } else {
                            precedences[i] = new int[0];
                        }
					}
				}
                write.println(Arrays.toString(times));
                write.println(Arrays.toString(stdDev));
                for (int i = 0; i < times.length; i++)
                {
                    write.println(Arrays.toString(precedences[i]));
                }
                write.close();
			}
		});
		mntmOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				mntmInitialPopulation.setEnabled(true);
				mntmGenerations.setEnabled(true);
				table.setModel(new DefaultTableModel(
						new Object[][] {
								{"Task", "Time", "Variance", "Precedences"},
						},
						new String[] {
								"Task", "Time", "Variance", "Precedence"
						}
						) {
					Class[] columnTypes = new Class[] {
							String.class, Object.class, Object.class, Object.class
					};
					@Override
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Line Balancing Problem file", new String[] {"lbp"});
				fc.addChoosableFileFilter(filter);
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					load();
				}
			}
		});
		
		mntmNextGeneration.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				nextGeneration();
			}
		});

	}
}
