package Test;

public class LBPParameters {
	/* these are used for calculating solutions */
	private double probability;
	private int cycleTime;
	private int numChromosomes;

	/* these are used for generating iterations */
	private double childPercent;
	private int iterations;
	private double mutationsPercent;
	private double preservedPercent;

	public LBPParameters(double probability, int cycle, int chromosomes) {
		this.probability = probability;
		this.cycleTime = cycle;
		this.numChromosomes = chromosomes;
	}

	public LBPParameters() {
		probability = 0;
		cycleTime = 0;
		numChromosomes = 0;
		iterations = 1;
	}

	/* getters */
	public double getProbability() {
		return probability;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	public int getNumChromosomes() {
		return numChromosomes;
	}

	public double getChildPercent() {
		return childPercent;
	}

	public double getMutationsPercent() {
		return mutationsPercent;
	}

	public double getPreservedPercent() {
		return preservedPercent;
	}

	public int getIterations() {
		return iterations;
	}

	/* setters */
	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public void setNumChromosomes(int numChromosomes) {
		this.numChromosomes = numChromosomes;
	}

	public void setChildPercent(double childPercent) {
		this.childPercent = childPercent;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public void setMutationsPercent(double mutationsPercent) {
		this.mutationsPercent = mutationsPercent;
	}

	public void setPreservedPercent(double preservedPercent) {
		this.preservedPercent = preservedPercent;
	}
}
