package Test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChromosomeDialog extends JDialog {
	private JTextField textField;
	private int number;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public ChromosomeDialog() {
		super();
		this.setModal(true);
		setBounds(100, 100, 150, 130);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gbl_contentPanel);
		//getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblCycleTime = new JLabel("Number of Chromosomes");
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.insets = new Insets(0, 0, 0, 5);
			gbc_label.anchor = GridBagConstraints.EAST;
			gbc_label.gridx = 1;
			gbc_label.gridy = 3;
			getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			getContentPane().add(lblCycleTime);
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
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						setNumber(Integer.parseInt(textField.getText()));
						setVisible(false);
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int numb) {
		this.number = numb;
	}

    public int showDialog(){
        this.setVisible(true);
        return number;
    }

}
