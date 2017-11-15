package Test;

public class task {
	private int[] precedence;
	private int time;

	task(int[] precedence, int time) {
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
