package application;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class escreveArquivo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			FileOutputStream arquivo = new FileOutputStream("arquivo.txt");
			PrintWriter pr = new PrintWriter(arquivo);
			
			pr.println("Lnha 1 coluna 1: teste clyssia");
			pr.println("linha 2 coluna 2 clyssia");
			
			pr.close();
			arquivo.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
