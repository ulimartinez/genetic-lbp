package Test;


import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.ArrayList;


public class Task {
	private ArrayList<Integer> precedence;
	private double MeanTime;
	private double StdDeviation;
	NormalDistribution probabilityA;
	private int taskNum;
	
	Task() {
		
	}
	Task(int num){
	    this.taskNum = num;
	    this.precedence = new ArrayList<Integer>();
    }
	Task(ArrayList<Integer> precedence, double time, double stdDev) {
		this.precedence = precedence;
		this.MeanTime = time;
		this.StdDeviation = stdDev;
	}
    Task(int[] precedence, double time, double stdDev) {
        this.precedence = new ArrayList<Integer>(precedence.length);
        for (int aPrecedence : precedence) this.precedence.add(aPrecedence);
        this.MeanTime = time;
        this.StdDeviation = stdDev;
    }
	public int[] getPrecedences() {
		int[] ret = new int[precedence.size()];
		for (int i=0; i < ret.length; i++)
		{
			ret[i] = precedence.get(i);
		}
		return ret;
	}
	public double getTime() {
		return(MeanTime);
	}
	public double getStdDev() {
		return(StdDeviation);
	}
	public  NormalDistribution getProbability() {
		probabilityA = new NormalDistributionImpl(MeanTime,StdDeviation);
		return(probabilityA);
	}
	public int getTaskNum(){ return taskNum; }
	void setTime(double time) {
		this.MeanTime = time;
	}
	void setPrecedences(int[] precedences) {
		precedence = new ArrayList<Integer>(precedences.length);
		for(int i : precedences){
			precedence.add(i);
		}
	}
	void setStd(double stdDeviation) {
		this.StdDeviation = stdDeviation;
	}
	void addPrecedence(int prec){
	    precedence.add(prec); }
}
