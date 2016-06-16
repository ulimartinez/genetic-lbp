package Test;

public class task {
	private int[] precedence;
	private int time;
	task()
	{
		
	}
	task (int[] precedence, int time)
	{
		this.precedence = precedence;
		this.time = time;
	}
	public int[] getPrecedences()
	{
		return (precedence);
	}
	public int getTime()
	{
		return(time);
	}
	void setTime(int time)
	{
		this.time = time;
	}
	void setPrecedences(int[] precedences)
	{
		precedence = precedences;
	}
}
