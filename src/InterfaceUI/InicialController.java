package InterfaceUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Trabalhador.SalvaJson;
import controle.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InicialController implements javafx.fxml.Initializable{



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	} 
	
///////////////////////// FXML \\\\\\\\\\\\\\\\\\\\\\\\\\\
@FXML private Button btnBusca;
@FXML private Label lbl_Mensagem;
@FXML private TextField campoNome_Arquivo;
@FXML private TextField campoNome_ArquivoJSON;
@FXML private Label lbl_MsgAbrir;
@FXML private Button btnSalvar;
@FXML private Button btnOK;
@FXML public TextArea txtAreaResult;
@FXML private Button btnBuscaJSON;
@FXML private ProgressBar PrB_Processo;


private File arquivoCSV;
private File arquivoJson;
private Controller controller;
private int totalLinhas;
private File dirDestinado;

@FXML
private void ArquivoCSV(ActionEvent event) {
 
	btnOK.setDisable(true);
	
	FileChooser fileChooser = new FileChooser();
	
	 //Adicionando Filtro por extensao de arquivos 
	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));	
	
	//Abrir janela do explorador para selecao de arquivo com diretorio inicial definido
	fileChooser.setInitialDirectory(new File("/Users/clyssiamelo/Downloads/brasil"));
	
	fileChooser.setTitle("Selecione o arquivo CSV para convers�o...");
	
	//Pega os detalhes do stage
	arquivoCSV = fileChooser.showOpenDialog((Stage)((Node) event.getSource()).getScene().getWindow());
	
	
	//Verificacao para saber se o arquivo está vazio
	if (arquivoCSV != null) {
		campoNome_Arquivo.appendText("\n Arquivo de Origem: " + arquivoCSV.toString());
		verificaArquivosSelecionados();
		}

	
	}

@FXML
void btn_busca_click(ActionEvent event) {
   
	try {
	
	   ArquivoCSV(event);

   }
  catch (Exception e) {
	e.printStackTrace();
}	
   	
	
	
}


private void verificaArquivosSelecionados() {
		if (arquivoCSV != null & arquivoJson != null ) {
			btnOK.setDisable(false);
		}
		
	}



@FXML
private void ArquivoJSON(ActionEvent event) {
    	
	btnOK.setDisable(true);
	
	FileChooser fileChooser = new FileChooser();
	
	
	 //Adicionando Filtro por extensao de arquivos 
	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

	//Definir diretorio inicial
	fileChooser.setInitialDirectory(new File("/Users/clyssiamelo/Downloads/brasil"));
	
	fileChooser.setTitle("Selecione o arquivo JSON para salvar...");

	//Abrir janela do explorador para selecao de arquivo
	arquivoJson = fileChooser.showOpenDialog((Stage)((Node) event.getSource()).getScene().getWindow());
	
	//Verificacao para saber se o arquivo está vazio
	if (arquivoJson != null) {
		campoNome_ArquivoJSON.appendText("\n Arquivo de Destino: " + arquivoJson.toString());
		verificaArquivosSelecionados();
		}

	
	}

@FXML
void btn_buscajson_click(ActionEvent event) {
	ArquivoJSON(event);
}



@FXML
void btn_salvar_click(ActionEvent event) {

		//Salva os registros Json colocados na fila
		new SalvaJson();
}




@FXML
void btn_ok_click(ActionEvent event) {

Converter(event);

}



@FXML 
private void Converter(ActionEvent event) {
	controller = new Controller(arquivoCSV, arquivoJson);
	controller.inicia();
	
	btnOK.setDisable(true);
	
	
	//Processo para controlar a quantidade de registros
	do {
		totalLinhas =  controller.getQtdeRegistros();
		if (!controller.isContinuaLeituraCSV()) {
			break;
		}
		
	} while (totalLinhas == 0);

		this.atualizaCSV();

}

	


	private void atualizaCSV(){
		Task<Void> taskCSV = new Task<Void>() {

		
		@Override
		protected Void call() throws Exception{
			
			int lidos = 0;
			
			do {
				lidos = controller.getRegistrosLidos();
				updateProgress(lidos, totalLinhas);
			} while (controller.isContinuaLeituraCSV());
			
			updateMessage("Total de linha: " + totalLinhas + " Linhas Lidas " + lidos);
			
			return null;
			
		}
		
			
	
	@Override
	protected void succeeded() {
	super.succeeded();
	updateMessage("Concluído com Sucesso");
	}
	
	@Override
	protected void cancelled() {
	super.cancelled();
	updateMessage("Cancelado");
	}
	
	@Override
	protected void failed() {
	super.failed();
	updateMessage("Falhou");
	}
};

	taskCSV.messageProperty().addListener(new ChangeListener<String>() {
		
		@Override
		public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
			txtAreaResult.appendText("\n" + taskCSV.getMessage());
			}
		});
	
	
	
	PrB_Processo.progressProperty().bind(taskCSV.progressProperty());
	
	new Thread(taskCSV).start();
	}
	
	
	
	private File getPath() {
		try {
	JFrame parentFrame = new JFrame();
	JFileChooser file_chooser = new JFileChooser();
	file_chooser.setDialogTitle("Onde Deseja Salvar o Arquivo CSV");
	int userSelection = file_chooser.showSaveDialog(parentFrame);
	if (userSelection == JFileChooser.APPROVE_OPTION) {
	    File fileToSave = file_chooser.getSelectedFile();
		controller.setDirDestinado(fileToSave);
	    dirDestinado = fileToSave;
		txtAreaResult.appendText(dirDestinado + "\n");
	    return fileToSave;
	}
	return null;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}
	
	
	
	


}
	
	
	
	
	
	
	
