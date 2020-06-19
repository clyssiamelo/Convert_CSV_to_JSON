package Trabalhador;


import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;

import controle.InterfaceJSON;
import entidade.Registro;

import application.main;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ParseJson implements Runnable {
	
	@FXML
	public TextArea txtAreaResult;
	
	private InterfaceJSON controler;
	
	public ParseJson(InterfaceJSON interfaceJSON) {
		this.controler = interfaceJSON;
	}

	@Override
	public void run() {
		

		do {
			CSVRecord csvRecord = controler.recebeCSV();
			
			if (csvRecord == null) {
				synchronized (this) {
					try {
						this.wait(200);
						System.out.println(Thread.currentThread().getName() + "- Foi para Wait. ");
						
						
					} catch (InterruptedException e) {
						System.out.println("ParseData: " + e.getMessage());
					}
				}
			}else {
				String json = converteParaJson(csvRecord);
				controler.addJson(json);
				System.out.println(Thread.currentThread().getName());
				
			}
			
		} while (controler.emOperacao());
		// Enquanto a fila tiver registro, ele continua em operacao
		//Ou seja, caso a fila esteja vazia, ele encerra a thread

	}
	
	
	
	//4. Implemente o processo que converte os registros CSV (csvQueue) para JSON
	
	private String converteParaJson(CSVRecord csvRecord) {
		
		String json = null;
		
		Registro registro = new Registro();
		
		registro.setNumber(csvRecord.get(0));
		registro.setGender(csvRecord.get(1));
		registro.setNameSet(csvRecord.get(2));
		registro.setTitle(csvRecord.get(3));
		registro.setGivenName(csvRecord.get(4));
		registro.setSurname(csvRecord.get(5));
		registro.setStreetAddress(csvRecord.get(6));
		registro.setCity(csvRecord.get(7));
		registro.setState(csvRecord.get(8));
		registro.setZipCode(csvRecord.get(9));
		registro.setCountryFull(csvRecord.get(10));
		registro.setEmailAddress(csvRecord.get(11));
		registro.setUsername(csvRecord.get(12));
		registro.setPassword(csvRecord.get(13));
		registro.setTelephoneNumber(csvRecord.get(14));
		registro.setBirthday(csvRecord.get(15));
		registro.setCCType(csvRecord.get(16));
		registro.setCCNumber(csvRecord.get(17));
		registro.setCVV2(csvRecord.get(18));
		registro.setCCExpires(csvRecord.get(19));
		registro.setNationalID(csvRecord.get(20));
		registro.setColor(csvRecord.get(21));
		registro.setKilograms(csvRecord.get(22));
		registro.setCentimeters(csvRecord.get(23));
		registro.setGUID(csvRecord.get(24));
		
		
		Gson gson = new Gson();
		json = gson.toJson(registro, Registro.class);
		
		return json;
	}
	
}
