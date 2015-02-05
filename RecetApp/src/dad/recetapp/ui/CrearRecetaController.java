package dad.recetapp.ui;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import dad.recetapp.MainPrueba;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.SeccionItem;
import dad.recetapp.utils.IntegerUtils;

public class CrearRecetaController {
	
	private RecetAppController recetAppController;
	private MainPrueba main = new MainPrueba();
	
	private RecetaItem receta = new RecetaItem();

	// Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;

	// Para
	private ObservableList<String> paraList;
	private ObservableList<String> descripcionesString;
	
	private List<SeccionItem> secciones = receta.getSecciones();
	@FXML
	public TabPane seccionesTabPane;

	// Botones
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	@FXML
	private Button ingredientesTableAnadirButton;
	@FXML
	private Button instruccionesTableAnadirButton;

	// Tablas
	// @FXML
	// private TableView<RecetaListItem> recetasTable;
	// @FXML
	// private TableView<CategoriaItem> categoriasTable;
	
	


	// Combobox
	@FXML
	private ComboBox<String> paraComboBox;
	@FXML
	private ComboBox<String> categoriasComboBox;
	@FXML
	private ComboBox<Integer> tiempoTotalMinutosComboBox;
	@FXML
	private ComboBox<Integer> tiempoTotalSegundosComboBox;
	@FXML
	private ComboBox<Integer> tiempoThermomixMinutosComboBox;
	@FXML
	private ComboBox<Integer> tiempoThermomixSegundosComboBox;

	// TextField
	@FXML
	private TextField nombreTextField;
	@FXML
	private TextField paraTextField;

	// Tabla de categorias Columnas
	// @FXML
	// private TableColumn<CategoriaItem,String> categoriasTableDescripcionCol;

