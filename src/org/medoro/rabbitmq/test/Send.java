package org.medoro.rabbitmq.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.medoro.rabbitmq.config.Config;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

	public static void main(String[] args) {
		
		final JFrame frame = new JFrame("Send into RabbitMQ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setAlwaysOnTop(true);
		
		final JTextField queue = new JTextField();
		final JTextField text = new JTextField();
		final JButton btn = new JButton(new AbstractAction("Send message") {

			private static final long serialVersionUID = 8912127175400349703L;

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					ConnectionFactory factory = new ConnectionFactory();
					factory.setRequestedHeartbeat(60);
					factory.setHost(Config.getInstance().getProperty("rabbitAddress"));
				    factory.setUsername(Config.getInstance().getProperty("rabbitUser"));
					factory.setPassword(Config.getInstance().getProperty("rabbitPassword"));
				    Connection connection = factory.newConnection();
				    try {
				    	Channel channel = connection.createChannel();
					    try {
					    	channel.queueDeclare(queue.getText(), false, false, false, null);
						    String message = text.getText();
						    channel.basicPublish("", queue.getText(), null, message.getBytes());
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
		
		PanelBuilder builder = new PanelBuilder(new FormLayout("9dlu, r:p, 6dlu, f:120dlu:g, 9dlu, ", "9dlu, p, 3dlu, p, 3dlu, p, 9dlu"));
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Queue: ", cc.xy(2, 2));
		builder.add(queue, cc.xy(4, 2));
		
		builder.addLabel("Message: ", cc.xy(2, 4));
		builder.add(text, cc.xy(4, 4));
		
		builder.add(btn, cc.xyw(2, 6, 3));
		
		frame.add(builder.getPanel(), BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
	}
	
}
