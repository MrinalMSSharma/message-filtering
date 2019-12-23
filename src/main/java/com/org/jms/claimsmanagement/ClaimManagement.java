package com.org.jms.claimsmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ClaimManagement {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue claimQueue = (Queue) initialContext.lookup("queue/claimQueue");
		try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
				JMSContext context = connectionFactory.createContext()) {
			JMSProducer producer = context.createProducer();
			JMSConsumer consumer = context.createConsumer(claimQueue, "docName LIKE 'J%'");
			ObjectMessage objectMessage = context.createObjectMessage();
//			objectMessage.setIntProperty("id", 1);
//			objectMessage.setDoubleProperty("claimAmount", 1000);
			objectMessage.setStringProperty("docName", "John");
			
			Claim claim = new Claim();
			claim.setId(1);
			claim.setDocName("John");
			claim.setDocType("Gyna");
			claim.setClaimAmount(1000);
			claim.setInsuranceProvider("Blue Bird");
			objectMessage.setObject(claim);
			
			producer.send(claimQueue, objectMessage);
			
			Claim body = consumer.receiveBody(Claim.class);
			System.out.println("Amount is: " + body.getClaimAmount());
		}
	}
}