package webservice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Api(value = "GreenHouse Management")
public class DataController {

    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("config2.json"));

    @Autowired
    private CustomerRepository repository;

    CustomMessageListener listener = new CustomMessageListener();

    private static Map<Integer, Object> finalMapTempInt = new HashMap<>();
    private static Map<Integer, Object> finalMapTempExt = new HashMap<>();
    private static Map<Integer, Object> finalVelocVento = new HashMap<>();
    private static Map<Integer, Object> finalRad = new HashMap<>();
    private static Map<Integer, Object> finalPh = new HashMap<>();
    private static Map<Integer, Object> finalHumidade = new HashMap<>();
    private static Map<Integer, Object> finalAcoes = new HashMap<>();

    private ArrayList<Integer> estufas = new ArrayList<>();

    private Multimap<String, Object> allSensores = ArrayListMultimap.create();
    private Map<String, Object> sensores = new HashMap<>();

    public DataController() throws IOException {
    }

    @ApiOperation(value = "View a list of all greenhouses", response = List.class)
    @GetMapping(value = "/estufas")
    public ResponseEntity<Object> getEstufas() {
        return new ResponseEntity<>(listener.getEstufas(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all internal temperatures from sensor")
    @GetMapping(value = "/data/tempint")
    public ResponseEntity<Object> getTempInt() {
        for (int key : listener.getTempInt().keySet()) {
            finalMapTempInt.put(key, listener.getTempInt().get(key).toArray());
        }
        return new ResponseEntity<>(finalMapTempInt, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all external temperatures from sensor")
    @RequestMapping(value = "data/tempext", method = RequestMethod.GET)
    public ResponseEntity<Object> getTempExt() {
        for (int key : listener.getTempExt().keySet()) {
            finalMapTempExt.put(key, listener.getTempExt().get(key).toArray());
        }
        return new ResponseEntity<>(finalMapTempExt, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all wind speed data from sensor")
    @RequestMapping(value = "data/vento", method = RequestMethod.GET)
    public ResponseEntity<Object> getVento() {
        for (int key : listener.getVelocVento().keySet()) {
            finalVelocVento.put(key, listener.getVelocVento().get(key).toArray());
        }
        return new ResponseEntity<>(finalVelocVento, HttpStatus.OK);
    }

    @ApiOperation(value = "Get radiation levels from sensor")
    @RequestMapping(value = "data/rad", method = RequestMethod.GET)
    public ResponseEntity<Object> getRad() {
        for (int key : listener.getRad().keySet()) {
            finalRad.put(key, listener.getRad().get(key).toArray());
        }
        return new ResponseEntity<>(finalRad, HttpStatus.OK);
    }

    @ApiOperation(value = "Get pH levels from sensor")
    @RequestMapping(value = "data/ph", method = RequestMethod.GET)
    public ResponseEntity<Object> getPh() {
        for (int key : listener.getPh().keySet()) {
            finalPh.put(key, listener.getPh().get(key).toArray());
        }
        return new ResponseEntity<>(finalPh, HttpStatus.OK);
    }

    @ApiOperation(value = "Get humidity level from sensor")
    @RequestMapping(value = "data/humidade", method = RequestMethod.GET)
    public ResponseEntity<Object> getHumidade() {
        for (int key : listener.getHumidade().keySet()) {
            finalHumidade.put(key, listener.getHumidade().get(key).toArray());
        }
        return new ResponseEntity<>(finalHumidade, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all actions performed from windows, heater etc")
    @RequestMapping(value = "data/acoes", method = RequestMethod.GET)
    public ResponseEntity<Object> getAcoes() {
        for (int key : listener.getAcoes().keySet()) {
            finalAcoes.put(key, listener.getAcoes().get(key).toArray());
        }
        return new ResponseEntity<>(finalAcoes, HttpStatus.OK);
    }

    @ApiOperation(value = "Insert a new sensor into configuration file")
    @RequestMapping(value = "/sensor/{_id}/{tipo}/{estufa}", method = RequestMethod.PUT)
    public ResponseEntity<String> putIdSensor(@PathVariable("_id") String _id,
                                              @PathVariable("tipo") String tipo,
                                              @PathVariable("estufa") String estufa) {
        sensores.put("_id", _id);
        sensores.put("tipo", tipo);
        sensores.put("estufa", estufa);
        allSensores.put("sensores", sensores);
        log.error(allSensores.toString());
        return new ResponseEntity<>("Data is updated successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Confirm new sensor and write it to .json file")
    @RequestMapping(value = "/save_sensor", method = RequestMethod.POST)
    public ResponseEntity<String> post() throws IOException {
        log.error(allSensores.toString());
        if (allSensores.containsKey("sensores")) {
            log.error("WE ARE HERE!");
            writer.write(allSensores.toString());
            writer.close();
            return new ResponseEntity<>(allSensores.toString(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Error while creating data", HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Remove a sensor from configuration file")
    @RequestMapping(value = "/delete_sensor/{_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("_id") String _id) {
        for (String key : allSensores.keySet()) {
            if (Arrays.toString(allSensores.get(key).toArray()).equals(_id)) {
                allSensores.remove(key, _id);
            }
        }
        return new ResponseEntity<>("Delete sucessfull", HttpStatus.OK);
    }

    @RequestMapping(value = "/login/{user}/{pass}/{role}", method = RequestMethod.GET)
    public ResponseEntity<String> login(@PathVariable("user") String user,
                                        @PathVariable("pass") String pass,
                                        @PathVariable("role") String role) {
        String a = repository.findByMail(user);
    return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @RequestMapping(value = "/create/{user}/{pass}/{role}", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@PathVariable("user") String user,
                                             @PathVariable("pass") String pass,
                                             @PathVariable("role") String role) {
        repository.save(new Users(user, pass, role));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
