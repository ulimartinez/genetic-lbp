package Test;

import Test.Dialog;

/**
 * A class that extends Dialog and asks an for the Cycle Time of the problem
 * @author Ivan Monares Vélez <ivanmonares@hotmail.com>
 *
 * @see Dialog
 */
public class CycleTimeDialog extends Dialog {
	private static final long serialVersionUID = -5365070432517905136L;

	/**
	 * A double variable to store the input
	 */
	protected double value;
	
	/**
	 * A Double variable to store the smallest time acceptable
	 */
	protected Double minimum;

	/**
	 * Class Constructor
	 */
	public CycleTimeDialog() {
		super("Cycle Time", "(Enter doubles)");
	}
	
	/* (non-Javadoc)
	 * @see Test.Dialog#readValue()
	 * Make sure the input is a double number over/equal to the minimum, store it and return true
	 * Otherwise, clear the textField and return false
	 */
	@Override
	protected boolean readValue() {
		try {
			Double v = Double.parseDouble( textField.getText() );
			if(v < minimum) {
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
	 * @param highest - the highest task time that the problem has
	 * @return - Returns the double value of variable {@link #value }
	 */
	public double showDialog(double highest) {
		this.minimum = highest;
		this.changeTypeLabel("(Over " + this.minimum.floatValue() + ")");
		this.setVisible(true);
		return this.value;
	}
}