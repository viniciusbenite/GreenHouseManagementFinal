package webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class ControloRemoto implements Serializable{

    private boolean estado_janela1;
    private boolean estado_janela2;
    private boolean regador;
    private boolean luz1;
    private boolean luz2;
    private boolean luz3;
    private int estufa;

    ControloRemoto(@JsonProperty("Estufa") int estufa,
                   @JsonProperty("Estado Janela 1") boolean estado_janela1,
                   @JsonProperty("Estado Janela 2") boolean estado_janela2,
                   @JsonProperty("Estado regador") boolean regador,
                   @JsonProperty("Estado luz 1") boolean luz1,
                   @JsonProperty("Estado luz 2") boolean luz2,
                   @JsonProperty("Estado luz 3") boolean luz3) {
        this.estufa = estufa;
        this.estado_janela1 = estado_janela1;
        this.estado_janela2 = estado_janela2;
        this.regador = regador;
        this.luz1 = luz1;
        this.luz2 = luz2;
        this.luz3 = luz3;
    }

    public int getEstufa() { return estufa; }

    public boolean isEstado_janela1() {
        return estado_janela1;
    }

    public boolean isEstado_janela2() {
        return estado_janela2;
    }

    public boolean isRegador() {
        return regador;
    }

    public boolean isLuz1() {
        return luz1;
    }

    public boolean isLuz2() {
        return luz2;
    }

    public boolean isLuz3() {
        return luz3;
    }

    @Override
    public String toString() {
        return "ControloRemoto{" +
                "estado_janela1=" + isEstado_janela1() +
                ", estado_janela2=" + isEstado_janela2() +
                ", regador=" + isRegador() +
                ", luz1=" + isLuz1() +
                ", luz2=" + isLuz2() +
                ", luz3=" + isLuz3() +
                '}';
    }
}
