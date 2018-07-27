package Test;

/**
 * A class that stores the parameters used to solve the LBP problem
 * @author Ulises Martinez
 *
 */
public class LBPParameters {

	/* these are used for calculating solutions */
	
	/**
	 * A double variable to store the confidence level 
	 */
	private double probability;
	
	/**
	 * A double variable to store the problem's cycle time
	 */
	private double cycleTime;
	
	/**
	 * A double variable to store the amount of chromosomes to generate
	 */
	private int numChromosomes;

	/* these are used for generating iterations */

	/**
	 * An int variable to store the amount of iterations used to solve the problem
	 */
	private int iterations;
	
	/**
	 * A double variable to store the percentage of chromosomes per iteration
	 * that are children of the previous iteration's best chromosomes	
	 */
	private double childPercent;
	
	/**
	 * A double variable to store the percentage of chromosomes per iteration
	 * that are mutations of the previous iteration's best chromosomes	
	 */
	private double mutationsPercent;
	
	/**
	 * A double variable to store the percentage of chromosomes per iteration
	 * that are simply the previous iteration's best chromosomes	preserved
	 */
	private double preservedPercent;

	/**
	 * Parameterized class constructor that initializes {@link #probability}, {@link #cycleTime} and {@link #numChromosomes}
	 * to the given values
	 * @param probability 		- The value used to initialize probability
	 * @param cycle						- The value used to initialize cycleTime
	 * @param chromosomes	- The value used to initialize numChromosomes
	 */
	public LBPParameters(double probability, int cycle, int chromosomes) {
		this.probability = probability;
		this.cycleTime = cycle;
		this.numChromosomes = chromosomes;
	}

	/**
	 * Unparameterized class constructor that initializes {@link #probability}, {@link #cycleTime} and {@link #numChromosomes}
	 * to 0 and {@link #iterations} to 1
	 */
	public LBPParameters() {
		probability = 0;
		cycleTime = 0;
		numChromosomes = 0;
		iterations = 1;
	}

	/* getters */
	
	/**
	 * Getter for the variable probability
	 * @return - double value of {@link #probability}
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * Getter for the variable cycleTime
	 * @return - double value of {@link #cycleTime}
	 */
	public double getCycleTime() {
		return cycleTime;
	}

	/**
	 * Getter for the variable numChromosomes
	 * @return - int value of {@link #numChromosomes}
	 */
	public int getNumChromosomes() {
		return numChromosomes;
	}

	/**
	 * Getter for the variable childPercent
	 * @return - double value of {@link #childPercent}
	 */
	public double getChildPercent() {
		return childPercent;
	}

	/**
	 * Getter for the variable mutationsPercent
	 * @return - double value of {@link #mutationsPercent}
	 */
	public double getMutationsPercent() {
		return mutationsPercent;
	}

	/**
	 * Getter for the variable preservedPercent
	 * @return - double value of {@link #preservedPercent}
	 */
	public double getPreservedPercent() {
		return preservedPercent;
	}

	/**
	 * Getter for the variable iterations
	 * @return - double value of {@link #iterations}
	 */
	public int getIterations() {
		return iterations;
	}

	/* setters */
	
	/**
	 * Setter for the variable cycleTime
	 * @param cycleTime - The value used to set {@link #cycleTime}
	 */
	public void setCycleTime(double cycleTime) {
		this.cycleTime = cycleTime;
	}

	/**
	 * Setter for the variable probability
	 * @param probability - The value used to set {@link #probability}
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * Setter for the variable numChromosomes
	 * @param numChromosomes - The value used to set {@link #numChromosomes}
	 */
	public void setNumChromosomes(int numChromosomes) {
		this.numChromosomes = numChromosomes;
	}

	/**
	 * Setter for the variable childPercent
	 * @param childPercent - The value used to set {@link #childPercent}
	 */
	public void setChildPercent(double childPercent) {
		this.childPercent = childPercent;
	}

	/**
	 * Setter for the variable iterations
	 * @param iterations - The value used to set {@link #iterations}
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * Setter for the variable mutationsPercent
	 * @param mutationsPercent - The value used to set {@link #mutationsPercent}
	 */
	public void setMutationsPercent(double mutationsPercent) {
		this.mutationsPercent = mutationsPercent;
	}

	/**
	 * Setter for the variable preservedPercent
	 * @param preservedPercent - The value used to set {@link #preservedPercent}
	 */
	public void setPreservedPercent(double preservedPercent) {
		this.preservedPercent = preservedPercent;
	}
}
