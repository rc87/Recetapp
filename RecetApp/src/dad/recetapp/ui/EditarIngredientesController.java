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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class EditarIngredientesController {
	
	private MainPrueba main = new MainPrueba();
	private SeccionesController seccionesController;
	
	private IngredienteItem ingrediente;
	
	//Medidas
	private List<MedidaItem> medidas;
	private ObservableList<MedidaItem> medidasList;
	private ObservableList<String> medidasString;
	
	private List<TipoIngredienteItem> tipoIngrediente;
	private ObservableList<TipoIngredienteItem> tipoIngredienteList;
	private ObservableList<String> tipoIngredienteString;


	
	@FXML
	private TextField cantidadTextField;
	@FXML
	private ComboBox<String> medidasComboBox;
	@FXML
	private ComboBox<String> tiposComboBox;
	
	
	@FXML
	public void initialize() {
		
	}


	private void cargarTipoIngredienteComboBox() {
		
		try{
			tipoIngrediente = ServiceLocator.getTiposIngredientesService().listarTiposIngredientes();
			}catch(ServiceException e){
				e.printStackTrace();
			}

		tipoIngredienteList = FXCollections.observableList(tipoIngrediente);
			//Cuidado!! al iniciarlizar no hacerlo a null porque da error
		tipoIngredienteString = FXCollections.observableArrayList();
			
			for(int i=0;i<tipoIngredienteList.size();i++){
				
				tipoIngredienteString.add(tipoIngredienteList.get(i).getNombre());
							}
			
			tiposComboBox.setItems(tipoIngredienteString);
			tiposComboBox.getSelectionModel().select(ingrediente.getTipo().getNombre());
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
			medidasComboBox.getSelectionModel().select(ingrediente.getMedida().getNombre());
			medidasComboBox.setVisibleRowCount(10);
		
		
	}
	
	
	@FXML
	private void cancelarButtonActionPerformed(){
		main.cerrarEditarIngrediente();
	}
	
	
	@FXML
	private void guardarButtonActionPerformed(){
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
				seccionesController.getIngredientesList().remove(ingrediente);
				ingrediente.setCantidad(Integer.valueOf(cantidadTextField.getText()));
				ingrediente.setMedida(
						medidasList.get(medidasComboBox.getSelectionModel().getSelectedIndex()));
				ingrediente.setTipo(
						tipoIngredienteList.get(tiposComboBox.getSelectionModel().getSelectedIndex()));
				
				seccionesController.getIngredientesList().add(ingrediente);
				seccionesController.cargarTablaIngredientes();
				seccionesController.getSeccion().setIngredientes((seccionesController.getIngredientesList()));
				
				main.cerrarEditarIngrediente();
			}
			
		}
		
		
	}


	public void cargarSeccionesController(SeccionesController seccionesController, IngredienteItem ingrediente) {
		this.seccionesController = seccionesController;
		this.ingrediente = ingrediente;
		
		cargarMedidasComboBox();
		cargarTipoIngredienteComboBox();
		cantidadTextField.setText("" + ingrediente.getCantidad());
		
	}
	
	
	
		

}