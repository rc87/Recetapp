package dad.recetapp.ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import dad.recetapp.Main;
import dad.recetapp.model.CategoriaItem;
import dad.recetapp.model.RecetaItem;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiciosException;
import dad.recetapp.ui.*;

public class CrearRecetaController {
	
	private RecetAppController recetappController = new RecetAppController();
	private Main main = new Main();
	
	private RecetaItem receta;

	// Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;

	// Para
	private ObservableList<String> paraList;

	private ObservableList<String> descripcionesString;

	// Botones
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	@FXML
	private Button ingredientesTableAnadirButton;

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

	}
	
	
	private void cargarPara() {
		paraList = FXCollections.observableArrayList("Personas", "Raciones",
				"Unidades");

		paraComboBox.setItems(paraList);
		paraComboBox.setVisibleRowCount(3);
		paraComboBox.getSelectionModel().select(0);
	}
	
	private void cargarCategoriasComboBox() {
		int i;
		try {
			categorias = ServiceLocator.getCategoriasService().listarCategorias();
		} catch (ServiciosException e) {
			e.printStackTrace();
		}

		categoriasList = FXCollections.observableList(categorias);
		// Cuidado!! al iniciarlizar no hacerlo a null porque da error
		descripcionesString = FXCollections.observableArrayList();

		for (i = 0; i < categoriasList.size(); i++) {
			descripcionesString.add(categoriasList.get(i).getDescripcion());
		}

		categoriasComboBox.setItems(descripcionesString);
		categoriasComboBox.setVisibleRowCount(4);
		categoriasComboBox.getSelectionModel().select(0);

	}
	
	

	// LLenamos el combobox de los minutos
	private void cargarTiempoComboBox() {
		
		int i;
		
		ObservableList<Integer> tiempoTotalList = FXCollections.observableArrayList();
		for (i = 0; i <= 200; i++) {

			tiempoTotalList.add(i);
		}

		tiempoTotalMinutosComboBox.setItems(tiempoTotalList);
		tiempoTotalMinutosComboBox.setVisibleRowCount(5);
		tiempoTotalMinutosComboBox.getSelectionModel().select(0);

		// Llenamos el combobox de los segundos
		ObservableList<Integer> tiempoTotalSegundosList = FXCollections
				.observableArrayList();
		for (i = 0; i < 60; i++) {

			tiempoTotalSegundosList.add(i);
		}

		tiempoTotalSegundosComboBox.setItems(tiempoTotalSegundosList);
		tiempoTotalSegundosComboBox.setVisibleRowCount(5);
		tiempoTotalSegundosComboBox.getSelectionModel().select(0);

		// LLenamos el combobox de los minutos de Thermomix
		tiempoThermomixMinutosComboBox.setItems(tiempoTotalList);
		tiempoThermomixMinutosComboBox.setVisibleRowCount(5);
		tiempoThermomixMinutosComboBox.getSelectionModel().select(0);
		// LLenamos el combobox de los segundos de thermomix
		tiempoThermomixSegundosComboBox.setItems(tiempoTotalSegundosList);
		tiempoThermomixSegundosComboBox.setVisibleRowCount(5);
		tiempoThermomixSegundosComboBox.getSelectionModel().select(0);

	}

	@FXML
	private void crearButtonActionPerformed() throws ServiciosException {

		receta = new RecetaItem();
		receta.setNombre(nombreTextField.getText());
		// Convertir el valor para de entero a String
		String para = paraTextField.getText();
		int paraInt = Integer.parseInt(para);
		receta.setCantidad(paraInt);
		receta.setPara(paraComboBox.getValue());

		receta.setTiempoTotal(PasarSegundos(tiempoTotalMinutosComboBox.getValue(),
				tiempoTotalSegundosComboBox.getValue()));
		receta.setTiempoThermomix(PasarSegundos(
				tiempoThermomixMinutosComboBox.getValue(),
				tiempoThermomixSegundosComboBox.getValue()));
		receta.setCategoria(categoriasList.get(categoriasComboBox
				.getSelectionModel().getSelectedIndex()));

		ServiceLocator.getRecetasService().crearReceta(receta);
		
		//recetappController.vaciarYCargarTabla();
		main.cerrarCrearReceta();
	}

	@FXML
	private void cancelarButtonActionPerformed() {
		main.cerrarCrearReceta();
	}
	
	@FXML
	private void anadirIngredienteActionPerformed(){
		main.showAnadirIngrediente();
	}
	
	@FXML
	private void editarIngredienteActionPerformed(){
		main.showEditarIngrediente();
	}

	private int PasarSegundos(int minutos, int segundos) {
		int total = (minutos * 60) + segundos;
		return total;
	}
	
	private boolean comprobarCampos() {
		
		//TODO
		
		
		return true;
	}
}
