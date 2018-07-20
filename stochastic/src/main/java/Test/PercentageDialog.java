package Test;

/**
 * This is an class that extends Dialog and asks a percentage value
 * @author Ivan Monares Vélez <ivanmonares@hotmail.com>
 *
 * @see Dialog
 */
public class PercentageDialog extends Dialog {
	private static final long serialVersionUID = -957483271281472126L;

	/**
	 * A double variable to store the input
	 */
	public double value;
	
	/**
	 * Constructor of the class
	 * @param title -  Identifies which data to input
	 */
	public PercentageDialog(String title) {
		super(title, "(Enter doubles 0-100)");
	}
	
	/* (non-Javadoc)
	 * @see Test.Dialog#readValue()
	 * Make sure the input is a double number, divide by 100, store it and return true
	 * Otherwise, clear the textField and return false
	 */
	@Override
	public boolean readValue() {
		try {
			Double v = Double.parseDouble( textField.getText() );
			if(v > 100 || v < 0) {
				v = (double) (1/0);
			}
			this.value = v / 100;
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