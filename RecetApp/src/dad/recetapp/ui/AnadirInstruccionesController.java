package dad.recetapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import dad.recetapp.MainPrueba;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.utils.IntegerUtils;

public class AnadirInstruccionesController {
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
	private void anadirActionPerformed(){
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
				instruccion = new InstruccionItem();
				instruccion.setOrden(Integer.valueOf(ordenTextField.getText()));
				instruccion.setDescripcion(descripcionTextField.getText());
		
				seccionesController.getInstruccionesList().add(instruccion);
				seccionesController.cargarTablaInstrucciones();
				seccionesController.getSeccion().setInstrucciones(seccionesController.getInstruccionesList());
				
				main.cerrarAnadirInstruccion();
			}

		}
	}
	
	@FXML
	private void cancelarActionPerformed(){
		main.cerrarAnadirInstruccion();
	}



	public void cargarSeccionesController(SeccionesController seccionesController) {
		this.seccionesController = seccionesController;
	}
	
}
