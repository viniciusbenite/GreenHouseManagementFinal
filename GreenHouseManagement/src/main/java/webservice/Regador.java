package webservice;

public class Regador {

    String regador;
    String id_estufa;
    boolean state;
    double min = 300;
    double max = 600;

    public Regador(String regador, String id_estufa) {
        this.regador = regador;
        this.id_estufa = id_estufa;
    }

    public void on() {
        state = true;
    	min = 400;
    	max=600;
        System.out.printf("Regador %s is on\n", regador);
    }

    public void off() {
        state = false;
        min=300;
        max= 600;
        System.out.printf("Regador %s is off\n", regador);
    }
    
    public Double getmin() {
		return min; 	
    }
    
    public Double getmax() {
    	return max;
    }

    @Override
    public String toString() {
        return regador;
    }
}

class RegadorOnCommand implements Command
{
    Regador regador;

    public RegadorOnCommand(Regador regador)
    {
        this.regador = regador;
    }
    public void execute()
    {
        regador.on();
    }
}



class RegadorOffComand implements Command {
    Regador regador;

    public RegadorOffComand(Regador regador) {
        this.regador = regador;
    }

    public void execute() {
        regador.off();

    }
}



