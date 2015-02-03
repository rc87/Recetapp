package dad.recetapp.ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import dad.recetapp.Main;
import dad.recetapp.model.CategoriaItem;
import dad.recetapp.model.RecetaListItem;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.ServiciosException;

public class EditarRecetaController {
	
	private Main main = new Main();
	
	private RecetaListItem receta;
	
	// Categorias
	private List<CategoriaItem> categorias;
	private ObservableList<CategoriaItem> categoriasList;

	// Para
	private ObservableList<String> paraList;
	private int i;
	private Integer e;

	private ObservableList<String> descripcionesString;



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

	private void cargarCampos() {
		nombreTextField.setText(receta.getNombre());
		paraTextField.setText(String.valueOf(receta.getCantidad()));
	}

	// LLenamos el combobox de los minutos
	private void cargarTiempoComboBox() {

		ObservableList<Integer> tiempoTotalList = FXCollections
				.observableArrayList();
		for (e = 0; e <= 200; e++) {

			tiempoTotalList.add(e);
		}

		tiempoTotalMinutosComboBox.setItems(tiempoTotalList);
		tiempoTotalMinutosComboBox.setVisibleRowCount(5);
		tiempoTotalMinutosComboBox.getSelectionModel().select(0);

		// Llenamos el combobox de los segundos
		ObservableList<Integer> tiempoTotalSegundosList = FXCollections
				.observableArrayList();
		for (e = 0; e < 60; e++) {

			tiempoTotalSegundosList.add(e);
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

	private void cargarCategoriasComboBox() {

		try {
			categorias = ServiceLocator.getCategoriasService()
					.listarCategorias();
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

	private void cargarPara() {
		paraList = FXCollections.observableArrayList("Personas", "Raciones", "Unidades");
		
		paraComboBox.setItems(paraList);
		paraComboBox.setVisibleRowCount(3);
		paraComboBox.getSelectionModel().select(0);
	}

	public void cargarReceta(RecetaListItem receta) {
		this.receta = receta;
		cargarCampos();
		cargarPara();
		cargarCategoriasComboBox();
		cargarTiempoComboBox();
		
	}
	
	@FXML
	private void anadirNuevoIngredienteActionPerformed(){
		main.showAnadirIngrediente();
	}

}
