package Test;

import Test.Dialog;

public class CycleTimeDialog extends Dialog {
	private static final long serialVersionUID = -5365070432517905136L;

	public double value;

	public CycleTimeDialog() {
		super("Cycle Time", "(Enter doubles)");
	}
	
	@Override
	public boolean readValue() {
		try {
			Double v = Double.parseDouble( textField.getText() );
			if(v <= 0) {
				v = (double) (1/0);
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

	public double showDialog() {
		this.setVisible(true);
		return this.value;
	}
}