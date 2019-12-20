package webservice;

public class Janela {

    String janela;
    String id_estufa;
    boolean state;
    double min = 300;
    double max = 600;

    public Janela(String janela, String id_estufa) {
        this.janela = janela;
        this.id_estufa = id_estufa;
    }

    public void on() {
    	min = 400;
    	max=600;
        state = true;
        System.out.printf("Janela %s aberta\n", janela);
    }

    public void off() {
        state = false;
        min=300;
        max= 500;
        System.out.printf("Janela %s fechada\n", janela);
    }
    
    public Double getmin() {
		return min; 	
    }
    
    public Double getmax() {
    	return max;
    }

    @Override
    public String toString() {
        return janela;
    }
}


class JanelaOnCommand implements Command
{
    Janela janela;

    public JanelaOnCommand(Janela janela)
    {
        this.janela = janela;
    }
    public void execute()
    {
        janela.on();
    }
}


class JanelaOffComand implements Command {
    Janela janela;

    public JanelaOffComand(Janela janela) {
        this.janela = janela;
    }

    public void execute() {
        janela.off();

    }

}

