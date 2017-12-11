package LBP.LBP;

public class Task {
	private int[] precedence;
	private int time;

	Task(int[] precedence, int time) {
		this.precedence = precedence;
		this.time = time;
	}

	int[] getPrecedences() {
		return (precedence);
	}

	public int getTime() {
		return (time);
	}
}
