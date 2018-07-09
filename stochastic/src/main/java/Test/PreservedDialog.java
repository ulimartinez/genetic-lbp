package Test;

import Test.Dialog;

public class PreservedDialog extends Dialog {
	private static final long serialVersionUID = -3849656380435416567L;

	public double value;

	public PreservedDialog() {
		super("Preserved %", "(Enter doubles 0-100)");
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