package Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Chromosome {
	int assigned;
	int Varassigned;
	String[] line;
	Task[] tasks;
	int[] taskIndex;
	int cycleTime;
	List<List<Integer>> solved;
	String[] toTable;
	List<Integer> WSTimes;
	double smoothness;
	boolean isChild;
	boolean isMutation;
	boolean isPreserved;
	long computationalTime;
	long startTime;
	int generation;
	double sum = 0.0;
	double devsum = 0.0;
	private static NormalDistribution d;
	double res = 0.0;
	double res2 = 0.0;
	double res3 = 0.0;

	Chromosome() {
	}

	Chromosome(Task[] tasks) {
		this.tasks = tasks;
		taskIndex = new int[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			taskIndex[i] = i + 1;
		}
	}

	Chromosome SetInitialTime(Chromosome chromo, long Initial) {
		chromo.startTime = Initial;
		return chromo;
	}
	
	void setGeneration(int gen) {
		this.generation = gen;
	}

	Chromosome mutate(long STime, double probability, int gen) throws MathException {
		// Adding Start Time to take computational time
		Chromosome mutated = new Chromosome();
		mutated.startTime = STime;
		//Make head of at least 2 genes
		int position = (int) (Math.random() * (tasks.length - 2)) + 2;
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
				//Revise, why the !contain check?
				if (candidate && !contains(selectedIndex, remainingIndex.get(i))) {
					candidates.add(remaining.get(i));
					remaining.remove(i);
					candidatesIndex.add(remainingIndex.get(i));
					remainingIndex.remove(i);
					to = remaining.size();
					//Used to be i=-1, but that re-checked previous tasks
					i = -1;
				}
			}
			//Randomly choose a candidate and pass it to select
			int rand = (int) (Math.random() * candidates.size());
			selected.add(candidates.get(rand));
			selectedIndex.add(candidatesIndex.get(rand));
			candidates.remove(rand);
			candidatesIndex.remove(rand);
			//Send the remaining candidates to remaining
			for (int l = 0; l < candidates.size(); l++) {
				remaining.add(candidates.get(l));
				remainingIndex.add(candidatesIndex.get(l));
			}
		}

		mutated.cycleTime = this.cycleTime;
		//Transfor List<> to Integer[]
		Integer[] b = new Integer[tasks.length];
		b = selectedIndex.toArray(b);
		//Transform Integer[] to int[]
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

	String[] getSelected() {
		return (toTable);
	}

	double getSmoothness() {
		return (smoothness);
	}

	float getCompTime() {
		return (computationalTime / 1000);
	}

	List<List<Integer>> getSolution() {
		return (solved);
	}

	Chromosome crossOver(Chromosome parent, double probability, long STime, int gen) throws MathException {

		int first = (int) (Math.random() * parent.tasks.length / 2 - 1) + 1;
		int last = (int) (Math.random() * parent.tasks.length / 2 - 1) + 1;
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

	boolean contains(final int[] array, final int v) { // checks if a number is contained in an array of numbers
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}

	boolean contains(final List<Integer> array, final int v) {// checks if a number is contained in an array of numbers
		for (final int e : array)
			if (e == v)
				return true;

		return false;
	}

	public void solution(double probability) throws MathException {
		WSTimes = new LinkedList<Integer>();
		toTable = new String[tasks.length];
		line = new String[tasks.length];
		String currLine;
		List<Integer> taskIn;
		int index = 1;
		int tableIndex = 0;
		int availableTime = cycleTime;
		int first = 0;
		int last = tasks.length - 1;
		int iterations = 0;
		int x = 0;
		float MeanTimeAssigned[] = new float[tasks.length];
		double VarAssigned[] = new double[tasks.length];
		int Ind[] = new int[tasks.length];
		List<List<Integer>> workstations = new LinkedList<List<Integer>>();
		taskIn = new LinkedList<Integer>();
		while (!(first > last) & (iterations < 1000)) {
			sum = 0;
			devsum = 0;
			for (x = 0; x < tasks.length; x++) {
				if (Ind[x] == index) {
					sum += MeanTimeAssigned[x];
					devsum += VarAssigned[x];
				}
			}
			iterations += 1;
			currLine = index + "&";
			d = new NormalDistributionImpl((sum + tasks[first].getTime()),
					(Math.sqrt(devsum + tasks[first].getStdDev())));
			res2 = d.cumulativeProbability(cycleTime);
			d = new NormalDistributionImpl((sum + tasks[last].getTime()),
					(Math.sqrt((devsum + tasks[last].getStdDev()))));
			res3 = d.cumulativeProbability(cycleTime);
			if (res2 >= probability && res3 >= probability) {
				// 1st option //stochastic
				currLine += taskIndex[first] + "," + taskIndex[last] + "&";
				Random fromFirst = new Random();
				if (fromFirst.nextBoolean()) {
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

	public void setSmoothness() {
		int maxTime = 0;
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
