package Trabalhador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


//5. Implemente o processo que salva os registros JSON em arquivo
public class SalvaJson {
	
	public void salva(String arquivoJson, List<String> jsonQueue) {
		List<String> lista  = jsonQueue;
		Path path = Paths.get(arquivoJson);
		try {
			Files.write(path, lista, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
