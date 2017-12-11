package LBP.LBP;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

class Code {
	private Chromosome taskss;
	//chromosome containing tasks with precedence
	private Task[] tasks;
	//array of tasks

	Code(int numTasks)
	//create an array of the number of tasks specified
	{
		tasks = new Task[numTasks];
		taskss = new Chromosome();
	}

	Chromosome getChromosome()
	//return the chromosome
	{
		return (taskss);
	}

	void setTasks(int[][] precedences, int[] times)
	//having the precedence of each Task and the time, we store them in an array of tasks
	{
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new Task(precedences[i], times[i]);
		}
	}

	void setChromosomes(int[] population)
	//this rearranges the array of tasks according to some initial population
	{
		Task[] tmp = new Task[population.length];
		taskss.taskIndex = population;
		for (int i = 0; i < population.length; i++) {
			tmp[i] = tasks[population[i] - 1];

		}
		taskss.tasks = tmp;
	}

	int[] initialPopulation()
	//having the array of tasks, we can generate an initial population respecting the precedences
	{
		int[] initial = new int[tasks.length];
		List<Integer> noPrecedence = new LinkedList<Integer>();
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].getPrecedences() == null || tasks[i].getPrecedences()[0] == 0) {
				noPrecedence.add(i + 1);
			}
		}
		int first = (int) (Math.random() * noPrecedence.size());
		initial[0] = noPrecedence.get(first);
		noPrecedence.remove(first);
		for (int k = 1; k < initial.length; k++) {
			int[] candidates = new int[initial.length];
			int indexOfCandidates = 0;
			for (int i = 0; i < tasks.length; i++) {
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
			initial[k] = candidates[(int) (Math.random() * totalCandidates)];
		}
		return initial;
	}

	boolean contains(final int[] array, final int v)
	//checks if a number is contained in an array of numbers
	{
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}

}