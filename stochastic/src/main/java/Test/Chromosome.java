package Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * A class used to create instances of chromosomes to solve a Line Balancing
 * Problem
 * 
 * @author Ulises Martinez
 *
 */
public class Chromosome {

	/**
	 * A Random variable used to create random results
	 */
	private Random generator = new Random(System.currentTimeMillis());

	/**
	 * An array containing the chromosome's tasks
	 */
	protected Task[] tasks;

	/**
	 * An int array containing a numerical index for each task in the chromosome
	 */
	protected int[] taskIndex;

	/**
	 * A double variable containing the problem's cycle time
	 */
	double cycleTime;

	/**
	 * A List containing the tasks assigned to each workstation
	 */
	List<List<Integer>> solved;

	/**
	 * A String array containing the chromosome's coded solution
	 */
	String[] toTable;

	/**
	 * A List that stores the work time of each workstation
	 */
	List<Double> WSTimes;

	/**
	 * A double variable that stores the chromosome's smoothness value
	 */
	double smoothness;

	/**
	 * A boolean variable that stores whether the chromosome is a child of last
	 * generation's chromosomes or not
	 */
	boolean isChild;

	/**
	 * A boolean variable that stores whether the chromosome is a mutation of last
	 * generation's chromosome or not
	 */
	boolean isMutation;

	/**
	 * A boolean variable that stores whether the chromosome is preserved from the
	 * last generation or not
	 */
	boolean isPreserved;

	/**
	 * A long variable that stores the time it took to computate the chromosome
	 */
	long computationalTime;

	/**
	 * A long variable that stores the time at which the chromosome began its
	 * computation
	 */
	long startTime;

	/**
	 * An int variable that stores the chromosome's generation
	 */
	int generation;

	// Constructors

	/**
	 * An empty class constructor
	 */
	Chromosome() {
	}

	/**
	 * A class constructor that initializes {@link #tasks} and {@link #taskIndex}
	 * 
	 * @param tasks
	 *            - A Task array, containing the chromosomes' tasks
	 */
	Chromosome(Task[] tasks) {
		this.tasks = tasks;
		taskIndex = new int[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			taskIndex[i] = i + 1;
		}
	}
	
	

	@Override
	public boolean equals(Object obj) {
		return (Arrays.toString(this.taskIndex).equals(Arrays.toString( ((Chromosome)obj).taskIndex) ) );
	}

	/**
	 * Setter for the {@link #startTime} of a given chromosome
	 * 
	 * @param chromo
	 *            - The chromosome that will contain the new startTime
	 * @param Initial
	 *            - A long variable containing the startTime's value
	 * @return - The parameter's chromosome after setting it's startTime
	 */
	Chromosome SetInitialTime(Chromosome chromo, long Initial) {
		chromo.startTime = Initial;
		return chromo;
	}

	/**
	 * Setter for the variable {@link #generation}
	 * 
	 * @param gen
	 *            - The numeric value that identifies the chromosome's generation
	 */
	void setGeneration(int gen) {
		this.generation = gen;
	}

	/**
	 * Setter method that initializes the chromosome's tasks using the values of
	 * {@link #tasks} in the order given by the values of the population parameter
	 * 
	 * @param population
	 *            - An array containing the order in which the chromosome's task
	 *            should be assigned
	 */
	void setChromosomes(int[] population) {
		// this rearranges the array of tasks according to some initial population
		Task[] tmp = new Task[population.length];
		this.taskIndex = population;
		for (int i = 0; i < population.length; i++) {
			tmp[i] = tasks[population[i] - 1];
		}
		this.tasks = tmp;
	}

