package Test;

import Test.Dialog;

public class CycleTimeDialog extends Dialog {
	private static final long serialVersionUID = -5365070432517905136L;

	public int value;

	public CycleTimeDialog() {
		super("Cycle Time", "(Enter integers)");
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