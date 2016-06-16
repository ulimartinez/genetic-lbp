package Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Chromosome {
	task[] tasks;
	int[] taskIndex;
	int cycleTime;
	List<List<Integer>> solved;
	String[] toTable;
	List<Integer> WSTimes;
	double smoothness;
	boolean isChild;
	boolean isMutation;
	boolean isPreserved;
	int generation = 1;
	Chromosome()
	{
		
	}
	Chromosome(task[] tasks)
	{
		this.tasks = tasks;
		taskIndex = new int[tasks.length];
		for (int i = 0; i < tasks.length; i++)
		{
			taskIndex[i] = i + 1;
		}
	}
	Chromosome mutate()
	{
		int position = (int)(Math.random() * (tasks.length - 2)) + 2;
		List<task> selected = new LinkedList<task>();
		List<task> remaining = new LinkedList<task>();
		List<Integer> selectedIndex = new LinkedList<Integer>();
		List<Integer> remainingIndex = new LinkedList<Integer>();
		for (int i = 0; i < tasks.length; i++)
		{
			if (i < position)
			{
				selected.add(tasks[i]);
				selectedIndex.add(taskIndex[i]);
			}
			else
			{
				remaining.add(tasks[i]);
				remainingIndex.add(taskIndex[i]);
			}
		}
		while (remaining.size() > 0)
		{
			List<task> candidates = new LinkedList<task>();
			List<Integer> candidatesIndex = new LinkedList<Integer>();
			int to = remaining.size();
			for (int i = 0; i < to; i++)
			{
				boolean candidate = true;
				for (int j = 0; j < remaining.get(i).getPrecedences().length; j++)
				{
					if (!contains(selectedIndex, remaining.get(i).getPrecedences()[j]))
					{
						if (remaining.get(i).getPrecedences()[j] != 0)
						{
							candidate = false;
							break;
						}
					}
				}
				if (candidate && !contains(selectedIndex, remainingIndex.get(i)))
				{
					candidates.add(remaining.get(i));
					remaining.remove(i);
					candidatesIndex.add(remainingIndex.get(i));
					remainingIndex.remove(i);
					to = remaining.size();
					i = -1;
				}
			}
			int rand = (int)(Math.random() * candidates.size());
			selected.add(candidates.get(rand));
			selectedIndex.add(candidatesIndex.get(rand));
			candidates.remove(rand);
			candidatesIndex.remove(rand);
			for (int l = 0; l < candidates.size(); l++)
			{
				remaining.add(candidates.get(l));
				remainingIndex.add(candidatesIndex.get(l));
			}
		}
		Chromosome mutated = new Chromosome();
		mutated.cycleTime = this.cycleTime;
		Integer[] b = new Integer[tasks.length];
		b = selectedIndex.toArray(b);
		int[] ind = new int[b.length];
		int track = 0;
		for (int e : b)
		{
			ind[track] = e;
			track++;
		}
		mutated.taskIndex = ind;
		task[] a = new task[tasks.length];
		mutated.tasks = selected.toArray(a);
		mutated.solution();
		mutated.isMutation = true;
		mutated.isChild = false;
		mutated.isPreserved = false;
		mutated.generation = generation;
		return mutated;
	}
	String[] getSelected()
	{
		return(toTable);
	}
	double getSmoothness()
	{
		return (smoothness);
	}
	List<List<Integer>> getSolution()
	{
		return (solved);
	}
	Chromosome crossOver (Chromosome parent)
	{
		int first = (int)(Math.random() * parent.tasks.length/2 - 1) + 1;
		int last = (int)(Math.random() * parent.tasks.length/2 - 1) + 1;
		Chromosome newChild = new Chromosome();
		if (parent.tasks.length == this.tasks.length)
		{
			task[] child = tasks;
			task[] childBody = new task[child.length - (first + last)];
			task[] newChildBody = new task[childBody.length];
			int[] childIndex = taskIndex;
			int[] body = new int[tasks.length - (first + last)];
			int[] newBody = new int[tasks.length - (first + last)];
			int newInd = 0;
			for (int i = 0; i < body.length; i++)
			{
				body[i] = taskIndex[i + first];
				childBody[i] = tasks[i + first];
			}
			for (int i = 0; i < parent.taskIndex.length; i++)
			{
				if(contains(body, parent.taskIndex[i]))
				{
					newBody[newInd] = parent.taskIndex[i];
					newChildBody[newInd] = parent.tasks[i];
					newInd++;
				}
			}
			for (int i = first; i < child.length - last; i++)
			{
				child[i] = newChildBody[i - first];
				childIndex[i] = newBody[i - first];
			}
			newChild.tasks = child;
			newChild.taskIndex = childIndex;
			newChild.cycleTime = this.cycleTime;
			newChild.solution();
			newChild.isChild = true;
			newChild.isMutation = false;
			newChild.isPreserved = false;
			
		}
		return (newChild);
	}
	boolean contains(final int[] array, final int v) 
	//checks if a number is contained in an array of numbers
	{
	    for (final int e : array)
	        if (e == v)
	            return true;

	    return false;
	}
	boolean contains(final List<Integer> array, final int v) 
	//checks if a number is contained in an array of numbers
	{
	    for (final int e : array)
	        if (e == v)
	            return true;

	    return false;
	}
	public void solution()
	{
		WSTimes = new LinkedList<Integer>();
		toTable = new String[tasks.length];
		String currLine;
		List<Integer> taskIn;
		int index = 1;
		int tableIndex = 0;
		int availableTime = cycleTime;
		int first = 0;
		int last = tasks.length - 1;
		List<List<Integer>> workstations = new LinkedList<List<Integer>>();
		taskIn = new LinkedList<Integer>();
		while (!(first > last))
		{
			currLine = index + "&";
			if (tasks[first].getTime() <= availableTime && tasks[last].getTime() <= availableTime)
			{
				currLine += taskIndex[first] + "," + taskIndex[last] + "&";
				Random fromFirst = new Random();
				if (fromFirst.nextBoolean())
				{
					currLine += taskIndex[first] + "&";
					availableTime -= tasks[first].getTime();
					currLine += availableTime;
					taskIn.add(taskIndex[first]);
					toTable[tableIndex] = currLine;
					tableIndex++;
					first++;
				}
				else
				{
					currLine += taskIndex[last] + "&";
					availableTime -= tasks[last].getTime();
					currLine += availableTime;
					taskIn.add(taskIndex[last]);
					toTable[tableIndex] = currLine;
					tableIndex++;
					last--;
				}
			}
			else if (tasks[first].getTime() <= availableTime)
			{
				currLine += taskIndex[first] + "&" + taskIndex[first] + "&";
				availableTime -= tasks[first].getTime();
				currLine += availableTime;
				taskIn.add(taskIndex[first]);
				toTable[tableIndex] = currLine;
				tableIndex++;
				first++;
			}
			else if (tasks[last].getTime() <= availableTime)
			{
				availableTime -= tasks[last].getTime();
				currLine += taskIndex[last] + "&" + taskIndex[last] + "&" + availableTime;
				taskIn.add(taskIndex[last]);
				toTable[tableIndex] = currLine;
				tableIndex++;
				last--;
			}
			else
			{
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
	public void setSmoothness()
	{
		int maxTime = 0;
		for (int i = 0; i < WSTimes.size(); i++)
		{
			if (WSTimes.get(i) > maxTime)
			{
				maxTime = WSTimes.get(i);
			}
		}
		double sum = 0;
		for (int i = 0; i < WSTimes.size(); i++)
		{
			sum += (Math.pow((maxTime - WSTimes.get(i)), 2)) / WSTimes.size();
		}
		smoothness = Math.sqrt(sum);
	}
}
