package controle;

import org.apache.commons.csv.CSVRecord;

public interface InterfaceJSON {

	//Colocar registro na fila Json p/ salvar
	void addJson(String json);

	CSVRecord recebeCSV();

	//Este método verifica se a fila ainda tem registro
	boolean emOperacao();

}