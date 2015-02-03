package dad.recetapp.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import dad.recetapp.*;
import dad.recetapp.model.CategoriaItem;
import dad.recetapp.model.MedidaItem;
import dad.recetapp.model.RecetaListItem;
import dad.recetapp.model.TipoAnotacionItem;
import dad.recetapp.model.TipoIngredienteItem;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiciosException;
import dad.recetapp.services.TiposAnotacionesService;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class RecetAppController {
	
	private Main main = new Main();

	//Recetas
	private List<RecetaListItem> recetas;
	private ObservableList<RecetaListItem> recetasList;
	
	//Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;
	
	//Ingredientes
	private List<TipoIngredienteItem> ingredientes;
	private ObservableList<TipoIngredienteItem> ingredientesList;
	
	//Medidas
	private List<MedidaItem> medidas;
	private ObservableList<MedidaItem> medidasList;
	
	//Anotaciones
	private List<TipoAnotacionItem> anotaciones;
	private ObservableList<TipoAnotacionItem> anotacionesList;
	
	
	
	@FXML
	public TableView<RecetaListItem> recetasTable;
	@FXML
	private TableView<CategoriaItem> categoriasTable;
	@FXML
	private TableView<TipoIngredienteItem> ingredientesTable;
	@FXML
	private TableView<MedidaItem> medidasTable;
	@FXML
	private TableView<TipoAnotacionItem> anotacionesTable;
	

	// Tabla de recetas
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableNombreCol;
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableParaCol;
	@FXML
	private TableColumn<RecetaListItem, Integer> recetasTableTiempoCol;
	@FXML
	private TableColumn<RecetaListItem, Date> recetasTableFechaCol;
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableCategoriaCol;
	
	
	
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableNombreCol2;
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableParaCol2;
	@FXML
	private TableColumn<RecetaListItem, Integer> recetasTableTiempoCol2;
	@FXML
	private TableColumn<RecetaListItem, Date> recetasTableFechaCol2;
	@FXML
	private TableColumn<RecetaListItem, String> recetasTableCategoriaCol2;
	
	
	

	// Columna de categorias
	@FXML
	private TableColumn<CategoriaItem, String> categoriasTableDescripcionCol;
	
	//Columna de ingredientes
	@FXML
	private TableColumn<TipoIngredienteItem, String> ingredientesTableNombreCol;
	
	//Tabla de medidas columnas
	@FXML
	private TableColumn<MedidaItem,String> medidasTableNombreCol;
	@FXML
	private TableColumn<MedidaItem,String> medidasTableAbreviaturaCol;
	
	//Tabla anotaciones columnas
	@FXML
	private TableColumn<TipoAnotacionItem,String> anotacionesTableDescripcionCol;
	
	
	
	//Botones
	@FXML
	private Button anadirButton;
	@FXML
	private Button editarButton;
	@FXML
	private Button eliminarButton;
	@FXML
	private Label recetasLabel;
	@FXML
	private Button categoriasAñadirButton;
	@FXML
	private Button ingredientesAnadirButton;
	

	
	
	//TextField
	@FXML
	private TextField categoriasDescripcionField;
	@FXML
	private TextField ingredientesNombreField;
	@FXML
	private TextField medidasNombreField;
	@FXML
	private TextField medidasAbreviaturasField;
	@FXML
	private TextField anotacionesDescripcionField;

	
	@FXML
	public void initialize() {

		System.out.println("iniciando RecetApp");

		// Cargamos la tabla recetas principal
		cargarTablaRecetas();

		cargarTablaCategorias();

		cargarTablaIngredientes();

		cargarTablaMedidas();

		cargarTablaAnotaciones();
	}

	private void cargarTablaRecetas() {
		
		
		recetasTableNombreCol.setCellValueFactory(new PropertyValueFactory<RecetaListItem, String>("nombre"));
		// recetasTableNombreCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableParaCol.setCellValueFactory(new PropertyValueFactory<RecetaListItem, String>("para"));
		// recetasTableParaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableTiempoCol.setCellValueFactory(new PropertyValueFactory<RecetaListItem, Integer>("tiempoTotal"));
		// recetasTableTiempoCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableFechaCol.setCellValueFactory(new PropertyValueFactory<RecetaListItem, Date>("fechaCreacion"));
		// recetasTableFechaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());
		//
		recetasTableCategoriaCol.setCellValueFactory(new PropertyValueFactory<RecetaListItem, String>("categoria"));
		// recetasTableCategoriaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		// Cargamos la tabla categorias

		try {
			recetas = ServiceLocator.getRecetasService().listarRecetas();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}

		recetasList = FXCollections.observableList(recetas);

		// Le damos el valor al label recetas
		recetasLabel.setText("Recetas: " + recetasList.size());

		recetasTable.setItems(recetasList);
		
		

	}

	private void cargarTablaCategorias() {

		categoriasTableDescripcionCol.setCellValueFactory(new PropertyValueFactory<CategoriaItem, String>("descripcion"));

		try {
			categorias = ServiceLocator.getCategoriasService().listarCategorias();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}

		categoriasList = FXCollections.observableList(categorias);
		categoriasTable.setItems(categoriasList);

	}
	
	private void cargarTablaIngredientes() {
		
		ingredientesTableNombreCol.setCellValueFactory(new PropertyValueFactory<TipoIngredienteItem, String>("nombre"));

		try {
			ingredientes = ServiceLocator.getTiposIngredientesService().listarTiposIngredientes();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
		ingredientesList = FXCollections.observableList(ingredientes);
		ingredientesTable.setItems(ingredientesList);

	}

	private void cargarTablaMedidas() {
		
		medidasTableNombreCol.setCellValueFactory(new PropertyValueFactory<MedidaItem,String>("nombre"));
		medidasTableAbreviaturaCol.setCellValueFactory(new PropertyValueFactory<MedidaItem,String>("abreviatura"));

		try {
			medidas = ServiceLocator.getMedidasService().listarMedidas();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
		
		medidasList = FXCollections.observableList(medidas);
		medidasTable.setItems(medidasList);
	}
	
	private void cargarTablaAnotaciones() {
		anotacionesTableDescripcionCol.setCellValueFactory(new PropertyValueFactory<TipoAnotacionItem,String>("descripcion"));

		try {
			anotaciones = ServiceLocator.getTiposAnotacionesService().listarTiposAnotaciones();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
		
		anotacionesList = FXCollections.observableList(anotaciones);
		anotacionesTable.setItems(anotacionesList);		
	}

	@FXML
	private void anadirButtonActionPerformed() {
		main.showAnadirReceta();
	}

	@FXML
	private void editarButtonActionPerformed() {
		main.showEditarReceta(recetasTable.getSelectionModel().getSelectedItem());
		
	}
	
	@FXML
	private void eliminarButtonActionPerformed(){
		
		
		try {
			ServiceLocator.getRecetasService().eliminarReceta(recetasTable.getSelectionModel().getSelectedItem().getId());
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
		
		recetasList.clear();
		cargarTablaRecetas();
		
	}
		
	@FXML
	public void vaciarYCargarTabla(){


		 recetasList.clear();
		cargarTablaRecetas();
		
	
		
	}
	
	
	
	
	
	
	
	
	
	//Panel Categorias
	//Añadir Categoria
	@FXML
	private void anadirCategoriaButtonActionPerformed(){
		
		CategoriaItem nuevaCategoria = new CategoriaItem();
		if(categoriasDescripcionField.getText().isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir una categoria sin nombre");
			alert.showAndWait();
		}else{
		nuevaCategoria.setDescripcion(categoriasDescripcionField.getText());
		try {
			ServiceLocator.getCategoriasService().crearCategoria(nuevaCategoria);
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		categoriasList.clear();
		cargarTablaCategorias();
		}
	}
	//Eliminar Categoria
	@FXML
	private void eliminarCategoriaButtonActionPerformed(){
		

		if(categoriasTable.getSelectionModel().getSelectedIndex()> 0){
		
		try {
			ServiceLocator.getCategoriasService().eliminarCategoria(categoriasTable.getSelectionModel().getSelectedItem().getId());
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
		categoriasList.clear();
		cargarTablaCategorias();
		
		}else{
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una categoria antes de eliminarla");
			alert.showAndWait();
		}
	}
	
	
	
	//Panel Ingredientes
	//Añadir Tipo de Ingrediente
	@FXML
	private void anadirTipoIngredienteButtonActionPerformed(){
		
		if(ingredientesNombreField.getText().isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir un tipo de ingrediente sin nombre");
			alert.showAndWait();
		}else{	
		TipoIngredienteItem tipoIngrediente = new TipoIngredienteItem();
		tipoIngrediente.setNombre(ingredientesNombreField.getText());
		try {
			ServiceLocator.getTiposIngredientesService().crearTipoIngrediente(tipoIngrediente);
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ingredientesList.clear();
		cargarTablaIngredientes();
		}
	}
	//Eliminar Tipo de Ingrediente
	@FXML
	private void eliminarTipoIngredienteButtonActionPerformed(){
		if(ingredientesTable.getSelectionModel().getSelectedIndex()> 0){
			
			try {
				ServiceLocator.getTiposIngredientesService().eliminarTipoIngrediente(ingredientesTable.getSelectionModel().getSelectedItem().getId());
			} catch (ServiciosException e) {
				e.printStackTrace();
			}
			ingredientesList.clear();
			cargarTablaIngredientes();
			
			}else{
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Advertencia");
				alert.setHeaderText(null);
				alert.setContentText("Debe seleccionar un tipo de ingrediente antes de eliminarlo");
				alert.showAndWait();
			}
		}
		
		
	//Panel Medidas
	//Añadir Medida
	@FXML
	private void anadirMedidaButtonActionPerformed(){
		
		if(medidasNombreField.getText().isEmpty() ){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir una medida sin nombre");
			alert.showAndWait();
		}else{	
		MedidaItem medida = new MedidaItem();
		medida.setNombre(medidasNombreField.getText());
		medida.setAbreviatura(medidasAbreviaturasField.getText());
		try {
			ServiceLocator.getMedidasService().crearMedida(medida);
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		medidasList.clear();
		cargarTablaMedidas();
		}
	}
	//Eliminar Medidas
		@FXML
		private void eliminarMedidaButtonActionPerformed(){
			if(medidasTable.getSelectionModel().getSelectedIndex()> 0){
				
				try {
					ServiceLocator.getMedidasService().eliminarMedida((medidasTable.getSelectionModel().getSelectedItem().getId()));
				} catch (ServiciosException e) {
					e.printStackTrace();
				}
				medidasList.clear();
				cargarTablaMedidas();
				
				}else{
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Advertencia");
					alert.setHeaderText(null);
					alert.setContentText("Debe seleccionar una medida antes de eliminarla");
					alert.showAndWait();
				}
			}
		
	
			//Panel Anotaciones
			//Añadir Anotacion
		@FXML
		private void anadirAnotacionButtonActionPerformed(){
			
			if(anotacionesDescripcionField.getText().isEmpty() ){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Advertencia");
				alert.setHeaderText(null);
				alert.setContentText("No puede añadir un tipo de anotación sin nombre");
				alert.showAndWait();
			}else{	
			TipoAnotacionItem anotacion = new TipoAnotacionItem();
			anotacion.setDescripcion(anotacionesDescripcionField.getText());
			try {
				ServiceLocator.getTiposAnotacionesService().crearTipoAnotacion((anotacion));
			} catch (ServiciosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			anotacionesList.clear();
			cargarTablaAnotaciones();
			}
		}
	
		//Eliminar Anotaciones
				@FXML
				private void eliminarAnotacionesButtonActionPerformed(){
					if(anotacionesTable.getSelectionModel().getSelectedIndex()> 0){
						
						try {
							ServiceLocator.getTiposAnotacionesService().eliminarTipoAnotacion((anotacionesTable.getSelectionModel().getSelectedItem().getId()));
						} catch (ServiciosException e) {
							e.printStackTrace();
						}
						anotacionesList.clear();
						cargarTablaAnotaciones();
						
						}else{
							
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Advertencia");
							alert.setHeaderText(null);
							alert.setContentText("Debe seleccionar una anotación antes de eliminarla");
							alert.showAndWait();
						}
					}
				
	
	}
	
	
	
	
	



