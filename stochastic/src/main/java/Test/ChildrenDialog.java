package Test;

public class ChildrenDialog extends Dialog {
	private static final long serialVersionUID = -957483271281472126L;

	public double value;
	
	public ChildrenDialog() {
		super("Children %", "(Enter doubles 0-100)");
	}
	
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

	public double showDialog() {
		this.setVisible(true);
		return this.value;
	}
}