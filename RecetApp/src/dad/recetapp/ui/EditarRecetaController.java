package dad.recetapp.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
import dad.recetapp.services.items.RecetaListItem;
import dad.recetapp.services.items.SeccionItem;
import dad.recetapp.utils.IntegerUtils;

public class EditarRecetaController {
	
	private RecetAppController recetAppController;
	private MainPrueba main = new MainPrueba();
	
	private RecetaItem receta;
	
	// Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;
	
	private int segundosResto;
	private int segundosRestoThermomix;
	
	// Para
	private ObservableList<String> paraList;

	private ObservableList<String> descripcionesString;
	
	
	private List<SeccionItem> secciones = new ArrayList<SeccionItem>();
	@FXML
	public TabPane seccionesTabPane;



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
	@FXML
	private TextField nombreTextField;
	@FXML
	private TextField paraTextField;


	@FXML
	public void initialize() {
		
	}
	
	public void cargarRecetAppController(RecetAppController recetAppController) {
		// Crear un objeto RecetaItem con los datos del objeto RecetaListItem de RecetAppController
		receta = new RecetaItem();
		this.recetAppController = recetAppController;
		RecetaListItem recetaList = recetAppController.recetasTable.getSelectionModel().getSelectedItem();
		try {
			receta = ServiceLocator.getRecetasService().obtenerReceta(recetaList.getId());
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		
		for (SeccionItem seccion : receta.getSecciones()) {
			cargarSeccion(seccion);
		}
		
//		receta.setId(recetaList.getId());
//		System.out.println(receta.getId());
//		receta.setNombre(recetaList.getNombre());
//		receta.setCantidad(recetaList.getCantidad());
//		receta.setPara(recetaList.getPara());
//		receta.setTiempoTotal(recetaList.getTiempoTotal());
//		receta.setTiempoThermomix(recetaList.getTiempoThermomix());
//		// Hay que obtener la categoria con el servicio, porque el RecetaListItem solo nos da el ID
//		CategoriaItem categoriaItem = new CategoriaItem();
//		try {
//			categoriaItem = ServiceLocator.getCategoriasService().obtenerCategoria(recetaList.getCategoria().getId());
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		receta.setCategoria(categoriaItem);
		cargarCampos();
		cargarPara();
		cargarCategoriasComboBox();
		cargarTiempoComboBox();
		
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
	
	private void cargarSeccion(SeccionItem seccion) {
		secciones.add(0, seccion);
		Tab nuevaTab = new Tab(seccion.getNombre());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"TabSecciones.fxml"));
			Parent root = (Parent) loader.load();
			SeccionesController controller = loader
					.<SeccionesController> getController();
			controller.asociarSeccion(seccion, this, nuevaTab);
			nuevaTab.setContent(root);
			root.autosize();
		} catch (IOException e) {
			e.printStackTrace();
		}

