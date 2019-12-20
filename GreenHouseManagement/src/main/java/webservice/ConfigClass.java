package webservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class ConfigClass {

        ArrayList<Luz> luzList = new ArrayList<>();
        ArrayList<Janela> janelasList = new ArrayList<>();
        ArrayList<Regador> regadorList = new ArrayList<>();
        ArrayList<Aquecedor> aquecedorList = new ArrayList<>();
        Invoker ligar = new Invoker();
        Invoker desligar = new Invoker();
        
       
        public ConfigClass() {
        	
        	 try {
                 // create a reader
                 Reader reader = Files.newBufferedReader(Paths.get("src/main/java/webservice/config.json"));

                 //create ObjectMapper instance
                 ObjectMapper objectMapper = new ObjectMapper();

                 //read customer.json file into tree model
                 JsonNode parser = objectMapper.readTree(reader);

                 for (JsonNode estufa : parser.path("sensores")) {
                    if (estufa.path("tipo").asText().equals("rad")) {
                         Luz luz = new Luz(estufa.path("_id").asText(), estufa.path("estufa").asText());
                         luzList.add(luz);
                     }
                     if (estufa.path("tipo").asText().equals("veloc_vento")) {
                         Janela janela = new Janela(estufa.path("_id").asText(),  estufa.path("estufa").asText());
                         janelasList.add(janela);
                     }
                     if (estufa.path("tipo").asText().equals("humidade")) {
                         Regador regador = new Regador(estufa.path("_id").asText(),  estufa.path("estufa").asText());
                         regadorList.add(regador);
                     }
                     
       
                     if (estufa.path("tipo").asText().equals("temp_int")) {
                         Aquecedor aquecedor = new Aquecedor(estufa.path("_id").asText(),  estufa.path("estufa").asText());
                         aquecedorList.add(aquecedor);
                     }
                 }
                 for (Luz luz : luzList) {
                     Command luzOn = new LuzOnCommand(luz);
                     Command luzOff = new LuzOffComand(luz);
                     ligar.register(luz.toString(), luzOn);
                     desligar.register(luz.toString(), luzOff);
                 }
                 for (Janela janela : janelasList) {
                     Command janelaOpen = new JanelaOnCommand(janela);
                     Command janelaClose = new JanelaOffComand(janela);
                     ligar.register(janela.toString(), janelaOpen);
                     desligar.register(janela.toString(), janelaClose);
                 }
                 for (Regador regador : regadorList) {
                     Command regadorOn = new RegadorOnCommand(regador);
                     Command regadorOff = new RegadorOffComand(regador);
                     ligar.register(regador.toString(), regadorOn);
                     desligar.register(regador.toString(), regadorOff);
                 }
                 for (Aquecedor aquecedor : aquecedorList) {
                     Command aquecedorOn = new AquecedorOnCommand(aquecedor);
                     Command aquecedorOff = new AquecedorOffComand(aquecedor);
                     ligar.register(aquecedor.toString(), aquecedorOn);
                     desligar.register(aquecedor.toString(), aquecedorOff);
                 }
     ;

                 reader.close();

             } catch (Exception ex) {
                 ex.printStackTrace();
             }
        	
        }
        
    
        public Aquecedor getAquecedores(String aquecedor_id) throws IOException{
           for (Aquecedor aq : aquecedorList) {
        	   if (aq.aquecedor.equals(aquecedor_id)) {
             	  return aq;
        	   }
           }
           return null;
		}
        
        public Invoker getLigar() {
        	return ligar;
        }
        
        public Invoker getDesligar() {
        	return desligar;
        }
        

        public Regador getRegadores(String regador_id) throws IOException{
            for (Regador re : regadorList) {
         	   if (re.regador.equals(regador_id)) {
              	  return re;
         	   }
            }
            return null;
			}
        
		public Janela getJanelas(String janela_id) throws IOException{
	           for (Janela ja : janelasList) {
	        	   if (ja.janela.equals(janela_id)) {
	             	  return ja;
	        	   }
	           }
	           return null;
			}
		
		public Luz getLuzes(String luz_id) throws IOException{
	           for (Luz luz : luzList) {
	        	   if (luz.luz.equals(luz_id)) {
	             	  return luz;
	        	   }
	           }
	           return null;
			}
		
}