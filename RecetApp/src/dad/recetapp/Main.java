package dad.recetapp;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import dad.recetapp.model.RecetaListItem;
import dad.recetapp.ui.EditarRecetaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private static Stage recetAppStage;
	private static Stage crearRecetaStage;
	private static Stage editarRecetaStage;
	private static Stage anadirIngredientesStage;
	private static Stage editarIngredientesStage;

	private AnchorPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		recetAppStage = primaryStage;
		recetAppStage.setTitle("RecetApp");

		// initIntro();

		initRootLayout();
	}

	private void initIntro() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Inicio.fxml"));
			rootLayout = (AnchorPane) loader.load();

			Scene scene = new Scene(rootLayout);

			Stage inicioStage = new Stage();
			inicioStage.setScene(scene);
			inicioStage.centerOnScreen();
			inicioStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Recetapp.fxml"));
			rootLayout = (AnchorPane) loader.load();

			Scene scene = new Scene(rootLayout);

			recetAppStage.setScene(scene);
			recetAppStage.centerOnScreen();
			recetAppStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Llamar a la ventana Añadir
	public void showAnadirReceta() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Crear.fxml"));
			BorderPane page = (BorderPane) loader.load();

			crearRecetaStage = new Stage();
			crearRecetaStage.setTitle("Nueva Receta");
			crearRecetaStage.initModality(Modality.WINDOW_MODAL);
			crearRecetaStage.initOwner(recetAppStage);
			Scene scene = new Scene(page);
			crearRecetaStage.setScene(scene);
			crearRecetaStage.show();
			// Set the person into the controller.
			// RecetAppController controller = loader.getController();
			// controller.

			// Show the dialog and wait until the user closes it
			// dialogStage.showAndWait();

			// return controller.anadirButtonActionPerformed();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cerrarCrearReceta() {
		crearRecetaStage.close();
	}

	// Llamar a la ventana Editar
	public void showEditarReceta(RecetaListItem receta) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Editar.fxml"));
			BorderPane page = (BorderPane) loader.load();

			editarRecetaStage = new Stage();
			editarRecetaStage.setTitle("Editar Receta");
			editarRecetaStage.initModality(Modality.WINDOW_MODAL);
			editarRecetaStage.initOwner(recetAppStage);
			Scene scene = new Scene(page);
			editarRecetaStage.setScene(scene);
			editarRecetaStage.show();
			EditarRecetaController editarRecetaController = loader
					.<EditarRecetaController> getController();
			editarRecetaController.cargarReceta(receta);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarEditarReceta() {
		editarRecetaStage.close();
	}

	// Llamar a la ventana Añadir ingrediente
	public void showAnadirIngrediente() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/AnadirIngredientes.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			anadirIngredientesStage = new Stage();
			anadirIngredientesStage.setTitle("Nuevo ingrediente para ");
			anadirIngredientesStage.initModality(Modality.WINDOW_MODAL);
			anadirIngredientesStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			anadirIngredientesStage.setScene(scene);
			anadirIngredientesStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarAnadirNuevoIngrediente() {
		anadirIngredientesStage.close();
	}

	// Llamar a la ventana Editar ingrediente
	public void showEditarIngrediente() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/EditarIngredientes.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			editarIngredientesStage = new Stage();
			editarIngredientesStage.setTitle("Editar ingrediente para ");
			editarIngredientesStage.initModality(Modality.WINDOW_MODAL);
			editarIngredientesStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			editarIngredientesStage.setScene(scene);
			editarIngredientesStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarEditarIngrediente() {
		editarIngredientesStage.close();
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}
}
