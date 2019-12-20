package webservice;

public class Aquecedor {

    String aquecedor;
    String id_estufa;
    boolean state;
    double min = 23;
    double max = 27;


    public Aquecedor(String aquecedor, String id_estufa) {
        this.aquecedor = aquecedor;
        this.id_estufa = id_estufa;
    }

    public void on() {
    	min= 24;
    	max= 27;
        state = true;
        System.out.printf("Aquecedor %s is on\n", aquecedor);
    }

    public void off() {
    	min=23;
    	max= 26;
        state = false;
        System.out.printf("Aquecedor %s is off\n", aquecedor);
    }
    
    public Double getmin() {
		return min; 	
    }
    
    public Double getmax() {
    	return max;
    }
    
    public Boolean getState() {
    	return state;
    }

    @Override
    public String toString() {
        return aquecedor;
    }
}

class AquecedorOnCommand implements Command
{
    Aquecedor aquecedor;

    public AquecedorOnCommand(Aquecedor aquecedor)
    {
        this.aquecedor = aquecedor;
    }
    public void execute()
    {
        aquecedor.on();
    }
}


class AquecedorOffComand implements Command {
    Aquecedor aquecedor;

    public AquecedorOffComand(Aquecedor aquecedor) {
        this.aquecedor = aquecedor;
    }

    public void execute() {
        aquecedor.off();

    }

}




