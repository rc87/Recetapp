package dad.recetapp.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import dad.recetapp.MainPrueba;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.services.items.SeccionItem;

public class SeccionesController {
	private MainPrueba main = new MainPrueba();
	private SeccionItem seccion;
	private Tab tab;
	
	private List<IngredienteItem> ingredientes = new ArrayList<IngredienteItem>();
	private ObservableList<IngredienteItem> ingredientesList = FXCollections.observableArrayList();
	
	private List<InstruccionItem> instrucciones = new ArrayList<InstruccionItem>();
	private ObservableList<InstruccionItem> instruccionesList = FXCollections.observableArrayList();
	
	@FXML
	public TableView<IngredienteItem> ingredientesTable;	
	@FXML
	public TableView<InstruccionItem> instruccionesTable;
	
	@FXML
	private TableColumn<IngredienteItem, Integer> ingredientesTableCantidadCol;
	@FXML
	private TableColumn<IngredienteItem, String> ingredientesTableMedidaCol;
	@FXML
	private TableColumn<IngredienteItem, String> ingredientesTableTipoCol;
	
	@FXML
	private TableColumn<InstruccionItem, Integer> instruccionesTableOrdenCol;
	@FXML
	private TableColumn<InstruccionItem, String> instruccionesTableDescripcionCol;

	
	@FXML
	private TextField seccionText;
	
	
	
	
	
	
	
	
	
	@FXML
	public void initialize() {
		
	}
	


	public void asociarSeccion(SeccionItem seccion, CrearRecetaController crearRecetaController, Tab tab) {
		this.seccion = seccion;
		this.tab = tab;
		cargarTablaIngredientes();
		cargarTablaInstrucciones();
	}
	
	public void asociarSeccion(SeccionItem seccion, EditarRecetaController editarRecetaController, Tab tab) {
		this.seccion = seccion;
		this.tab = tab;
		ingredientes = seccion.getIngredientes();
		instrucciones = seccion.getInstrucciones();
		cargarTablaIngredientes();
		cargarTablaInstrucciones();
		seccionText.setText(seccion.getNombre());
	}
	
	public SeccionItem getSeccion() {
		return seccion;
	}
		
	public void cargarTablaIngredientes() {
		
		ingredientesTableCantidadCol.setCellValueFactory(
				new PropertyValueFactory<IngredienteItem, Integer>("cantidad"));
		ingredientesTableMedidaCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngredienteItem,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<IngredienteItem, String> param) {
				return new SimpleStringProperty(param.getValue().getMedida().getNombre());
			}
		});
		ingredientesTableTipoCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngredienteItem,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<IngredienteItem, String> param) {
				return new SimpleStringProperty(param.getValue().getTipo().getNombre());
			}
		});
		
		ingredientesList.clear();
		ingredientesList = FXCollections.observableArrayList(ingredientes);
		ingredientesTable.setItems(ingredientesList);
		
	}

	public void cargarTablaInstrucciones() {
		
		instruccionesTableOrdenCol.setCellValueFactory(
				new PropertyValueFactory<InstruccionItem, Integer>("orden"));
		instruccionesTableDescripcionCol.setCellValueFactory(
				new PropertyValueFactory<InstruccionItem, String>("descripcion"));
		
		instruccionesList.clear();
		instruccionesList = FXCollections.observableArrayList(instrucciones);
		instruccionesTable.setItems(instruccionesList);
		
		
	}
	
	
	
	@FXML
	private void anadirIngredienteActionPerformed(){
		main.showAnadirIngrediente(this);
	}
	
	@FXML
	private void editarIngredienteActionPerformed(){
		IngredienteItem ingrediente = ingredientesTable.getSelectionModel().getSelectedItem();
		if (ingrediente != null ) {
			main.showEditarIngrediente(this, ingrediente);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar un ingrediente para poder editarlo");
			alert.showAndWait();
		}
		
	}
	
	@FXML
	private void eliminarIngredienteActionPerformed(){
		IngredienteItem ingrediente = ingredientesTable.getSelectionModel().getSelectedItem();
		if (ingrediente != null) {
			ingredientes.remove(ingrediente);
			cargarTablaIngredientes();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar un ingrediente antes de eliminarlo");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void anadirInstruccionActionPerformed(){
		main.showAnadirInstruccion(this);
	}
	
	@FXML
	private void editarInstruccionActionPerformed(){
		InstruccionItem instruccion = instruccionesTable.getSelectionModel().getSelectedItem(); 
		if (instruccion != null ) {
			main.showEditarInstruccion(this, instruccion);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una instrucción para poder editarla");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void eliminarInstruccionActionPerformed(){
		
	}
	
	@FXML
	private void actualizarNombreTab() {
		seccion.setNombre(seccionText.getText());
		tab.setText(seccionText.getText());
	}

	
	public List<IngredienteItem> getIngredientesList() {
		return ingredientes;
	}
	
	public List<InstruccionItem> getInstruccionesList() {
		return instrucciones;
	}

}
