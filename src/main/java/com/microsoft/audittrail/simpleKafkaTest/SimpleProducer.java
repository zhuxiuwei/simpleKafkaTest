package com.microsoft.audittrail.simpleKafkaTest;

import java.util.Date;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import kafka.producer.ProducerConfig;;

public class SimpleProducer {
	private static Producer<String, String> producer;

	public SimpleProducer() {
		Properties props = new Properties();
		// Set the broker list for requesting metadata to find the lead broker
		props.put("metadata.broker.list", "localhost:9092");
		// This specifies the serializer class for keys
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// 1 means the producer receives an acknowledgment once the lead replica
		// has received the data. This option provides better durability as the
		// client waits until the server acknowledges the request as successful.
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
	}

	public static void main(String[] args) {
		// Topic name and the message count to be published is passed from the
		// command line
		String topic = "kafkatopic";
		String count = "500000";
		int messageCount = Integer.parseInt(count);
		System.out.println("Topic Name - " + topic);
		System.out.println("Message Count - " + messageCount);
		SimpleProducer simpleProducer = new SimpleProducer();
		simpleProducer.publishMessage(topic, messageCount);
	}

	private void publishMessage(String topic, int messageCount) {
		int durationInOneSecond = 1 * 1000;
		long durationInOneDay = 1000 * 60 * 60 * 24;
		for (long end = new Date().getTime() + durationInOneDay * 2, count = 0; new Date().getTime() < end; count++) {
			String msg = "Message Sequence: " + count;
			System.out.println(msg);
			// Creates a KeyedMessage instance
			KeyedMessage<String, String> data = new KeyedMessage<String, String>( topic, msg);
			// Publish the message
			producer.send(data);
		}
		// Close producer connection with broker.
		producer.close();
	}
}