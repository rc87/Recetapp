package dad.recetapp.ui;

import java.util.List;

import dad.recetapp.Main;
import dad.recetapp.model.IngredienteItem;
import dad.recetapp.model.MedidaItem;
import dad.recetapp.model.TipoIngredienteItem;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiciosException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AnadirIngredientesController {
	
	private Main main = new Main();
	
	
	//Medidas
	private List<MedidaItem> medidas;
	private ObservableList<MedidaItem> medidasList;
	private int i;
	private ObservableList<String> medidasString;
	
	private List<TipoIngredienteItem> tipo_ingrediente;
	private ObservableList<TipoIngredienteItem> tipos_ingredientesList;
	private ObservableList<String> tipos_ingredientesString;


	
	@FXML
	private TextField cantidadTextField;
	@FXML
	private ComboBox<String> medidasComboBox;
	@FXML
	private ComboBox<String> tiposComboBox;
	
	
	@FXML
	private Button anadirButton;
	@FXML
	private Button cancelarButton;
	
	@FXML
	public void initialize() {
		
	
		cargarMedidasComboBox();
		cargarTipoIngredienteComboBox();
		
				
	}


	private void cargarTipoIngredienteComboBox() {

		
		try{
			tipo_ingrediente = ServiceLocator.getTiposIngredientesService().listarTiposIngredientes();
			}catch(ServiciosException e){
				e.printStackTrace();
			}

		tipos_ingredientesList = FXCollections.observableList(tipo_ingrediente);
			//Cuidado!! al iniciarlizar no hacerlo a null porque da error
		tipos_ingredientesString = FXCollections.observableArrayList();
			
			for(i=0;i<tipos_ingredientesList.size();i++){
				
				tipos_ingredientesString.add(tipos_ingredientesList.get(i).getNombre());
							}
			
			tiposComboBox.setItems(tipos_ingredientesString);
			tiposComboBox.setVisibleRowCount(4);
			tiposComboBox.getSelectionModel().select(1);
		
		
		
		
		
	}


	private void cargarMedidasComboBox() {

		try{
			medidas = ServiceLocator.getMedidasService().listarMedidas();
			}catch(ServiciosException e){
				e.printStackTrace();
			}

			medidasList = FXCollections.observableList(medidas);
			//Cuidado!! al iniciarlizar no hacerlo a null porque da error
			medidasString = FXCollections.observableArrayList();
			
			for(i=0;i<medidasList.size();i++){
				
				medidasString.add(medidasList.get(i).getNombre());
							}
			
			medidasComboBox.setItems(medidasString);
			medidasComboBox.setVisibleRowCount(4);
			medidasComboBox.getSelectionModel().select(1);
		
		
	}
	
	
	@FXML
	private void cancelarButtonActionPerformed(){
		main.cerrarAnadirNuevoIngrediente();
	}
	
	
	@FXML
	private void anadirButtonActionPerformed(){
		
		System.out.println("Cantidad: "+cantidadTextField.getText());
		System.out.println("Medidas: "+medidasComboBox.getValue());
		System.out.println("Tipo: "+tiposComboBox.getValue());
		
	}
	
	
	
		

}
