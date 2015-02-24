package dad.recetapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import dad.recetapp.MainPrueba;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.utils.IntegerUtils;

public class EditarInstruccionesController {
	
	private MainPrueba main = new MainPrueba();
	private SeccionesController seccionesController;
	
	private InstruccionItem instruccion;
	
	
	@FXML
	private TextField ordenTextField;
	@FXML
	private TextArea descripcionTextField;
	
	
	public void initialize() {	
				
	}
	
	
	
	@FXML
	private void guardarActionPerformed(){
		if (ordenTextField.getText().isEmpty() || descripcionTextField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Advertencia");
			alert.setHeaderText(null);
			alert.setContentText("Debe rellenar los campos para añadir una instrucción");
			alert.showAndWait();
		} else {
			if (!IntegerUtils.isInteger(ordenTextField.getText())){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Advertencia");
				alert.setHeaderText(null);
				alert.setContentText("Sólo se puede introducir números en el campo 'Orden'");
				alert.showAndWait();
				ordenTextField.clear();
			} else {
				seccionesController.getInstruccionesList().remove(instruccion);
				instruccion.setOrden(Integer.valueOf(ordenTextField.getText()));
				instruccion.setDescripcion(descripcionTextField.getText());
		
				seccionesController.getInstruccionesList().add(instruccion);
				seccionesController.cargarTablaInstrucciones();
				seccionesController.getSeccion().setInstrucciones(seccionesController.getInstruccionesList());
				
				main.cerrarEditarInstruccion();
			}

		}
	}
	
	@FXML
	private void cancelarActionPerformed(){
		main.cerrarEditarInstruccion();
	}



	public void cargarSeccionesController(SeccionesController seccionesController, InstruccionItem instruccion) {
		this.seccionesController = seccionesController;
		this.instruccion = instruccion;
		ordenTextField.setText("" + instruccion.getOrden());
		descripcionTextField.setText(instruccion.getDescripcion());
	}
	
}
