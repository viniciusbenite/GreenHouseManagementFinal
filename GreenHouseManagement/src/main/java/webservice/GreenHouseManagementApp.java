package webservice;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class GreenHouseManagementApp {

    public static final String EXCHANGE_NAME = "SENSOR DA ESTUFA";
    public static final String EXCHANGE_NAME_CONTROLO = "CONTROLO";
    public static final String QUEUE_SPECIFIC_NAME = "DADOS DO SENSOR";
    public static final String QUEUE_SPECIFIC_NAME_CONTROLO = "DADOS DO CONTROLO";
    public static final String ROUTING_KEY = "SENSOR.KEY";
    public static final String ROUTING_KEY_CONTROLO = "CONTROLO.KEY";

    public static void main(String[] args) { SpringApplication.run(GreenHouseManagementApp.class, args); }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueueSpecific() {
        return new Queue(QUEUE_SPECIFIC_NAME);
    }

    @Bean
    public TopicExchange controloAppExchange() { return new TopicExchange(EXCHANGE_NAME_CONTROLO); }

    @Bean
    public Queue controloAppQueueSpecific() { return new Queue(QUEUE_SPECIFIC_NAME_CONTROLO); }

    @Bean
    public Binding declareBindingSpecific() { return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY); }

    @Bean
    public Binding declareBindingSpecificControlo() { return BindingBuilder.bind(controloAppQueueSpecific()).to(controloAppExchange()).with(ROUTING_KEY_CONTROLO); }

    // serialization / deserialization por JSON
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}

