package dad.recetapp.ui;

import java.util.List;

import dad.recetapp.MainPrueba;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.TipoIngredienteItem;
import dad.recetapp.utils.IntegerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class AnadirIngredientesController {
	
	private MainPrueba main = new MainPrueba();
	private SeccionesController seccionesController;
	
	private IngredienteItem ingrediente;
	
	//Medidas
	private List<MedidaItem> medidas;
	private ObservableList<MedidaItem> medidasList;
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
			}catch(ServiceException e){
				e.printStackTrace();
			}

		tipos_ingredientesList = FXCollections.observableList(tipo_ingrediente);
			//Cuidado!! al iniciarlizar no hacerlo a null porque da error
		tipos_ingredientesString = FXCollections.observableArrayList();
			
			for(int i=0;i<tipos_ingredientesList.size();i++){
				
				tipos_ingredientesString.add(tipos_ingredientesList.get(i).getNombre());
							}
			
			tiposComboBox.setItems(tipos_ingredientesString);
			tiposComboBox.getSelectionModel().select(0);
			tiposComboBox.setVisibleRowCount(10);
			
		
		
	}


	private void cargarMedidasComboBox() {
		
		try{
			medidas = ServiceLocator.getMedidasService().listarMedidas();
			}catch(ServiceException e){
				e.printStackTrace();
			}
		
			medidasList = FXCollections.observableList(medidas);
			//Cuidado!! al iniciarlizar no hacerlo a null porque da error
			medidasString = FXCollections.observableArrayList();
			
			for(int i=0;i<medidasList.size();i++){
				medidasString.add(medidasList.get(i).getNombre());
				}
			
			medidasComboBox.setItems(medidasString);
			medidasComboBox.getSelectionModel().select(0);
			medidasComboBox.setVisibleRowCount(10);
		
		
	}
	

	
	@FXML
	private void cancelarButtonActionPerformed(){
		main.cerrarAnadirIngrediente();
	}
	
	
	@FXML
	private void anadirButtonActionPerformed(){
		if (cantidadTextField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe insertar una cantidad para añadir un ingrediente");
			alert.showAndWait();
		} else {
			if (!IntegerUtils.isInteger(cantidadTextField.getText())){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Advertencia");
				alert.setHeaderText(null);
				alert.setContentText("Sólo se puede introducir números en el campo 'Cantidad'");
				alert.showAndWait();
				cantidadTextField.clear();
			} else {
				ingrediente = new IngredienteItem();
				ingrediente.setCantidad(Integer.valueOf(cantidadTextField.getText()));
				ingrediente.setMedida(
						medidasList.get(medidasComboBox.getSelectionModel().getSelectedIndex()));
				ingrediente.setTipo(
						tipos_ingredientesList.get(tiposComboBox.getSelectionModel().getSelectedIndex()));
				
				seccionesController.getIngredientesList().add(ingrediente);
				seccionesController.cargarTablaIngredientes();
				seccionesController.getSeccion().setIngredientes((seccionesController.getIngredientesList()));
				
				main.cerrarAnadirIngrediente();
			}
			
		}
		
		
	}


	public void cargarSeccionesController(SeccionesController seccionesController) {
		this.seccionesController = seccionesController;
		
	}
	
	
	
		

}
