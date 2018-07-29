package Test;

//Import statements
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * An abstract class for a dialog that asks and retrieves a value. It contains
 * the abstract method {@link #readValue()} that must be defined by its children
 * 
 * @author Ivan Monares Vélez
 *
 */
public abstract class Dialog extends JDialog {
	private static final long serialVersionUID = 1218049109753003135L;

	/**
	 * TextField for the user to input their answer
	 */
	protected JTextField textField;

	/**
	 * Label to indicate to the user which data to input
	 */
	protected JLabel lblValue;

	/**
	 * Label to indicate to the user the value type to input
	 */
	protected JLabel lblType;

	/**
	 * Abstract method to read the value in the text field that must be implemented
	 * by the class' children
	 * 
	 * @return - true if the value was successfully read, false otherwise.
	 */
	protected abstract boolean readValue();

	/**
	 * Dialog constructor, creates a dialog that contains the given labels and a
	 * textField
	 * 
	 * @param label
	 *            - Identifies which data to input. Used for {@link #lblValue}
	 * @param type
	 *            - Specifies the variable type to input. Used for {@link #lblType}
	 */
	public Dialog(String label, String type) {
		super();
		this.setModal(true);
		setBounds(100, 100, 150, 150);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.getContentPane().setLayout(gbl_contentPanel);
		{
			lblValue = new JLabel(label);
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.insets = new Insets(0, 0, 0, 5);
			gbc_label.anchor = GridBagConstraints.EAST;
			gbc_label.gridx = 1;
			gbc_label.gridy = 3;
			getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			getContentPane().add(lblValue);
		}
		{
			lblType = new JLabel(type);
			getContentPane().add(lblType);
		}
		{
			textField = new JTextField();
			getContentPane().add(textField);
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {

						boolean exit = readValue();
						if (exit) {
							setVisible(false);
						}
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * A method to change the label of {@link #lblValue}
	 * 
	 * @param newLabel
	 *            - The new text for the label
	 */
	public void changeValueLabel(String newLabel) {
		lblValue.setText(newLabel);
	}

	/**
	 * A method to change the label of {@link #lblType}
	 * 
	 * @param newLabel
	 *            - The new text for the label
	 */
	public void changeTypeLabel(String newLabel) {
		lblType.setText(newLabel);
	}
}