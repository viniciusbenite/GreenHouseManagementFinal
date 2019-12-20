package webservice;

public class Luz {

    String luz;
    String id_estufa;
    boolean state;
    double min = 300;
    double max = 600;

    public Luz(String luz, String id_estufa) {
        this.luz = luz;
        this.id_estufa = id_estufa;
    }

    public void on( ) {
        state = true;
    	min = 400;
    	max=600;
        System.out.printf("Luz %s is on\n", luz);
    }

    public void off() {
        state = false;
        min=300;
        max= 600;
        System.out.printf("Luz %s is off\n", luz);
    }
    
    public Double getmin() {
		return min; 	
    }
    
    public Double getmax() {
    	return max;
    }

    @Override
    public String toString() {
        return luz;
    }
}


class LuzOnCommand implements Command
{
    Luz luz;

    public LuzOnCommand(Luz luz)
    {
        this.luz = luz;
    }

    public void execute()
    {
        luz.on();
    }
}

class LuzOffComand implements Command {
    Luz luz;

    public LuzOffComand(Luz luz) {
        this.luz = luz;
    }

    public void execute() {
        luz.off();

    }

}
