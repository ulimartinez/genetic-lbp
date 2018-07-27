package Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * A class with methods used to initialize a new Chromosome
 * @author Ulises Martinez
 *
 */
public class Code {
	
	/**
	 * A Random variable used to get random numbers
	 */
	private Random generator = new Random(System.currentTimeMillis());
	
	/**
	 * A Chromosome variable containing tasks with precedence
	 * @see Chromosome
	 */
	private Chromosome chromosome;
	
	/**
	 * A Task array containing {@link #chromosome}'s tasks
	 * @see Task
	 */
	private Task[] tasks;
	
	/**
	 * An empty class constructor
	 */
	Code() {
	}
	
	/**
	 * A class constructor that initializes {@link #tasks} with a size of numTasks
	 * @param numTasks - The new size of {@link #tasks}
	 */
	Code(int numTasks) {//create an array of the number of tasks specified
		tasks = new Task[numTasks];
		chromosome = new Chromosome();
	}
	
	/**
	 * Getter method for the variable {@link #chromosome}
	 * @return - {@link #chromosome}
	 */
	Chromosome getChromosome ()
	 {//return the chromosome
		return (chromosome);
	}

	/**
	 * Setter  method for {@link #tasks} that initializes the array using the values of the given parameters
	 * @param precedences - A matrix containing all task precedences 
	 * @param times					- An array containing the tasks' times
	 * @param stdDev				- An array containing the tasks' standard deviations
	 */
	void setTasks(int[][] precedences, double[] times, double[] stdDev) {//having the precedence of each Task and the time, we store them in an array of tasks
		for (int i = 0; i < tasks.length; i++)	 {//for each Task
			tasks[i] = new Task((i+1),precedences[i], times[i], stdDev[i]);	//Task equals new variable that contained precedence and time
		}
	}
	
	/**
	 * Setter  method for {@link #chromosome} that initializes its tasks using the values of {@link #tasks} 
	 * in the order given by the values of the population parameter
	 * @param population - An array containing the order in which the chromosome's task should be assigned
	 */
	void setChromosomes(int[] population)
	 {//this rearranges the array of tasks according to some initial population
		Task[] tmp = new Task[population.length];
		chromosome.taskIndex = population;
		for (int i = 0; i < population.length; i++) {
			tmp[i] = tasks[population[i] - 1];
		}
		chromosome.tasks = tmp;
	}


	/**
	 * A method to initialize a chromosome respecting its task precedences
	 * @param STime - The starting time from which to calculate the task's computational time
	 * @return - Return the chromosome's population
	 */
	int[] initialPopulation(long STime) {//having the array of tasks, we can generate an initial population respecting the precedences
		chromosome.startTime = STime;
		int[] initial = new int[tasks.length];		//Create an integer array size of tasks
		List<Integer> noPrecedence = new LinkedList<Integer>();//Create an array that will store Task with no precedence
		for (int i = 0; i < tasks.length; i++)	 {//For each Task
			if (tasks[i].getPrecedences() == null || tasks[i].getPrecedences()[0] == 0)  {// if Task has not any precedence
				noPrecedence.add(i + 1);	//adds to list Task with no precedence recognized by index array
			}
		}
		int first = generator.nextInt(noPrecedence.size());	//choose random any Task with no precedence
		initial[0] = noPrecedence.get(first);	//
		noPrecedence.remove(first);		// Reinitialize list "no precedence"
		for (int k = 1; k < initial.length; k++) {// verify every gen (Task)
			int[] candidates = new int[initial.length];
			int indexOfCandidates = 0;
			for (int i = 0; i < tasks.length; i++) {
				int[] precedences = tasks[i].getPrecedences();
				boolean candidate = true;		// it is already a candidate until verify it
				for (int j = 0; j < precedences.length; j++) {
					if (!(contains(initial, precedences[j])))	 {//verify  the precedence necessary for Task evaluated
						candidate = false;  //if not has been assigned is not a candidate
						break;
					}
				}
				if (candidate && !contains(initial, i+1))		 {//if it's a candidate and not has been assigned,
					candidates[indexOfCandidates] = i + 1;		// it's assigned
					indexOfCandidates++;
				}
			}
			int totalCandidates = 0;
			for (int i = 0; i < candidates.length; i++) {
				if (candidates[i] == 0)   {// if there's any candidate
					totalCandidates = i;		//count them
					break;
				}
			}
			initial[k] = candidates[generator.nextInt(totalCandidates)]; //choose any random candidate if exist more than 1
		}
		chromosome.computationalTime = System.nanoTime()-chromosome.startTime;
		return initial;
	}
	
	/**
	 * A method to initializeand print a chromosome respecting its task precedences
	 * @param a - Number of time the precedences are initialized before having a final set
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
					if (candidate && !contains(initial, i+1)) {
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
	 * Checks whether a given value exists within an array
	 * @param array - The array in which to look the value up
	 * @param v         - The value looked for
	 * @return			   - Returns true if the value is found; false otherwise.
	 */
	boolean contains(final int[] array, final int v)  {//checks if a number is contained in an array of numbers
	    for (final int e : array)
	        if (e == v)
	            return true;

	    return false;
	}
	
}