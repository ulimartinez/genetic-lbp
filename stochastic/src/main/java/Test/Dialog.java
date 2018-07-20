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
 * This is an abstract class for a dialog that asks and retrieves a value 
 * @author Ivan Monares Vélez <ivanmonares@hotmail.com>
 *
 */
public abstract class Dialog extends JDialog {
	private static final long serialVersionUID = 1218049109753003135L;

	/**
	 * TextField for the user to input their answer
	 */
	protected JTextField textField;
	
	/**
	 * Label to indicate to the user the value type to input
	 */
	protected JLabel lblValue;

	/**
	 * Abstract method to read the value in the text field 
	 * that must be implemented by the class' children
	 * @return - true if the value was successfully read, false otherwise.
	 */
	public abstract boolean readValue();

	
	/**
	 * Dialog constructor
	 * @param label - Identifies which data to input
	 * @param type - Specifies the variable type to input
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
			JLabel lblType = new JLabel(type);
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
						if(exit) {
							setVisible(false);
						}
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}