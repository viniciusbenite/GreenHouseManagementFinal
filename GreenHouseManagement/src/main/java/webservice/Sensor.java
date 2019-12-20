package webservice;

import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;

public class Sensor {

//    @Id
//    public ObjectId _id;

    private String _id;

    private String tipo;

    private String estufa;

    public Sensor(String _id, String tipo, String estufa) {
        this._id = _id;
//        this._id = _id;
        this.tipo = tipo;
        this.estufa = estufa;
    }

    public String  get_id() {return this._id; }
    public String getTipo() { return  this.tipo; }
    public String getEstufa() { return this.estufa; }

    public void set_id(String _id) { this._id = _id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEstufa(String estufa) { this.estufa = estufa; }

}
