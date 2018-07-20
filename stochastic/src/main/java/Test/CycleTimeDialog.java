package Test;

import Test.Dialog;

/**
 * This is an class that extends Dialog and asks an for the Cycle Time of the problem
 * @author Ivan Monares Vélez <ivanmonares@hotmail.com>
 *
 * @see Dialog
 */
public class CycleTimeDialog extends Dialog {
	private static final long serialVersionUID = -5365070432517905136L;

	/**
	 * A double variable to store the input
	 */
	public double value;

	/**
	 * Class Constructor
	 */
	public CycleTimeDialog() {
		super("Cycle Time", "(Enter doubles)");
	}
	
	/* (non-Javadoc)
	 * @see Test.Dialog#readValue()
	 * Make sure the input is a positive double number, store it and return true
	 * Otherwise, clear the textField and return false
	 */
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

	/**
	 * Shows the dialog to the user
	 * @return - Returns the double value of variable {@link #value }
	 */
	public double showDialog() {
		this.setVisible(true);
		return this.value;
	}
}