package webservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;


@Component
public class CustomMessageListener {

    private ConfigClass cenas = new ConfigClass();

    private MongoClientURI connectionString = new MongoClientURI("mongodb+srv://dbAdmin:projetoies@greenhouse-vvjsz.mongodb.net/test?retryWrites=true&w=majority");
    private MongoClient mongoClient = new MongoClient(connectionString);
    private MongoDatabase db = mongoClient.getDatabase("GreenHouseDb");
    private MongoCollection<Document> collecTempExt = db.getCollection("Temp_externas");
    private MongoCollection<Document> collecTempInt = db.getCollection("Temp_internas");
    private MongoCollection<Document> collecVelocVento = db.getCollection("Velocidade_vento");
    private MongoCollection<Document> collecRad = db.getCollection("Radiacao");
    private MongoCollection<Document> collecPh = db.getCollection("PH");
    private MongoCollection<Document> collecHumidade = db.getCollection("Humidade");
    
    private static final Logger log = LoggerFactory.getLogger(CustomMessageListener.class);

    // listmultimap -> faz o append dos values, e nao sobrescreve
    private static Multimap<Integer, String> tempExt = ArrayListMultimap.create(); // mapa que guarda {ID_SENSOR, [TEMP EXT]}
    private static Multimap<Integer, String> tempInt = ArrayListMultimap.create(); // mapa que guarda {ID_SENSOR, [TEMP INT]}
    private static Multimap<Integer, String> velocVento = ArrayListMultimap.create();
    private static Multimap<Integer, String> rad = ArrayListMultimap.create();
    private static Multimap<Integer, String> ph = ArrayListMultimap.create();
    private static Multimap<Integer, String> Humidade = ArrayListMultimap.create();
    private static Multimap<Integer, String> Acoes = ArrayListMultimap.create();
    private static ArrayList<Integer> estufas = new ArrayList<>();
    
    DateTimeFormatter formatter =
    	    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
    	                     .withLocale( Locale.UK )
    	                     .withZone( ZoneId.systemDefault() );

    int counter;


    // Mostrar dados das mensagens enviadas e trata de todos os dados necessarios
    @RabbitListener(queues = GreenHouseManagementApp.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final Message message) throws IOException, JSONException, MessagingException {
        log.info("Dados do sensor: {}", message.toString());

        JSONObject obj = new JSONObject(new String(message.getBody()));

        // o tipo eh passado na msg e eh configurado no config.json
        if (obj.getString("tipo").equals("temp_int")) {
            tempInt.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data")); 
            if (Double.parseDouble(obj.getString("data")) < 24) {
            	if (!cenas.getAquecedores(obj.getString("sensor")).getState()) {
            		cenas.getLigar().execute((cenas.getAquecedores(obj.getString("sensor"))).toString());
            		Instant ts = Instant.now();
                	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Aquecedor ligou ás "+ formatter.format( ts )); 
            	}
            }
            if (Double.parseDouble(obj.getString("data")) > 26) {
            	if (cenas.getAquecedores(obj.getString("sensor")).getState()) {
            		cenas.getDesligar().execute((cenas.getAquecedores(obj.getString("sensor"))).toString());
            		Instant ts = Instant.now();
                	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Aquecedor desligou ás "+ formatter.format( ts )); 
            	}
            }
            counter++;
        }
        if (obj.getString("tipo").equals("temp_ext")) {
            tempExt.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data"));
        }
        if (obj.getString("tipo").equals("veloc_vento")) {
            velocVento.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data"));
            if (Double.parseDouble(obj.getString("data")) < 400 ) {
            	cenas.getLigar().execute((cenas.getJanelas(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Janela abriu ás "+ formatter.format( ts )); 
        	}
            if (Double.parseDouble(obj.getString("data")) > 500) {
            	cenas.getDesligar().execute((cenas.getJanelas(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Janela fechou ás "+ formatter.format( ts )); 
        	}
        }
        if (obj.getString("tipo").equals("rad")) {
            rad.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data"));
            if (Double.parseDouble(obj.getString("data")) < 400 ) {
            	log.info("Luz ligou");
            	cenas.getLigar().execute((cenas.getLuzes(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Luz ligou ás "+ formatter.format( ts )); 
        	}
            if (Double.parseDouble(obj.getString("data")) > 500) {
            	cenas.getDesligar().execute((cenas.getLuzes(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Luz desligou ás "+ formatter.format( ts )); 
        	}
        }

        if (obj.getString("tipo").equals("ph")) {
            ph.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data"));
        }
        if (obj.getString("tipo").equals("humidade")) {
            Humidade.put(Integer.parseInt(obj.getString("sensor")), obj.getString("data")); 
            if (Double.parseDouble(obj.getString("data")) < 400 ) {
            	cenas.getLigar().execute((cenas.getRegadores(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Regador ligou ás "+ formatter.format( ts )); 
        	}
            if (Double.parseDouble(obj.getString("data")) > 500) {
            	cenas.getDesligar().execute((cenas.getRegadores(obj.getString("sensor"))).toString());
        		Instant ts = Instant.now();
            	Acoes.put(Integer.parseInt(obj.getString("sensor")), "Regador desligou ás "+ formatter.format( ts )); 
        	}
        }
        estufas.add(obj.getInt("estufa"));

        // Quando o mapa auxiliar atingier 10 entradas, ocorre uma escrita na BD
        // Precisa do array auxiliar pq o webservice utiliza o outro array
        if (counter > 10000) {
            try {
                for (int key : tempInt.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecTempInt.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", tempInt.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                for (int key : tempExt.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecTempExt.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", tempExt.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                for (int key : velocVento.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecVelocVento.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", velocVento.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                for (int key : rad.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecRad.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", rad.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                for (int key : ph.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecPh.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", ph.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                
                for (int key : Humidade.keySet()) {
                    for (int i = 0 ; i < counter ; i++) {
                        collecHumidade.updateOne(Filters.eq("sensor", key), Updates.addToSet("data", Humidade.get(key).toArray()[i]), new UpdateOptions().upsert(true));
                    }
                }
                log.info("DATABASE UPDATED");
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter = 0;
        }
    }

    public Multimap<Integer, String> getTempExt() { return tempExt; }
    public Multimap<Integer, String> getTempInt() { return tempInt; }
    public Multimap<Integer, String> getVelocVento() { return velocVento; }
    public Multimap<Integer, String> getRad() { return rad; }
    public Multimap<Integer, String> getPh() { return ph; }
    public Multimap<Integer, String> getHumidade() { return Humidade; }
    public Multimap<Integer, String> getAcoes() { return Acoes; }
    public ArrayList<Integer> getEstufas() { return estufas; }
}