	@FXML
	public void initialize() {
		
		cargarPara();
		cargarCategoriasComboBox();
		cargarTiempoComboBox();
		
		
//		ObservableList<Tab> tabPane= tab0.getTabPane().getTabs();
//		tabPane.add(new Tab("Nueva pestaña"));
//		Tab nuevoTab = tabPane.get(tabPane.size()-1);
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("TabSecciones.fxml"));
//			Parent root = (Parent) loader.load();
//			CrearRecetaController controller = loader.<CrearRecetaController>getController();
//			nuevoTab.setContent(root);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		

	}
	
	public void cargarRecetAppController(RecetAppController recetAppController) {
		this.recetAppController = recetAppController;
//		seccionesTabPane.getTabs().remove(1);
		
		Tab nuevaTab = new Tab("<Nueva sección>");

		SeccionItem nuevaSeccion = new SeccionItem();
		nuevaSeccion.setNombre("Nueva sección");
		secciones.add(0, nuevaSeccion);
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TabSecciones.fxml"));
		Parent root = (Parent) loader.load();
		SeccionesController controller = loader.<SeccionesController>getController();
		controller.asociarSeccion(nuevaSeccion, this, nuevaTab);
		nuevaTab.setContent(root);
		root.autosize();
		} catch (IOException e) {
		e.printStackTrace();
		}
		
		nuevaTab.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				secciones.remove(nuevaSeccion);
			}
		});
		
		seccionesTabPane.getTabs().add(1, nuevaTab);
		
		seccionesTabPane.getSelectionModel().selectLast();
		

	}
	
	
	@FXML
	private void anadirSeccionButton() {
		Tab nuevaTab = new Tab("<Nueva sección>");
		
		SeccionItem nuevaSeccion = new SeccionItem();
		nuevaSeccion.setNombre("Nueva sección");
		secciones.add(0, nuevaSeccion);
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TabSecciones.fxml"));
		Parent root = (Parent) loader.load();
		SeccionesController controller = loader.<SeccionesController>getController();
		controller.asociarSeccion(nuevaSeccion, this, nuevaTab);
		nuevaTab.setContent(root);
		root.autosize();
		} catch (IOException e) {
		e.printStackTrace();
		}
		
		nuevaTab.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				secciones.remove(nuevaSeccion);
			}
		});
		
		seccionesTabPane.getTabs().add(1, nuevaTab);
		seccionesTabPane.getSelectionModel().select(nuevaTab);
	}
	
	
	private void cargarPara() {
		paraList = FXCollections.observableArrayList("Personas", "Raciones",
				"Unidades");
		paraComboBox.setItems(paraList);
		paraComboBox.getSelectionModel().select(0);
		paraComboBox.setVisibleRowCount(10);
	}
	
	private void cargarCategoriasComboBox() {
		try {
			categorias = ServiceLocator.getCategoriasService().listarCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		categoriasList = FXCollections.observableList(categorias);
		// Cuidado!! al iniciarlizar no hacerlo a null porque da error
		descripcionesString = FXCollections.observableArrayList();

		for (int i = 0; i < categoriasList.size(); i++) {
			descripcionesString.add(categoriasList.get(i).getDescripcion());
		}

		categoriasComboBox.setItems(descripcionesString);
		categoriasComboBox.getSelectionModel().select("<Seleccione una categoría>");
		categoriasComboBox.setVisibleRowCount(10);

	}
	
	

	// LLenamos el combobox de los minutos
	private void cargarTiempoComboBox() {
				
		ObservableList<Integer> tiempoTotalList = FXCollections.observableArrayList();
		for (int i = 0; i <= 200; i++) {

			tiempoTotalList.add(i);
		}

		tiempoTotalMinutosComboBox.setItems(tiempoTotalList);
		tiempoTotalMinutosComboBox.getSelectionModel().select(0);
		tiempoTotalMinutosComboBox.setVisibleRowCount(10);

		// Llenamos el combobox de los segundos
		ObservableList<Integer> tiempoTotalSegundosList = FXCollections
				.observableArrayList();
		for (int i = 0; i < 60; i++) {

			tiempoTotalSegundosList.add(i);
		}

		tiempoTotalSegundosComboBox.setItems(tiempoTotalSegundosList);
		tiempoTotalSegundosComboBox.getSelectionModel().select(0);
		tiempoTotalSegundosComboBox.setVisibleRowCount(10);
		
		// LLenamos el combobox de los minutos de Thermomix
		
		ObservableList<Integer> tiempoThermomixMinutosList = FXCollections
				.observableArrayList();
		for (int i = 0; i <= 200; i++) {
			tiempoThermomixMinutosList.add(i);
		}
		tiempoThermomixMinutosComboBox.setItems(tiempoTotalList);
		tiempoThermomixMinutosComboBox.getSelectionModel().select(0);
		tiempoThermomixMinutosComboBox.setVisibleRowCount(10);
		// LLenamos el combobox de los segundos de thermomix
		
		ObservableList<Integer> tiempoThermomixSegundosList = FXCollections
				.observableArrayList();
		for (int i = 0; i < 60; i++) {
			tiempoThermomixSegundosList.add(i);
		}
		tiempoThermomixSegundosComboBox.setItems(tiempoTotalSegundosList);
		tiempoThermomixSegundosComboBox.getSelectionModel().select(0);
		tiempoThermomixSegundosComboBox.setVisibleRowCount(10);

	}

	@FXML
	private void crearButtonActionPerformed() throws ServiceException {
		
		if (comprobarCampos()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe rellenar todos los campos");
			alert.showAndWait();
		} else {
			if (!IntegerUtils.isInteger(paraTextField.getText())){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Advertencia");
				alert.setHeaderText(null);
				alert.setContentText("Sólo se puede introducir números en el campo 'Para'");
				alert.showAndWait();
				paraTextField.clear();
			} else {
				receta = new RecetaItem();
				receta.setNombre(nombreTextField.getText());
				receta.setCantidad(Integer.valueOf(paraTextField.getText()));
				receta.setPara(paraComboBox.getValue());

				receta.setTiempoTotal(pasarSegundos(
						tiempoTotalMinutosComboBox.getValue(),
						tiempoTotalSegundosComboBox.getValue()));
				
				receta.setTiempoThermomix(pasarSegundos(
						tiempoThermomixMinutosComboBox.getValue(),
						tiempoThermomixSegundosComboBox.getValue()));
				
				receta.setCategoria(categoriasList.get(categoriasComboBox
						.getSelectionModel().getSelectedIndex()));
				receta.setSecciones(secciones);
				
				ServiceLocator.getRecetasService().crearReceta(receta);
				recetAppController.vaciarYCargarTabla();
				main.cerrarCrearReceta();
			}

		}

	}
	
	
	@FXML
	private void cancelarButtonActionPerformed() {
//		System.out.println("Numero de secciones: " + secciones.size());
//		for (SeccionItem seccion : secciones) {
//			System.out.println(seccion.getNombre());
//		}
//		System.out.println("Numero de tabs: " + seccionesTabPane.getTabs().size());
//		for (Tab tab : seccionesTabPane.getTabs()) {
//			System.out.println(tab.getText());
//		}
		main.cerrarCrearReceta();
	}
	


	private int pasarSegundos(int minutos, int segundos) {
		int total = (minutos * 60) + segundos;
		return total;
	}
	
	
	
	
	
	
//	private int pasarMinutos(int segundos) {
//		int total = (segundos / 60);
//		segundosResto = (segundos % 60); 
//		return total;
//	}
	
	private boolean comprobarCampos() {
		boolean hayCamposVacios = false;
		if (nombreTextField.getText().isEmpty())
			hayCamposVacios = true;
		if (paraTextField.getText().isEmpty())
			hayCamposVacios = true;
		if (paraComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
		if (tiempoTotalMinutosComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
		if (tiempoTotalSegundosComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
		if (tiempoThermomixMinutosComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
		if (tiempoThermomixSegundosComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
		if (categoriasComboBox.getSelectionModel().getSelectedIndex() == -1)
			hayCamposVacios = true;
//		if(paraTextField.getText().matches("[0-9]+") ==true);
//			hayCamposVacios = true;
		return hayCamposVacios;
	}
}
