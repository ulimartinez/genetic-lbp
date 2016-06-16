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
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CycleTime extends JDialog {

	public final JDialog contentPanel = new JDialog();
	private JTextField textField;
	private int cycle;
	private JLabel lblCycleTime;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public CycleTime() {
		setBounds(100, 100, 150, 130);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.getContentPane().setLayout(gbl_contentPanel);
		//getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblCycleTime = new JLabel("Cycle Time");
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
						setCycle(Integer.parseInt(textField.getText()));
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
	public void setLabel(String set)
	{
		lblCycleTime.setText(set);
	}
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

}
