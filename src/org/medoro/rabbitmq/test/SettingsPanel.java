package org.medoro.rabbitmq.test;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.medoro.rabbitmq.config.Config;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SettingsPanel {

	private final JTextField rabbitServer = new JTextField();
	private final JTextField rabbitUser = new JTextField();
	private final JPasswordField rabbitPassword = new JPasswordField();
	private final JTextField queue = new JTextField();

	public JPanel getPanel() {

		PanelBuilder builder = new PanelBuilder(new FormLayout("9dlu, r:p, 3dlu, f:120dlu:g, 9dlu",
				"9dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu"));
		CellConstraints cc = new CellConstraints();
		int row = 2;

		builder.addSeparator("Settings", cc.xyw(2, row, 3));
		row += 2;

		builder.addLabel("Server address: ", cc.xy(2, row));
		builder.add(rabbitServer, cc.xy(4, row));
		row += 2;

		builder.addLabel("Server user: ", cc.xy(2, row));
		builder.add(rabbitUser, cc.xy(4, row));
		row += 2;

		builder.addLabel("Server password: ", cc.xy(2, row));
		builder.add(rabbitPassword, cc.xy(4, row));
		row += 2;

		builder.addLabel("Queue: ", cc.xy(2, row));
		builder.add(queue, cc.xy(4, row));

		setDefaultValues();
		
		return builder.getPanel();
	}

	private void setDefaultValues(){
		rabbitServer.setText(Config.getInstance().getProperty("rabbitAddress"));
		rabbitUser.setText(Config.getInstance().getProperty("rabbitUser"));
		rabbitPassword.setText(Config.getInstance().getProperty("rabbitPassword"));
	}
	
	public String getRabbitServer() {
		return rabbitServer.getText();
	}

	public String getRabbitUser() {
		return rabbitUser.getText();
	}

	public String getRabbitPassword() {
		return rabbitPassword.getText();
	}

	public String getQueue() {
		return queue.getText();
	}

	
	
}
