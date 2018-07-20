package Test;

import Test.Dialog;

/**
 * This is an class that extends Dialog and asks an integer value
 * @author Ivan Monares Vélez <ivanmonares@hotmail.com>
 *
 * @see Dialog
 */
public class IntegerDialog extends Dialog {
	private static final long serialVersionUID = 1858023231054325068L;

	/**
	 * An int variable to store the input
	 */
	public int value;

	/**
	 * Constructor of the class
	 * @param title -  Identifies which data to input
	 */
	public IntegerDialog(String title) {
		super(title, "Only integers");
	}

	/* (non-Javadoc)
	 * @see Test.Dialog#readValue()
	 * Make sure the value is a positive integer of at least 1, store it and return true
	 * Otherwise, return false
	 */
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

	/**
	 * Shows the dialog to the user
	 * @return - Returns the int value of variable {@link #value }
	 */
	public int showDialog() {
		this.setVisible(true);
		return this.value;
	}
}