	/**
	 * A method to initialize a chromosome respecting its task precedences
	 * 
	 * @param STime
	 *            - The starting time from which to calculate the task's
	 *            computational time
	 * @return - An int array containing the chromosome's population
	 */
	int[] initialPopulation(long STime) {// having the array of tasks, we can generate an initial population respecting
											// the precedences
		this.startTime = STime;
		int[] initial = new int[tasks.length]; // Create an integer array size of tasks
		List<Integer> noPrecedence = new LinkedList<Integer>();// Create an array that will store Task with no
																// precedence
		for (int i = 0; i < tasks.length; i++) {// For each Task
			if (tasks[i].getPrecedences() == null || tasks[i].getPrecedences()[0] == 0) {// if Task has not any
																							// precedence
				noPrecedence.add(i + 1); // adds to list Task with no precedence recognized by index array
			}
		}
		int first = generator.nextInt(noPrecedence.size()); // choose random any Task with no precedence
		initial[0] = noPrecedence.get(first); //
		noPrecedence.remove(first); // Reinitialize list "no precedence"
		for (int k = 1; k < initial.length; k++) {// verify every gen (Task)
			int[] candidates = new int[initial.length];
			int indexOfCandidates = 0;
			for (int i = 0; i < tasks.length; i++) {
				int[] precedences = tasks[i].getPrecedences();
				boolean candidate = true; // it is already a candidate until verify it
				for (int j = 0; j < precedences.length; j++) {
					if (!(contains(initial, precedences[j]))) {// verify the precedence necessary for Task evaluated
						candidate = false; // if not has been assigned is not a candidate
						break;
					}
				}
				if (candidate && !contains(initial, i + 1)) {// if it's a candidate and not has been assigned,
					candidates[indexOfCandidates] = i + 1; // it's assigned
					indexOfCandidates++;
				}
			}
			int totalCandidates = 0;
			for (int i = 0; i < candidates.length; i++) {
				if (candidates[i] == 0) {// if there's any candidate
					totalCandidates = i; // count them
					break;
				}
			}
			initial[k] = candidates[generator.nextInt(totalCandidates)]; // choose any random candidate if exist more
																			// than 1
		}
		this.computationalTime = System.nanoTime() - this.startTime;
		return initial;
	}

	/**
	 * A method to initialize and print a chromosome respecting its task precedences
	 * 
	 * @param a
	 *            - Number of time the precedences are initialized before having a
	 *            final set
	 * @deprecated - replaced by {@link #initialPopulation(long)}
	 */
	void initialPopulation(int a) {
		for (int l = 0; l < a; l++) {
			int[] initial = new int[tasks.length];
			initial[0] = 1;
			for (int k = 1; k < initial.length; k++) {
				int[] candidates = new int[initial.length];
				int indexOfCandidates = 0;
				for (int i = 1; i < tasks.length; i++) {
					int[] precedences = tasks[i].getPrecedences();
					boolean candidate = true;
					for (int j = 0; j < precedences.length; j++) {
						if (!(contains(initial, precedences[j]))) {
							candidate = false;
							break;
						}
					}
					if (candidate && !contains(initial, i + 1)) {
						candidates[indexOfCandidates] = i + 1;
						indexOfCandidates++;
					}
				}
				int totalCandidates = 0;
				for (int i = 0; i < candidates.length; i++) {
					if (candidates[i] == 0) {
						totalCandidates = i;
						break;
					}
				}
				initial[k] = candidates[generator.nextInt(totalCandidates)];
			}
			System.out.println(Arrays.toString(initial));
		}
	}

	/**
	 * A method to mutate the chromosome into a new one
	 * 
	 * @param STime
	 *            - A long value representing the time at which computation began
	 * @param probability
	 *            - A double value that represents the experiment's confidence level
	 * @param gen
	 *            - The numeric value that identifies the chromosome's generation
	 * @return - The mutated chromosome
	 */
	Chromosome mutate(long STime, double probability, int gen)  {
		// Adding Start Time to take computational time
		Chromosome mutated = new Chromosome();
		mutated.startTime = STime;
		// Make head of at least 2 genes
		int position = generator.nextInt(tasks.length - 2) + 2;
		List<Task> selected = new LinkedList<Task>();
		List<Task> remaining = new LinkedList<Task>();
		List<Integer> selectedIndex = new LinkedList<Integer>();
		List<Integer> remainingIndex = new LinkedList<Integer>();
		for (int i = 0; i < tasks.length; i++) {
			if (i < position) {
				selected.add(tasks[i]);
				selectedIndex.add(taskIndex[i]);
			} else {
				remaining.add(tasks[i]);
				remainingIndex.add(taskIndex[i]);
			}
		}
		while (remaining.size() > 0) {
			List<Task> candidates = new LinkedList<Task>();
			List<Integer> candidatesIndex = new LinkedList<Integer>();
			int to = remaining.size();
			for (int i = 0; i < to; i++) {
				boolean candidate = true;
				for (int j = 0; j < remaining.get(i).getPrecedences().length; j++) {
					if (!contains(selectedIndex, remaining.get(i).getPrecedences()[j])) {
						if (remaining.get(i).getPrecedences()[j] != 0) {
							candidate = false;
							break;
						}
					}
				}
				// Revise, why the !contain check?
				if (candidate && !contains(selectedIndex, remainingIndex.get(i))) {
					candidates.add(remaining.get(i));
					remaining.remove(i);
					candidatesIndex.add(remainingIndex.get(i));
					remainingIndex.remove(i);
					to = remaining.size();
					// Used to be i=-1, but that re-checked previous tasks
					i = -1;
				}
			}
			// Randomly choose a candidate and pass it to select
			int rand = generator.nextInt(candidates.size());
			selected.add(candidates.get(rand));
			selectedIndex.add(candidatesIndex.get(rand));
			candidates.remove(rand);
			candidatesIndex.remove(rand);
			// Send the remaining candidates to remaining
			for (int l = 0; l < candidates.size(); l++) {
				remaining.add(candidates.get(l));
				remainingIndex.add(candidatesIndex.get(l));
			}
		}

		mutated.cycleTime = this.cycleTime;
		// Transfor List<> to Integer[]
		Integer[] b = new Integer[tasks.length];
		b = selectedIndex.toArray(b);
		// Transform Integer[] to int[]
		int[] ind = new int[b.length];
		int track = 0;
		for (int e : b) {
			ind[track] = e;
			track++;
		}
		mutated.taskIndex = ind;
		Task[] a = new Task[tasks.length];
		mutated.tasks = selected.toArray(a);
		mutated.solution(probability);
		mutated.isMutation = true;
		mutated.isChild = false;
		mutated.isPreserved = false;
		mutated.computationalTime = System.nanoTime() - mutated.startTime;
		mutated.generation = gen;
		return mutated;
	}

