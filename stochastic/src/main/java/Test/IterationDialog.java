package Test;

import Test.Dialog;

public class IterationDialog extends Dialog {
	private static final long serialVersionUID = 8575975346611410122L;

	public int value;

	public IterationDialog() {
		super("Iterations", "(Enter integers)");
	}
	
	@Override
	public boolean readValue() {
		try {
			int v = Integer.parseInt(textField.getText());
			if(v < 1) {
				v = (int) (1/0);
			}
			this.value = v;
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