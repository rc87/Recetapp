package dad.recetapp.ui;

import java.util.Date;
import java.util.List;

import dad.recetapp.*;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.RecetaListItem;
import dad.recetapp.services.items.TipoAnotacionItem;
import dad.recetapp.services.items.TipoIngredienteItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class RecetAppController {

	private MainPrueba main = new MainPrueba();

	// Recetas
	private List<RecetaListItem> recetas;
	private ObservableList<RecetaListItem> recetasList;

	// Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;
	private ObservableList<CategoriaItem> categoriasFiltrarList;
	private ObservableList<String> descripcionessString;


	// Ingredientes
	private List<TipoIngredienteItem> ingredientes;
	private ObservableList<TipoIngredienteItem> ingredientesList;

	// Medidas
	private List<MedidaItem> medidas;
	private ObservableList<MedidaItem> medidasList;

	// Anotaciones
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


	// Columna de categorias
	@FXML
	private TableColumn<CategoriaItem, String> categoriasTableDescripcionCol;

	// Columna de ingredientes
	@FXML
	private TableColumn<TipoIngredienteItem, String> ingredientesTableNombreCol;

	// Tabla de medidas columnas
	@FXML
	private TableColumn<MedidaItem, String> medidasTableNombreCol;
	@FXML
	private TableColumn<MedidaItem, String> medidasTableAbreviaturaCol;

	// Tabla anotaciones columnas
	@FXML
	private TableColumn<TipoAnotacionItem, String> anotacionesTableDescripcionCol;
	
	//Label
	@FXML
	private Label recetasLabel;


	// TextField
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
	private TextField nombreText;
	
	
	
	@FXML
	private ComboBox<Integer> minutosCombo = new ComboBox<>();
	@FXML
	private ComboBox<Integer> segundosCombo = new ComboBox<>();
	@FXML
	private ComboBox<String> categoriaFiltrarCombo = new ComboBox<>();
	
	
	@FXML
	public void initialize() {

		System.out.println("iniciando RecetApp");

		// Cargamos la tabla recetas principal
		cargarTablaRecetas();

		cargarTablaCategorias();

		cargarTablaIngredientes();

		cargarTablaMedidas();

		cargarTablaAnotaciones();
		
		cargarCombos();
	}

	@FXML
	private void cargarCombos() {

//		//Cargar Combo Categorias
		try {
			categorias = ServiceLocator.getCategoriasService().listarCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		categoriasFiltrarList = FXCollections.observableList(categorias);
		categoriasFiltrarList.add(0, null);
		// Cuidado!! al iniciarlizar no hacerlo a null porque da error
		descripcionessString = FXCollections.observableArrayList();
		descripcionessString.add("<Todas>");

		for (int i = 1; i < categoriasFiltrarList.size(); i++) {
			descripcionessString.add(categoriasFiltrarList.get(i).getDescripcion());
		}
		
		categoriaFiltrarCombo.setItems(descripcionessString);
		categoriaFiltrarCombo.getSelectionModel().select("<Todas>");
		categoriaFiltrarCombo.setVisibleRowCount(10);

		
		
		//Combo de los minutos
		ObservableList<Integer> tiempoTotalList = FXCollections.observableArrayList();
		for (int i = 0; i <= 200; i++) {

			tiempoTotalList.add(i);
		}

		minutosCombo.setItems(tiempoTotalList);
		minutosCombo.getSelectionModel().select(60);
		minutosCombo.setVisibleRowCount(10);
		
		
		//Combo segundos
		// Llenamos el combobox de los segundos
		ObservableList<Integer> tiempoTotalSegundosList = FXCollections
				.observableArrayList();
		for (int i = 0; i < 60; i++) {

			tiempoTotalSegundosList.add(i);
		}

		segundosCombo.setItems(tiempoTotalSegundosList);
		segundosCombo.getSelectionModel().select(0);
		segundosCombo.setVisibleRowCount(10);

	}

	private void cargarTablaRecetas() {

		recetasTableNombreCol
				.setCellValueFactory(new PropertyValueFactory<RecetaListItem, String>("nombre"));
		// recetasTableNombreCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableParaCol
		.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RecetaListItem,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<RecetaListItem, String> param) {
				return new SimpleStringProperty(param.getValue().getCantidad() + " " + param.getValue().getPara());
			}
		});
		// recetasTableParaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableTiempoCol
				.setCellValueFactory(new PropertyValueFactory<RecetaListItem, Integer>("tiempoTotal"));
		// recetasTableTiempoCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		recetasTableFechaCol
				.setCellValueFactory(new PropertyValueFactory<RecetaListItem, Date>("fechaCreacion"));
		// recetasTableFechaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());
		//
		recetasTableCategoriaCol
		.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RecetaListItem,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<RecetaListItem, String> param) {
				return new SimpleStringProperty(param.getValue().getCategoria().getDescripcion());
			}
		});
		// recetasTableCategoriaCol.setCellFactory(TextFieldTableCell.<RecetaListItem>forTableColumn());

		// Cargamos la tabla categorias

		try {
			recetas = ServiceLocator.getRecetasService().listarRecetas();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		recetasList = FXCollections.observableList(recetas);

		// Le damos el valor al label recetas
		recetasLabel.setText("Recetas: " + recetasList.size());

		recetasTable.setItems(recetasList);

	}

	private void cargarTablaCategorias() {

		categoriasTableDescripcionCol
				.setCellValueFactory(new PropertyValueFactory<CategoriaItem, String>(
						"descripcion"));

		try {
			categorias = ServiceLocator.getCategoriasService()
					.listarCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		categoriasList = FXCollections.observableList(categorias);
		categoriasTable.setItems(categoriasList);

	}

	private void cargarTablaIngredientes() {

		ingredientesTableNombreCol
				.setCellValueFactory(new PropertyValueFactory<TipoIngredienteItem, String>(
						"nombre"));

		try {
			ingredientes = ServiceLocator.getTiposIngredientesService()
					.listarTiposIngredientes();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ingredientesList = FXCollections.observableList(ingredientes);
		ingredientesTable.setItems(ingredientesList);

	}

	private void cargarTablaMedidas() {

		medidasTableNombreCol
				.setCellValueFactory(new PropertyValueFactory<MedidaItem, String>(
						"nombre"));
		medidasTableAbreviaturaCol
				.setCellValueFactory(new PropertyValueFactory<MedidaItem, String>(
						"abreviatura"));

		try {
			medidas = ServiceLocator.getMedidasService().listarMedidas();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		medidasList = FXCollections.observableList(medidas);
		medidasTable.setItems(medidasList);
	}

	private void cargarTablaAnotaciones() {
		anotacionesTableDescripcionCol
				.setCellValueFactory(new PropertyValueFactory<TipoAnotacionItem, String>(
						"descripcion"));

		try {
			anotaciones = ServiceLocator.getTiposAnotacionesService()
					.listarTiposAnotaciones();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		anotacionesList = FXCollections.observableList(anotaciones);
		anotacionesTable.setItems(anotacionesList);
	}

	@FXML
	private void anadirButtonActionPerformed() {
		main.showAnadirReceta(this);
	}

	@FXML
	private void editarButtonActionPerformed() {
		if (recetasTable.getSelectionModel().getSelectedItem() != null ) {
			main.showEditarReceta(this);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una receta para poder editarla");
			alert.showAndWait();
		}
		
	}

	@FXML
	private void eliminarButtonActionPerformed() {

		if (recetasTable.getSelectionModel().getSelectedItem() != null ) {
		try {
			ServiceLocator.getRecetasService().eliminarReceta(
					recetasTable.getSelectionModel().getSelectedItem().getId());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		recetasList.clear();
		cargarTablaRecetas();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una receta para poder eliminarla");
			alert.showAndWait();
		}
	}

	public void vaciarYCargarTabla() {

		recetasList.clear();
		cargarTablaRecetas();

	}

	
	//Paneles
	// Panel Categorias
	// Añadir Categoria
	@FXML
	private void anadirCategoriaButtonActionPerformed() {

		CategoriaItem nuevaCategoria = new CategoriaItem();
		if (categoriasDescripcionField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir una categoria sin nombre");
			alert.showAndWait();
		} else {
			nuevaCategoria.setDescripcion(categoriasDescripcionField.getText());
			try {
				ServiceLocator.getCategoriasService().crearCategoria(
						nuevaCategoria);
				categoriasDescripcionField.setText("");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			categoriasList.clear();
			cargarTablaCategorias();
		}
	}

	// Eliminar Categoria
	@FXML
	private void eliminarCategoriaButtonActionPerformed() {

		if (categoriasTable.getSelectionModel().getSelectedItem() != null) {

			try {
				ServiceLocator.getCategoriasService().eliminarCategoria(
						categoriasTable.getSelectionModel().getSelectedItem()
								.getId());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			categoriasList.clear();
			cargarTablaCategorias();

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una categoria antes de eliminarla");
			alert.showAndWait();
		}
	}

	// Panel Ingredientes
	// Añadir Tipo de Ingrediente
	@FXML
	private void anadirTipoIngredienteButtonActionPerformed() {

		if (ingredientesNombreField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir un tipo de ingrediente sin nombre");
			alert.showAndWait();
		} else {
			TipoIngredienteItem tipoIngrediente = new TipoIngredienteItem();
			tipoIngrediente.setNombre(ingredientesNombreField.getText());
			try {
				ServiceLocator.getTiposIngredientesService()
						.crearTipoIngrediente(tipoIngrediente);
				ingredientesNombreField.setText("");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			ingredientesList.clear();
			cargarTablaIngredientes();
		}
	}

	// Eliminar Tipo de Ingrediente
	@FXML
	private void eliminarTipoIngredienteButtonActionPerformed() {
		if (ingredientesTable.getSelectionModel().getSelectedItem() != null) {

			try {
				ServiceLocator.getTiposIngredientesService().eliminarTipoIngrediente(
								ingredientesTable.getSelectionModel().getSelectedItem().getId());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			ingredientesList.clear();
			cargarTablaIngredientes();

		} else {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar un tipo de ingrediente antes de eliminarlo");
			alert.showAndWait();
		}
	}

	// Panel Medidas
	// Añadir Medida
	@FXML
	private void anadirMedidaButtonActionPerformed() {

		if (medidasNombreField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir una medida sin nombre");
			alert.showAndWait();
		} else {
			MedidaItem medida = new MedidaItem();
			medida.setNombre(medidasNombreField.getText());
			medida.setAbreviatura(medidasAbreviaturasField.getText());
			try {
				ServiceLocator.getMedidasService().crearMedida(medida);
				medidasNombreField.setText("");
				medidasAbreviaturasField.setText("");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			medidasList.clear();
			cargarTablaMedidas();
		}
	}

	// Eliminar Medidas
	@FXML
	private void eliminarMedidaButtonActionPerformed() {
		if (medidasTable.getSelectionModel().getSelectedItem() != null) {

			try {
				ServiceLocator.getMedidasService().eliminarMedida(
						(medidasTable.getSelectionModel().getSelectedItem()
								.getId()));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			medidasList.clear();
			cargarTablaMedidas();

		} else {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una medida antes de eliminarla");
			alert.showAndWait();
		}
	}

	// Panel Anotaciones
	// Añadir Anotacion
	@FXML
	private void anadirAnotacionButtonActionPerformed() {

		if (anotacionesDescripcionField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("No puede añadir un tipo de anotación sin nombre");
			alert.showAndWait();
		} else {
			TipoAnotacionItem anotacion = new TipoAnotacionItem();
			anotacion.setDescripcion(anotacionesDescripcionField.getText());
			try {
				ServiceLocator.getTiposAnotacionesService().crearTipoAnotacion((anotacion));
				anotacionesDescripcionField.setText("");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			anotacionesList.clear();
			cargarTablaAnotaciones();
		}
	}

	// Eliminar Anotaciones
	@FXML
	private void eliminarAnotacionesButtonActionPerformed() {
		if (anotacionesTable.getSelectionModel().getSelectedItem() != null) {

			try {
				ServiceLocator.getTiposAnotacionesService()
						.eliminarTipoAnotacion(
								(anotacionesTable.getSelectionModel()
										.getSelectedItem().getId()));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			anotacionesList.clear();
			cargarTablaAnotaciones();

		} else {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe seleccionar una anotación antes de eliminarla");
			alert.showAndWait();
		}
		
		
	}
	@FXML
	private void filtrarReceta(){
		
		try {
			//El combo cuando filtra por tiempo lo hace dando las recetas que tengan un tiempo menor del que le indiquemos
			//Falta filtrar por el id de la categoria
			if (categoriaFiltrarCombo.getSelectionModel().getSelectedIndex() == 0) {
				recetasList.clear();
				recetas=ServiceLocator.getRecetasService().buscarRecetas(
						nombreText.getText(), 
						pasarSegundos(
								minutosCombo.getValue(), segundosCombo.getValue()),
						null);
				//cargarTablaRecetas();
				recetasList = FXCollections.observableList(recetas);
				recetasTable.setItems(recetasList);
			} else {
				recetasList.clear();
				recetas=ServiceLocator.getRecetasService().buscarRecetas(
						nombreText.getText(), 
						pasarSegundos(
								minutosCombo.getValue(), segundosCombo.getValue()),
						categoriasFiltrarList.get(categoriaFiltrarCombo.getSelectionModel().getSelectedIndex()).getId());
				//cargarTablaRecetas();
				recetasList = FXCollections.observableList(recetas);
				recetasTable.setItems(recetasList);
			}

			if(!recetas.isEmpty()){
			

			recetasTable.setItems(recetasList);
			}else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Advertencia");
				alert.setContentText("No coincide ningún resultado con su búsqueda");
				alert.showAndWait();	
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}		
	}
	
	//Pasar a segundos
	private int pasarSegundos(int minutos, int segundos) {
		int total = (minutos * 60) + segundos;
		return total;
	}

}