		nuevaTab.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				secciones.remove(seccion);
			}
		});

		seccionesTabPane.getTabs().add(1, nuevaTab);
		seccionesTabPane.getSelectionModel().select(nuevaTab);
	}
	
	private void cargarCampos() {
		nombreTextField.setText(receta.getNombre());
		paraTextField.setText(String.valueOf(receta.getCantidad()));
	}
	
	
	private void cargarPara() {
		paraList = FXCollections.observableArrayList("Personas", "Raciones", "Unidades");
		
		paraComboBox.setItems(paraList);
		paraComboBox.getSelectionModel().select(receta.getPara());
		paraComboBox.setVisibleRowCount(10);
	}

	
	


	private void cargarCategoriasComboBox() {

		try {
			categorias = ServiceLocator.getCategoriasService()
					.listarCategorias();
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
		categoriasComboBox.getSelectionModel().select(receta.getCategoria().getDescripcion());
		categoriasComboBox.setVisibleRowCount(10);

	}

	
	private void cargarTiempoComboBox() {
		
		
		Integer tiempoTotalMinutos = pasarMinutos(receta.getTiempoTotal());
		Integer tiempoTotalSegundos = segundosResto;
		
		
		// LLenamos el combobox de los minutos
		ObservableList<Integer> tiempoMinutosList = FXCollections
				.observableArrayList();
		for (int i = 0; i <= 200; i++) {
			tiempoMinutosList.add(i);
		}
		tiempoTotalMinutosComboBox.setItems(tiempoMinutosList);
		tiempoTotalMinutosComboBox.getSelectionModel().select(tiempoTotalMinutos);
		tiempoTotalMinutosComboBox.setVisibleRowCount(10);
		
		

		// Llenamos el combobox de los segundos
		ObservableList<Integer> tiempoTotalSegundosList = FXCollections
				.observableArrayList();
		for (int i = 0; i < 60; i++) {
			tiempoTotalSegundosList.add(i);
		}
		tiempoTotalSegundosComboBox.setItems(tiempoTotalSegundosList);
		tiempoTotalSegundosComboBox.getSelectionModel().select(tiempoTotalSegundos);
		tiempoTotalSegundosComboBox.setVisibleRowCount(10);

		
		
		Integer tiempoTotalThermomixMinutos = pasarMinutosThermomix(receta.getTiempoThermomix());
		Integer tiempoTotalThermomixSegundos = segundosRestoThermomix;
		
		// LLenamos el combobox de los minutos de Thermomix
		
		ObservableList<Integer> tiempoThermomixMinutosList = FXCollections
				.observableArrayList();
		for (int i = 0; i <= 200; i++) {
			tiempoThermomixMinutosList.add(i);
		}
		tiempoThermomixMinutosComboBox.setItems(tiempoThermomixMinutosList);
		tiempoThermomixMinutosComboBox.getSelectionModel().select(tiempoTotalThermomixMinutos);
		tiempoThermomixMinutosComboBox.setVisibleRowCount(10);
		
		
		// LLenamos el combobox de los segundos de thermomix
		ObservableList<Integer> tiempoThermomixSegundosList = FXCollections
				.observableArrayList();
		for (int i = 0; i < 60; i++) {
			tiempoThermomixSegundosList.add(i);
		}
		tiempoThermomixSegundosComboBox.setItems(tiempoThermomixSegundosList);
		tiempoThermomixSegundosComboBox.getSelectionModel().select(tiempoTotalThermomixSegundos);
		tiempoThermomixSegundosComboBox.setVisibleRowCount(10);

	}
	
	
	
	
	
	@FXML
	private void guardarCambiosButtonActionPerformed() throws ServiceException {
		
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
				
				ServiceLocator.getRecetasService().modificarReceta(receta);
				
				recetAppController.vaciarYCargarTabla();
				main.cerrarEditarReceta();
			}
			
		}
	}
	
	@FXML
	private void cancelarButtonActionPerformed() {
		main.cerrarEditarReceta();
	}
	
	
	
	
	
	private int pasarSegundos(int minutos, int segundos) {
		int total = (minutos * 60) + segundos;
		return total;
	}
	
	private int pasarMinutos(int segundos) {
		int total = (segundos / 60);
		segundosResto = (segundos % 60); 
		return total;
	}
	
	private int pasarMinutosThermomix(int segundos) {
		int total = (segundos / 60);
		segundosRestoThermomix = (segundos % 60); 
		return total;
	}
	
	private boolean comprobarCampos() {
		boolean hayCamposVacios = false;
		if (nombreTextField.getText().isEmpty())
			hayCamposVacios = true;
		if (paraTextField.getText().isEmpty())
			hayCamposVacios = true;
		if (paraComboBox.getValue() == null)
			hayCamposVacios = true;
		if (tiempoTotalMinutosComboBox.getValue() == null)
			hayCamposVacios = true;
		if (tiempoTotalSegundosComboBox.getValue() == null)
			hayCamposVacios = true;
		if (tiempoThermomixMinutosComboBox.getValue() == null)
			hayCamposVacios = true;
		if (tiempoThermomixSegundosComboBox.getValue() == null)
			hayCamposVacios = true;
		if (categoriasComboBox.getValue() == null)
			hayCamposVacios = true;
		return hayCamposVacios;
	}

}
