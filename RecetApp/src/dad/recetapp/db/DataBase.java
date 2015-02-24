package dad.recetapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataBase {

	private static final ResourceBundle CONFIG = ResourceBundle.getBundle(DataBase.class.getPackage().getName() + ".basedatos");
	private static Connection conexion = null;
	
	private DataBase() {}
	
	public static Connection getConnection() {
		try {
			if (conexion == null || conexion.isClosed()) {
				conexion = conectar();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conexion;
	}
	
	private static void registrarDriver() {
		try {
			Class.forName(CONFIG.getString("db.driver.classname"));
		} catch (ClassNotFoundException e) {
			System.out.println("Error al cargar el driver JDBC");
		}
	}
	
	public static Connection conectar(String url, String username, String password) {
		registrarDriver();
		Connection conexion = null;
		try {
			conexion = DriverManager.getConnection(url, username, password);
			System.out.println("Conexión abierta");
		} catch (SQLException e) {
			System.err.println("No fue posible establecer la conexión.");
			e.printStackTrace();
		}
		return conexion;
	}
	
	public static Connection conectar() {
		return conectar(CONFIG.getString("db.url"), CONFIG.getString("db.username"), CONFIG.getString("db.password"));
	}
	
	public static void desconectar(Connection conexion) {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
				conexion = null;
			}
			System.out.println("Conexión cerrada");
		} catch (SQLException e) {
			System.err.println("No fue posible cerrar la conexión.");
			e.printStackTrace();
		}
	}
	
	public static void desconectar() {
		desconectar(conexion);
	}
	
}
