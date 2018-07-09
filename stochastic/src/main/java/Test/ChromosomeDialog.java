package Test;

import main.java.Test.Dialog;

public class ChromosomeDialog extends Dialog {
	private static final long serialVersionUID = 1858023231054325068L;

	public int value;

	public ChromosomeDialog() {
		super("Number of Chromosomes", "Only integers");
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