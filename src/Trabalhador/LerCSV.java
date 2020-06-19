package Trabalhador;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import controle.InterfaceCSV;;



	public class LerCSV implements Runnable{
		
		private InterfaceCSV controler;
		private File arquivoCSV;

	
		public LerCSV(InterfaceCSV controler, File arquivoCSV) {
			
			this.controler = controler;
			this.arquivoCSV = arquivoCSV;
		}

		
	
	public int getQtdeRegistros() {
		try (LineNumberReader lnr = new LineNumberReader(new FileReader(arquivoCSV));){
			
			lnr.skip(Long.MAX_VALUE);
			return lnr.getLineNumber() - 1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
		
		
		
		

@Override
public void run() {

	//3 - Implemente um processo leitura, que leia o arquivo CSV e coloque os registros em uma fila “csvQueue”.
//Leitura de arquivos acontecendo em uma Thread
	
try (Reader reader = new FileReader(arquivoCSV);){
	CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
	
	controler.setContinuaLeituraCSV(true);
	
	for (CSVRecord csvRecord : parser) {
		
		if (parser.getCurrentLineNumber() == 1 )
			continue;
			controler.addRegistroCSV(csvRecord);
		
	}
	
} catch (IOException e) {
	e.printStackTrace();

}

	controler.setContinuaLeituraCSV(false);
	
	
	}
	
}

