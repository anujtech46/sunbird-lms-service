package telemetry.dispatcher;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;

public class KafkaDispatcher implements IDispatcher {

	private final static String BOOTSTRAP_SERVERS = System.getenv("sunbird_telemetry_kafka_servers_config");
	private final static String topic = System.getenv("sunbird_telemetry_kafka_topic");
	private Producer<Long, String> producer;

	public KafkaDispatcher() {
		createProducer();
	}

	private void createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaClientProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producer = new KafkaProducer<Long, String>(props);
	}

	protected Producer<Long, String> getProducer() {
		return producer;
	}

	@Override
	public void dispatch(List<String> events) throws Exception {
		ProjectLogger.log("KafkaDispatcher got events: " + events.size(), LoggerEnum.INFO.name());
		for (String event : events) {
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(topic, event);
			producer.send(record);
		}
	}

}
