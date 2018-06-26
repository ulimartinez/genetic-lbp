package Test;


import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;


public class Task {
	private int[] precedence;
	private int MeanTime;
	private double StdDeviation;
	NormalDistribution probabilityA;
	private double Z;
	
	Task() {
		
	}
	Task(int[] precedence, int time, double stdDev) {
		this.precedence = precedence;
		this.MeanTime = time;
		this.StdDeviation = stdDev;
	}
	public int[] getPrecedences() {
		return (precedence);
	}
	public int getTime() {
		return(MeanTime);
	}
	public double getStdDev() {
		return(StdDeviation);
	}
	public  NormalDistribution getProbability() {
		probabilityA = new NormalDistributionImpl(MeanTime,StdDeviation);
		return(probabilityA);
	}
	void setTime(int time) {
		this.MeanTime = time;
	}
	void setPrecedences(int[] precedences) {
		precedence = precedences;
	}
	void setStd(double stdDeviation) {
		this.StdDeviation = stdDeviation;
	}
}
