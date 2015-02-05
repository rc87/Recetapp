package dad.recetapp;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.ui.AnadirIngredientesController;
import dad.recetapp.ui.AnadirInstruccionesController;
import dad.recetapp.ui.CrearRecetaController;
import dad.recetapp.ui.EditarIngredientesController;
import dad.recetapp.ui.EditarInstruccionesController;
import dad.recetapp.ui.EditarRecetaController;
import dad.recetapp.ui.RecetAppController;
import dad.recetapp.ui.SeccionesController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private static Stage recetAppStage;
	private static Stage crearRecetaStage;
	private static Stage editarRecetaStage;
	private static Stage anadirIngredientesStage;
	private static Stage editarIngredientesStage;
	private static Stage anadirInstruccionesStage;
	private static Stage editarInstruccionStage;
	private static Stage inicioStage;
	
	
	private AnchorPane rootLayout;
	private int segundos = 4;
	

	@Override
	public void start(Stage primaryStage) {
		recetAppStage = primaryStage;
		recetAppStage.setTitle("RecetApp");
		recetAppStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));		

		
		//Evento de pedir confirmación al cerrar la ventana
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent event) {
                event.consume();    //Consumar el evento                 
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmación");
               alert.setHeaderText("");
                alert.setContentText("¿Está seguro de que desea cerrar RecetApp?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    primaryStage.close();	//Cerramos RecetApp
                } else {
                    //Acción que poner al cerrar RecetApp
                }
                
                
                
                
                
                //Consumar el evento
            }  });

		// initIntro();
		
		cargarInicio();
		initRootLayout();
	}

	private void cargarInicio() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Inicio.fxml"));
			rootLayout = (AnchorPane) loader.load();

			Scene scene2 = new Scene(rootLayout);

			inicioStage = new Stage();
			inicioStage.setScene(scene2);
			inicioStage.centerOnScreen();
			inicioStage.initModality(Modality.WINDOW_MODAL);
			// inicioStage.initStyle(StageStyle.UNDECORATED);

			inicioStage.showAndWait();
//			 try {
//			 Thread.sleep(segundos * 1000);
//			 } catch (Exception e) {
//			 e.printStackTrace();
//			 }
			 inicioStage.close();
			 inicioStage.showingProperty();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//TODO
//	private void initIntro() {
//		try {
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(Main.class.getResource("ui/Inicio.fxml"));
//			rootLayout = (AnchorPane) loader.load();
//
//			Scene scene = new Scene(rootLayout);
//
//			Stage inicioStage = new Stage();
//			inicioStage.setScene(scene);
//			inicioStage.centerOnScreen();
//			inicioStage.show();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	//TODO

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
	public void showAnadirReceta(RecetAppController recetAppController) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Crear.fxml"));
			BorderPane page = (BorderPane) loader.load();

			crearRecetaStage = new Stage();
			crearRecetaStage.setTitle("Nueva Receta");
			crearRecetaStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			crearRecetaStage.initModality(Modality.WINDOW_MODAL);
			crearRecetaStage.initOwner(recetAppStage);
			Scene scene = new Scene(page);
			crearRecetaStage.setScene(scene);
			crearRecetaStage.show();
			
			CrearRecetaController crearRecetaController = loader.<CrearRecetaController> getController();
			crearRecetaController.cargarRecetAppController(recetAppController);
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
	public void showEditarReceta(RecetAppController recetAppController) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ui/Editar.fxml"));
			BorderPane page = (BorderPane) loader.load();

			editarRecetaStage = new Stage();
			editarRecetaStage.setTitle("Editar Receta");
			editarRecetaStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			editarRecetaStage.initModality(Modality.WINDOW_MODAL);
			editarRecetaStage.initOwner(recetAppStage);
			Scene scene = new Scene(page);
			editarRecetaStage.setScene(scene);
			editarRecetaStage.show();
			EditarRecetaController editarRecetaController = loader
					.<EditarRecetaController> getController();
			editarRecetaController.cargarRecetAppController(recetAppController);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarEditarReceta() {
		editarRecetaStage.close();
	}

	// Llamar a la ventana Añadir ingrediente
	public void showAnadirIngrediente(SeccionesController seccionesController) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/AnadirIngredientes.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			anadirIngredientesStage = new Stage();
			anadirIngredientesStage.setTitle("Nuevo ingrediente para " + seccionesController.getSeccion().getNombre());
			anadirIngredientesStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			anadirIngredientesStage.initModality(Modality.WINDOW_MODAL);
			anadirIngredientesStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			anadirIngredientesStage.setScene(scene);
			anadirIngredientesStage.show();
			
			AnadirIngredientesController anadirIngredientesController = loader
					.<AnadirIngredientesController> getController();
			anadirIngredientesController.cargarSeccionesController(seccionesController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void cerrarAnadirIngrediente() {
		anadirIngredientesStage.close();
	}
	
	//Llamar a ventana añadir instrucciones
	public void showAnadirInstruccion(SeccionesController seccionesController) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/AnadirInstrucciones.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			anadirInstruccionesStage = new Stage();
			anadirInstruccionesStage.setTitle("Nueva instrucción para " + seccionesController.getSeccion().getNombre());
			anadirInstruccionesStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			anadirInstruccionesStage.initModality(Modality.WINDOW_MODAL);
			anadirInstruccionesStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			anadirInstruccionesStage.setScene(scene);
			anadirInstruccionesStage.show();
			AnadirInstruccionesController anadirInstruccionesController = loader
					.<AnadirInstruccionesController> getController();
			anadirInstruccionesController.cargarSeccionesController(seccionesController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void cerrarAnadirInstruccion() {
		anadirInstruccionesStage.close();
	}

	// Llamar a la ventana Editar ingrediente
	public void showEditarIngrediente(SeccionesController seccionesController, IngredienteItem ingrediente) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/EditarIngredientes.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			editarIngredientesStage = new Stage();
			editarIngredientesStage.setTitle("Editar ingrediente para " + seccionesController.getSeccion().getNombre());
			editarIngredientesStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			editarIngredientesStage.initModality(Modality.WINDOW_MODAL);
			editarIngredientesStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			editarIngredientesStage.setScene(scene);
			editarIngredientesStage.show();
			EditarIngredientesController editarIngredientesController = loader
					.<EditarIngredientesController> getController();
			editarIngredientesController.cargarSeccionesController(seccionesController, ingrediente);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarEditarIngrediente() {
		editarIngredientesStage.close();
	}
	
	
	// Llamar a la ventana Editar ingrediente
	public void showEditarInstruccion(SeccionesController seccionesController, InstruccionItem instruccion) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("ui/EditarInstrucciones.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			editarInstruccionStage = new Stage();
			editarInstruccionStage.setTitle("Editar instrucción para " + seccionesController.getSeccion().getNombre());
			editarInstruccionStage.getIcons().add(new Image("./dad/recetapp/ui/images/logo.png"));
			editarInstruccionStage.initModality(Modality.WINDOW_MODAL);
			editarInstruccionStage.initOwner(crearRecetaStage);
			Scene scene = new Scene(page);
			editarInstruccionStage.setScene(scene);
			editarInstruccionStage.show();
			EditarInstruccionesController editarInstruccionesController = loader
					.<EditarInstruccionesController> getController();
			editarInstruccionesController.cargarSeccionesController(seccionesController, instruccion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarEditarInstruccion() {
		editarInstruccionStage.close();
	}
	
	
	
	
	public static void main(String[] args) {
		launch(args);

	}
}
