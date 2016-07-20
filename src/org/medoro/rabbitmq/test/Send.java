package org.medoro.rabbitmq.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Send {

	public static void main(String[] args) {
		
		final JFrame frame = new JFrame("Send into RabbitMQ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setAlwaysOnTop(true);

		final SettingsPanel settingsPanel = new SettingsPanel();
		final JTextField text = new JTextField();
		final JButton btn = new JButton(new AbstractAction("Send message") {

			private static final long serialVersionUID = 8912127175400349703L;

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					ConnectionFactory factory = new ConnectionFactory();
					factory.setRequestedHeartbeat(60);
					factory.setHost(settingsPanel.getRabbitServer());
				    factory.setUsername(settingsPanel.getRabbitUser());
					factory.setPassword(settingsPanel.getRabbitPassword());
				    Connection connection = factory.newConnection();
				    try {
				    	Channel channel = connection.createChannel();
					    try {
					    	channel.queueDeclare(settingsPanel.getQueue(), true, false, false, null);
						    String message = text.getText();
						    channel.basicPublish("", settingsPanel.getQueue(), MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
						    JOptionPane.showMessageDialog(frame, "sended!");
					    } finally {
					    	channel.close();
					    }
				    } finally {
				    	connection.close();
				    }
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
		text.setText("Message ...");
		
		PanelBuilder builder = new PanelBuilder(new FormLayout("9dlu, f:240dlu:g, 9dlu, ", "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu"));
		CellConstraints cc = new CellConstraints();
		int row = 1;
		
		builder.add(settingsPanel.getPanel(), cc.xyw(1, row, 3));
		row += 2;
		
		builder.addSeparator("Message", cc.xy(2, row));
		row += 2;
		
		builder.add(text, cc.xy(2, row));
		row += 2;
		
		builder.add(btn, cc.xy(2, row));
		
		frame.add(builder.getPanel(), BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
	}
	
}
