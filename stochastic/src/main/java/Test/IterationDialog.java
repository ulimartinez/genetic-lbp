package Test;

import main.java.Test.Dialog;

public class IterationDialog extends Dialog {
	private static final long serialVersionUID = 8575975346611410122L;

	public int value;

	public IterationDialog() {
		super("Iterations", "(Enter integers)");
	}
	
	@Override
	public boolean readValue() {
		try {
			this.value = Integer.parseInt(textField.getText());
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.textField.setText("");
			return false;
		}
	}

	public int showDialog() {
		this.setVisible(true);
		return this.value;
	}
}