	/**
	 * Getter that returns a String array containing the chromosome's coded solution
	 * 
	 * @return - {@link #toTable}
	 */
	String[] getSelected() {
		return (toTable);
	}

	/**
	 * Getter for the chromosome's smoothness value
	 * 
	 * @return - {@link #smoothness}
	 */
	double getSmoothness() {
		return (smoothness);
	}

	/**
	 * Getter for the chromosome's total computational time
	 * 
	 * @return {@link #computationalTime}
	 */
	long getCompTime() {
		return computationalTime;
	}

	/**
	 * Getter that returns a List containing the tasks assigned to each workstation
	 * 
	 * @return - {@link #solved}
	 */
	List<List<Integer>> getSolution() {
		return (solved);
	}
	
	/**
	 * Setter for {@link #cycleTime}
	 * @param time	- The new value of cycleTime
	 */
	void setCycleTime(double time) {
		this.cycleTime = time;
	}

	/**
	 * A method to cross the chromosome with another to create a child
	 * 
	 * @param parent
	 *            - The chromosome with which the cross-over is realized
	 * @param probability
	 *            - A double value that represents the experiment's confidence level
	 * @param STime
	 *            - A long value representing the time at which computation began
	 * @param gen
	 *            - The numeric value that identifies the chromosome's generation
	 * @return - The child chromosome produced by the cross-over
	 */
	Chromosome crossOver(Chromosome parent, double probability, long STime, int gen)  {

		int first = generator.nextInt(parent.tasks.length / 2 - 1) + 1;
		int last = generator.nextInt(parent.tasks.length / 2 - 1) + 1;
		Chromosome newChild = new Chromosome();
		newChild.startTime = STime;
		if (parent.tasks.length == this.tasks.length) {
			Task[] child = tasks;
			Task[] childBody = new Task[child.length - (first + last)];
			Task[] newChildBody = new Task[childBody.length];
			int[] childIndex = taskIndex;
			int[] body = new int[tasks.length - (first + last)];
			int[] newBody = new int[tasks.length - (first + last)];
			int newInd = 0;
			
			for (int i = 0; i < body.length; i++) {
				body[i] = taskIndex[i + first];
				childBody[i] = tasks[i + first];
			}
			
			for (int i = 0; i < parent.taskIndex.length; i++) {
				if (contains(body, parent.taskIndex[i])) {
					newBody[newInd] = parent.taskIndex[i];
					newChildBody[newInd] = parent.tasks[i];
					newInd++;
				}
			}
			
			for (int i = first; i < child.length - last; i++) {
				child[i] = newChildBody[i - first];
				childIndex[i] = newBody[i - first];
			}
			
			newChild.tasks = child;
			newChild.taskIndex = childIndex;
			newChild.cycleTime = this.cycleTime;
			newChild.solution(probability);
			newChild.isChild = true;
			newChild.isMutation = false;
			newChild.isPreserved = false;
			newChild.generation = gen;

		}
		newChild.computationalTime = System.nanoTime() - newChild.startTime;
		return (newChild);
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
	 * A method to check whether a given value exists within a List
	 * 
	 * @param array
	 *            - The List in which to look the value up
	 * @param v
	 *            - The value looked for
	 * @return - Returns true if the value is found; false otherwise.
	 */
	boolean contains(final List<Integer> array, final int v) {
		// checks if a number is contained in an array of numbers
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}

	/**
	 * A method to calculate the chromosme's solution to the LBP
	 * 
	 * @param probability
	 *            - A double value that represents the experiment's confidence level
	 */
	public void solution(double probability)  {
		WSTimes = new LinkedList<Double>();
		toTable = new String[tasks.length];
		String currLine;
		List<Integer> taskIn;
		int index = 1;
		int tableIndex = 0;
		double availableTime = cycleTime;
		int first = 0;
		int last = tasks.length - 1;
		int iterations = 0;
		int x = 0;
		double MeanTimeAssigned[] = new double[tasks.length];
		double VarAssigned[] = new double[tasks.length];
		int Ind[] = new int[tasks.length];
		List<List<Integer>> workstations = new LinkedList<List<Integer>>();
		taskIn = new LinkedList<Integer>();

		NormalDistribution d;
		while (!(first > last) & (iterations < 1000)) {
			double sum = 0;
			double devsum = 0;
			for (x = 0; x < tasks.length; x++) {
				if (Ind[x] == index) {
					sum += MeanTimeAssigned[x];
					devsum += VarAssigned[x];
				}
			}
			iterations += 1;
			currLine = index + "&";
			

			double res2 = 0;
			double res3 = 0;
			
			try {

				d = new NormalDistributionImpl((sum + tasks[first].getTime()),
						(Math.sqrt(devsum + tasks[first].getStdDev())));
				res2 = d.cumulativeProbability(cycleTime);
			}
			catch (Exception e) {
				//do nothing, res2 = 0
			}
			
			try {
				d = new NormalDistributionImpl((sum + tasks[last].getTime()),
						(Math.sqrt((devsum + tasks[last].getStdDev()))));
				res3 = d.cumulativeProbability(cycleTime);
			}
			catch (Exception e) {
				//do nothing, res3 = 0
			}
			
			if (res2 >= probability && res3 >= probability) {
				// 1st option //stochastic
				currLine += taskIndex[first] + "," + taskIndex[last] + "&";
				if (generator.nextBoolean()) {
					currLine += taskIndex[first] + "&";
					availableTime -= tasks[first].getTime();
					MeanTimeAssigned[tableIndex] = tasks[first].getTime();
					VarAssigned[tableIndex] = tasks[first].getStdDev();
					Ind[tableIndex] = index;
					currLine += availableTime;
					taskIn.add(taskIndex[first]);

					toTable[tableIndex] = currLine;

					tableIndex++;
					first++;
				} else {
					currLine += taskIndex[last] + "&";
					availableTime -= tasks[last].getTime();
					MeanTimeAssigned[tableIndex] = tasks[last].getTime();
					VarAssigned[tableIndex] = tasks[last].getStdDev();
					Ind[tableIndex] = index;
					currLine += availableTime;
					taskIn.add(taskIndex[last]);
					toTable[tableIndex] = currLine;
					tableIndex++;
					last--;
				}
			}

			else if (res2 >= probability) {// op 2
				currLine += taskIndex[first] + "&" + taskIndex[first] + "&";
				availableTime -= tasks[first].getTime();
				MeanTimeAssigned[tableIndex] = tasks[first].getTime();
				VarAssigned[tableIndex] = tasks[first].getStdDev();
				Ind[tableIndex] = index;
				currLine += availableTime;
				taskIn.add(taskIndex[first]);
				toTable[tableIndex] = currLine;
				tableIndex++;
				first++;
			} else if (res3 >= probability) {// op 3
				availableTime -= tasks[last].getTime();
				MeanTimeAssigned[tableIndex] = tasks[last].getTime();
				VarAssigned[tableIndex] = tasks[last].getStdDev();
				Ind[tableIndex] = index;
				currLine += taskIndex[last] + "&" + taskIndex[last] + "&" + availableTime;
				taskIn.add(taskIndex[last]);
				toTable[tableIndex] = currLine;
				tableIndex++;
				last--;
			} else {
				WSTimes.add(cycleTime - availableTime);
				workstations.add(taskIn);
				index++;
				taskIn = new LinkedList<Integer>();
				availableTime = cycleTime;
			}
		}
		workstations.add(taskIn);
		WSTimes.add(cycleTime - availableTime);
		index++;

		solved = workstations;
		setSmoothness();
	}

	/**
	 * A method to calculate and store the chromosome's smoothness value
	 * 
	 * @see #smoothness
	 */
	public void setSmoothness() {
		double maxTime = 0;
		for (int i = 0; i < WSTimes.size(); i++) {
			if (WSTimes.get(i) > maxTime) {
				maxTime = WSTimes.get(i);
			}
		}
		double sum = 0;
		for (int i = 0; i < WSTimes.size(); i++) {
			sum += (Math.pow((maxTime - WSTimes.get(i)), 2)) / WSTimes.size();
		}
		smoothness = Math.sqrt(sum);
	}
}
