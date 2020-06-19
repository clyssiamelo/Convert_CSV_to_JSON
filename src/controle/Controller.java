package controle;

import java.util.List;

import java.util.Vector;

import org.apache.commons.csv.CSVRecord;

import Trabalhador.LerCSV;
import Trabalhador.ParseJson;
import Trabalhador.SalvaJson;
import controle.InterfaceCSV;
import controle.InterfaceJSON;

import java.io.File;



    

    public class Controller implements InterfaceCSV, InterfaceJSON{
 

       	private List<CSVRecord> csvQueue = null;
    	private List<String> jsonQueue = null;
    	private boolean continuaLeituraCSV = false;
    	private File arquivoCSV;
    	private File arquivoJson;
		private int quantidadeRegistros;
		private int RegistrosLidos;
		private File dirDestinado;
    	
   	    	
    	
    	public Controller(File arquivoCSV, File arquivoJson) {
    		
    		//DADOS DE INÍCIO
    		this.arquivoCSV = arquivoCSV;
    		this.arquivoJson =  arquivoJson;
    		
    		
    		//Inicializacao das filas
    		this.csvQueue = new Vector<CSVRecord>();
    		this.jsonQueue = new Vector<String>();

    	}
    	
  		
    		public void inicia() {
    			
    			//INICIANDO A THREAD DE Leitura e Tratamento do CSV
        		this.LerCSV(arquivoCSV);
        		
        		//Parse do arquivo JSON
        		this.parseJSON();
        		
        		    	
    		}
    		
      	
    	
    	//Leitura de arquivos em uma Thread
    	
  		private void LerCSV(File arquivoCSV) {
    		
    		//3. Implemente um processo leitura (Classe LerCSV),
    		//que leia o arquivo CSV e coloque os registros em uma fila “csvQueue”.
    		
  			LerCSV lerCSV = new LerCSV(this, arquivoCSV);
  			
  			quantidadeRegistros = lerCSV.getQtdeRegistros();
  			 
  			
  			Thread leCSV = new Thread(lerCSV);
  			leCSV.setName("Thread-CSV");
  			leCSV.start();
  			
    		
    		
    		}
    		
    	
    	
    	

    	
    //////  OPERAÇÕES GERAIS (CSV e JSON) \\\\\\\\	
      	
    	//Este método verifica se a fila ainda tem registro
    	
    	@Override
		public synchronized boolean emOperacao() {
    		return csvQueue.size() > 0 || continuaLeituraCSV;
    	}
    	
    	private void parseJSON() {
    		Thread t1 = new Thread(new ParseJson(this));
      		//Thread t2 = new Thread(new ParseJson(this));
      		//Thread t3 = new Thread(new ParseJson(this));
      		//Thread t4 = new Thread(new ParseJson(this));
      		
      		t1.start();
      		//t2.start();
      		//t3.start();
      		//t4.start();
      		
      		try {
      			t1.join();
          	//	t2.join();
          	//	t3.join();
          	//	t4.join();
          		
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
    		
    	}
    	
    	
	//////////////////////// OPERAÇÕES JSON \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		//Colocar registro na fila Json p/ salvar
    	@Override
		public synchronized CSVRecord recebeCSV() {
    		if (csvQueue.size() > 0) {
    			return csvQueue.remove(0);
    		}
    		return null;
    		
    	}

    	
    	
    	@Override
		public synchronized void addJson(String json){
    		jsonQueue.add(json);
    	}

    	
    	
 
  
    	
 	////////////////////// OPERAÇÕES CSV \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
      

		@Override
		public synchronized void setContinuaLeituraCSV(boolean terminou) {
			continuaLeituraCSV = terminou;
			System.out.println("Término: "+ continuaLeituraCSV);
			
		}
		
		 

		
		public synchronized boolean isContinuaLeituraCSV() {
			return this.continuaLeituraCSV;
			
		}

		
		// colocando os registros em uma fila “csvQueue”.
		@Override
		public synchronized void addRegistroCSV(CSVRecord csvRecord) {
			this.csvQueue.add(csvRecord);
    		this.RegistrosLidos++;
			
			//System.out.println(csvRecord);
    		System.out.println("Colocando os Registros CSV na fila ");
    		//txtAreaResult.appendText("Adicionado registro na fila" + csvRecord.toString());
			
		}

		public synchronized int getQtdeRegistros() {
			return this.quantidadeRegistros;
		}
		
		public synchronized int getRegistrosLidos() {
			return this.RegistrosLidos;
		}
		
		public void setDirDestinado(File direDestinado) {
			dirDestinado = direDestinado;
		}
		
		  /*    
	    public void initialize(URL location, ResourceBundle resources) {
		    	//Chama o evento da conversao botao desabilitado, após o arquivo ser selecionado
		    	
	    	btnOK.setDisable(true);
		    	
		    }

		*/

}
    

