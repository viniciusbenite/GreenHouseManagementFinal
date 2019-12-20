package webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public final class CustomMessage implements Serializable {

    private final int sensor;
    private final double data;
    private final String tipo;
    private final int estufa;
//    private final double temp_ext;
//    private final double wind_speed;
//    private final double ph_solo;
//    private final double temp_interna;

    public CustomMessage(@JsonProperty("Sensor") int sensor,
                         @JsonProperty("Temperatura interna") String tipo,
                         @JsonProperty("Dado coletado") double data,
                         @JsonProperty("Id da estufa") int estufa) {
        this.sensor = sensor;
        this.tipo = tipo;
        this.data = data;
        this.estufa= estufa;
    }

    public int getSensor() {
        return sensor;
    }

    public double getData() {
        return data;
    }
    
    public int getEstufa() {
        return estufa;
    }

    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        return "CustomMessage{" +
                "sensor=" + sensor +
                ", data=" + data +
                ", tipo=" + tipo +
                ", estufa=" + estufa +
                '}';
    }
}
