package Test;

import main.java.Test.Dialog;

public class ProbabilityDialog extends Dialog {
	private static final long serialVersionUID = 1615004142184543418L;

	private double value;

	public ProbabilityDialog() {
		super("Probability", "(Enter doubles 0-100)");
	}

	@Override
	public boolean readValue() {
		try {
			this.value = Double.parseDouble(textField.getText()) / 100;
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.textField.setText("");
			return false;
		}
	}

	public double showDialog() {
		this.setVisible(true);
		return this.value;
	}
}