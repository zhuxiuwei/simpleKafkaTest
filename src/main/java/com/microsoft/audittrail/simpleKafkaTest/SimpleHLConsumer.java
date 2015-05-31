package com.microsoft.audittrail.simpleKafkaTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class SimpleHLConsumer {
	private final ConsumerConnector consumer;
	private final String topic;

	public SimpleHLConsumer(String zookeeper, String groupId, String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig(zookeeper,
						groupId));
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig(String zookeeper,
			String groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "500");
		props.put("zookeeper.sync.time.ms", "250");
		props.put("auto.commit.interval.ms", "1000");
		return new ConsumerConfig(props);
	}

	public void testConsumer() {
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		// Define single thread for topic
		topicMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap = consumer
				.createMessageStreams(topicMap);
		List<KafkaStream<byte[], byte[]>> streamList = consumerStreamsMap
				.get(topic);
		for (final KafkaStream<byte[], byte[]> stream : streamList) {
			ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();
			String msg ;
			try {
				FileWriter fw = new FileWriter(new File("D:\\Result.log"));
				int flushCount = 5, count = 0;
				while (consumerIte.hasNext()){
					msg = new String(consumerIte.next().message());
					fw.write("!" + msg + "\r\n");
					if(count == flushCount){
						fw.flush();
						count = 0;
					}
					count ++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
				
		}
		if (consumer != null)
			consumer.shutdown();
	}

	public static void main(String[] args) {
		String zooKeeper = "10.86.164.32:2181";
		String groupId = "testsldsf";
		String topic = "kafkatopic";
		SimpleHLConsumer simpleHLConsumer = new SimpleHLConsumer(zooKeeper,
				groupId, topic);
		simpleHLConsumer.testConsumer();
	}
}