package org.medoro.rabbitmq.test;

import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.medoro.rabbitmq.config.Config;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Recv {

	private final JTextField queue = new JTextField();
	private final JTextArea results = new JTextArea();
	
	private boolean running = false;
	
	public Recv() {
		final JFrame frame = new JFrame("Receive from RabbitMQ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setAlwaysOnTop(true);
		
		final JButton btnStart = new JButton();
		btnStart.setAction(new AbstractAction("Start") {
			private static final long serialVersionUID = -617275520317848746L;
			@Override
			public void actionPerformed(java.awt.event.ActionEvent arg0) {

				running = !running;
				btnStart.setText(running ? "Stop" : "Start");
				
				initRabbit();
				
			}
		});
		
		PanelBuilder builder = new PanelBuilder(new FormLayout("9dlu, r:p, 3dlu, f:120dlu:g, 9dlu", "9dlu, p, 3dlu, f:45dlu:g, 3dlu, p, 9dlu"));
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Queue: ", cc.xy(2, 2));
		builder.add(queue, cc.xy(4, 2));
		
		builder.add(new JScrollPane(results), cc.xyw(2, 4, 3));

		builder.add(btnStart, cc.xyw(2, 6, 3));

		frame.add(builder.getContainer(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private void initRabbit(){
		new Thread(
			new Runnable() {
				@Override
				public void run() {
					try {
						ConnectionFactory factory = new ConnectionFactory();
					    factory.setHost(Config.getInstance().getProperty("rabbitAddress"));
					    factory.setUsername(Config.getInstance().getProperty("rabbitUser"));
						factory.setPassword(Config.getInstance().getProperty("rabbitPassword"));
					    Connection connection = factory.newConnection();
					    try {
					    	Channel channel = connection.createChannel();

						    channel.queueDeclare(queue.getText(), false, false, false, null);
						    
						    if(running) {
						    	results.append(" [*] Waiting for messages. To exit press CTRL+C \n");
						    } else {
						    	results.append(" [*] Stop queue \n");
						    }
						    results.setCaretPosition(results.getDocument().getLength());
						    
						    QueueingConsumer consumer = new QueueingConsumer(channel);
						    channel.basicConsume(queue.getText(), true, consumer);

						    while (running) {
						      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
						      String message = new String(delivery.getBody());
						      results.append(" [x] Received '" + message + "' \n");
						      results.setCaretPosition(results.getDocument().getLength());
						    }
						    
					    } finally {
					    	connection.close();
					    }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		).start();
	}
	
	public static void main(String[] argv) {
		new Recv();
	}
}
