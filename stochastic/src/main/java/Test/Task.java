package Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.ArrayList;

/**
 * A class used to create instances of each task in a Line Balancing Problem
 * 
 * @author Ulises Martinez
 *
 */
public class Task {

	/**
	 * An ArrayList that stores the immediate predecessors of the given task
	 */
	private ArrayList<Integer> precedence;

	/**
	 * A double variable that stores the task's mean completion time
	 */
	private double meanTime;

	/**
	 * A double variable that stores the task' time's standard deviation
	 */
	private double stdDeviation;

	/**
	 * An int variable that stores the task's identifying number
	 */
	private int taskNum;

	/**
	 * An empty class constructor
	 */
	Task() {
	}

	/**
	 * A class constructor that initializes {@link #taskNum} with the given
	 * parameter and initializes {@link #precedence} as an empty array
	 * 
	 * @param num
	 *            - An int number that identifies the task
	 */
	Task(int num) {
		this.taskNum = num;
		this.precedence = new ArrayList<Integer>();
	}

	/**
	 * A class constructor that initializes the variables taskNum, precedence,
	 * meanTime and stdDeviation.
	 * 
	 * @param taskNum
	 *            - An int number that identifies the task
	 * @param precedence
	 *            - An ArrayList used to initialize {@link #precedence}
	 * @param time
	 *            - A double used to initialize {@link #meanTime}
	 * @param stdDev
	 *            - A double used to initialize {@link #stdDeviation}
	 */
	Task(int taskNum, ArrayList<Integer> precedence, double time, double stdDev) {
		this.taskNum = taskNum;
		this.precedence = precedence;
		this.meanTime = time;
		this.stdDeviation = stdDev;
	}

	/**
	 * A class constructor that initializes the variables precedence, meanTime and
	 * stdDeviation.
	 * 
	 * @param taskNum
	 *            - An int number that identifies the task
	 * @param precedence
	 *            - An int array used to initialize {@link #precedence}
	 * @param time
	 *            - A double used to initialize {@link #meanTime}
	 * @param stdDev
	 *            - A double used to initialize {@link #stdDeviation}
	 */
	Task(int taskNum, int[] precedence, double time, double stdDev) {
		this.taskNum = taskNum;
		this.precedence = new ArrayList<Integer>(precedence.length);
		for (int aPrecedence : precedence) {
			this.precedence.add(aPrecedence);
		}
		this.meanTime = time;
		this.stdDeviation = stdDev;
	}

	/**
	 * Getter for the task'sprecedence
	 * 
	 * @return - The data from {@link #precedence} transformed into an int array.
	 */
	public int[] getPrecedences() {
		int[] ret = new int[precedence.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = precedence.get(i);
		}
		return ret;
	}

	/**
	 * Getter for the task's mean completion time
	 * 
	 * @return - {@link #meanTime}
	 */
	public double getTime() {
		return (meanTime);
	}

	/**
	 * Getter for the task's time's standard deviation
	 * 
	 * @return - {@link #stdDeviation}
	 */
	public double getStdDev() {
		return (stdDeviation);
	}

	/**
	 * Method that uses {@link #meanTime} and {@link #stdDeviation} to calculate the
	 * task's maximum time for a given confidence level
	 * 
	 * @param probability
	 *            - The confidence value used for the normal distribution
	 * @return - The task's maximum possible time
	 */
	public double getMaxTime(double probability) {
		NormalDistribution probabilityA = new NormalDistributionImpl(meanTime, stdDeviation);
		double res = 0;
		try {
			res = probabilityA.inverseCumulativeProbability(probability);
		} catch (MathException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Getter for the task's identifying number
	 * 
	 * @return - {@link #taskNum}
	 */
	public int getTaskNum() {
		return taskNum;
	}

	/**
	 * Setter for {@link #meanTime}
	 * 
	 * @param time	
	 *            - The task's mean completion time
	 */
	void setTime(double time) {
		this.meanTime = time;
	}

	/**
	 * Setter for {@link #precedence} using an int array as a base
	 * 
	 * @param precedences
	 *            - An int array hat contains the task's immediate predecessors
	 */
	void setPrecedences(int[] precedences) {
		precedence = new ArrayList<Integer>(precedences.length);
		for (int i : precedences) {
			precedence.add(i);
		}
	}

	/**
	 * Setter for {@link #stdDeviation}
	 * 
	 * @param stdDeviation
	 *            - The task's time's standard deviation
	 */
	void setStdDev(double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}

	/**
	 * A method to add a new predecessor to the given task. Use
	 * {@link #getTaskNum()} to get a task's number
	 * 
	 * @param prec
	 *            - The identifying number of the new predecessor.
	 */
	void addPrecedence(int prec) {
		precedence.add(prec);
	}
}
