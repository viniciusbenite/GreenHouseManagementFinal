package webservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomMessageSender {

    private static final Logger log = LoggerFactory.getLogger(CustomMessageSender.class);
    private final RabbitTemplate rabbitTemplate;
    private ConfigClass cenas = new ConfigClass();

    public CustomMessageSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "*/5 * * * * *") // Periodo de atualizacao dos dados -> https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
    public void sendMessage() throws IOException {

        ControloRemoto msg_controlo = new ControloRemoto(ThreadLocalRandom.current().nextInt(1,4), false, false, false, false, false, false);

        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/webservice/config.json"));

            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            //read customer.json file into tree model
            JsonNode parser = objectMapper.readTree(reader);

            for (JsonNode sensor : parser.path("sensores")) {

                // dependendo do tipo, vai gerar um tipo de dado
                if (sensor.path("tipo").asText().equals("temp_int")) {
                    CustomMessage msg = new CustomMessage(sensor.path("_id").asInt(),
                            sensor.path("tipo").asText(),
                            ThreadLocalRandom.current().nextDouble(cenas.getAquecedores(sensor.path("_id").asText()).getmin(), cenas.getAquecedores(sensor.path("_id").asText()).getmax()), sensor.path("estufa").asInt()); // temp_interna
//                    log.info("Enviando dados...");
                    rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME, GreenHouseManagementApp.ROUTING_KEY, msg);
                }
                if (sensor.path("tipo").asText().equals("temp_ext")) {
                    CustomMessage msg = new CustomMessage(sensor.path("_id").asInt(),
                            sensor.path("tipo").asText(),
                            ThreadLocalRandom.current().nextDouble(5, 8), sensor.path("estufa").asInt()); // temp_externa
                    rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME, GreenHouseManagementApp.ROUTING_KEY, msg);
                }
                if (sensor.path("tipo").asText().equals("veloc_vento")) {
                    CustomMessage msg = new CustomMessage(sensor.path("_id").asInt(),
                            sensor.path("tipo").asText(),
                            ThreadLocalRandom.current().nextDouble(cenas.getJanelas(sensor.path("_id").asText()).getmin(), cenas.getJanelas(sensor.path("_id").asText()).getmax()), sensor.path("estufa").asInt()); // velocidade vento
                    rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME, GreenHouseManagementApp.ROUTING_KEY, msg);
                }
                if (sensor.path("tipo").asText().equals("rad")) {
                    CustomMessage msg = new CustomMessage(sensor.path("_id").asInt(),
                            sensor.path("tipo").asText(),
                            ThreadLocalRandom.current().nextDouble(cenas.getLuzes(sensor.path("_id").asText()).getmin(), cenas.getLuzes(sensor.path("_id").asText()).getmax()), sensor.path("estufa").asInt()); // radiacao
                    rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME, GreenHouseManagementApp.ROUTING_KEY, msg);
                }
                if (sensor.path("tipo").asText().equals("ph")) {
                    CustomMessage msg = new CustomMessage(sensor.path("_id").asInt(),
                            sensor.path("tipo").asText(),
                            ThreadLocalRandom.current().nextDouble(6.5, 7), sensor.path("estufa").asInt()); // ph
                    rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME, GreenHouseManagementApp.ROUTING_KEY, msg);
                }
            }
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        rabbitTemplate.convertAndSend(GreenHouseManagementApp.EXCHANGE_NAME_CONTROLO, GreenHouseManagementApp.ROUTING_KEY_CONTROLO, msg_controlo);
    }
}

// RUN DOCKER RABBIT: docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management