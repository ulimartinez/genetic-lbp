package Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
public class Code {
	private Chromosome taskss;
	//chromosome containing tasks with precedence
	private Task[] tasks;
	//array of tasks
	Code() {
	}
	Code(int numTasks) {//create an array of the number of tasks specified
		tasks = new Task[numTasks];
		taskss = new Chromosome();
	}
	Chromosome getChromosome ()
	 {//return the chromosome
		return (taskss);
	}
	void setTasks(int[][] precedences, int[] times, double[] stdDev)
	 {//having the precedence of each Task and the time, we store them in an array of tasks
		
		for (int i = 0; i < tasks.length; i++)	 {//for each Task
			tasks[i] = new Task(precedences[i], times[i], stdDev[i]);	//Task equals new variable that contained precedence and time
			
		}
		
	}
	void setChromosomes(int[] population)
	 {//this rearranges the array of tasks according to some initial population
		Task[] tmp = new Task[population.length];
		taskss.taskIndex = population;
		for (int i = 0; i < population.length; i++) {
			tmp[i] = tasks[population[i] - 1];
			
		}
		taskss.tasks = tmp;
	}

	
	void setPrecedence ()
	 {//manually enter precedences
		Scanner in = new Scanner(System.in);
		System.out.println("Whats the time of Task 1?");
		tasks[0] = new Task(null, in.nextInt(), 0);
		for (int task = 1; task < tasks.length; task++) {
			float StdDev = in.nextFloat();
			System.out.println("Whats the time of Task " + (task + 1) + "?");
			int time = in.nextInt();
			System.out.println("How many precedences does Task " + (task + 1)  + " have?");
			int precedences = in.nextInt();
			if (precedences > 0) {
				int[] precedence = new int[precedences];
				for (int i = 0; i < precedences; i++) {
					System.out.println("Enter precedence of Task " + (task + 1));
					int enter = in.nextInt();
					if (enter == 0) {
						task++;
						break;
					}
					else if (enter <= tasks.length) {
						precedence[i] = enter;
					}
					else {
						System.out.println("Please enter a number between 1 and " + tasks.length);
					}
				}
				tasks[task] = new Task(precedence, time, StdDev);
				taskss = new Chromosome(tasks);
			}			
		}
	}
	int[] initialPopulation(long STime) {//having the array of tasks, we can generate an initial population respecting the precedences
		taskss.startTime = STime;
		int[] initial = new int[tasks.length];		//Create an integer array size of tasks
		List<Integer> noPrecedence = new LinkedList<Integer>();//Create an array that will store Task with no precedence
		for (int i = 0; i < tasks.length; i++)	 {//For each Task
			if (tasks[i].getPrecedences() == null || tasks[i].getPrecedences()[0] == 0)  {// if Task has not any precedence
				noPrecedence.add(i + 1);	//adds to list Task with no precedence recognized by index array
			}
		}
		int first = (int)(Math.random() * noPrecedence.size());	//choose random any Task with no precedence
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
			initial[k] = candidates[(int)(Math.random() * totalCandidates)]; //choose any random candidate if exist more than 1
		}
		taskss.computationalTime = System.nanoTime()-taskss.startTime;
		return initial;
	}
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
				initial[k] = candidates[(int)(Math.random() * totalCandidates)];
			}
			System.out.println(Arrays.toString(initial));
		}
	}
	boolean contains(final int[] array, final int v)  {//checks if a number is contained in an array of numbers
	    for (final int e : array)
	        if (e == v)
	            return true;

	    return false;
	}
	
}