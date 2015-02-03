package dad.recetapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.BaseDatos;
import dad.recetapp.model.TipoIngredienteItem;

public class TiposIngredientesService {

	public void crearTipoIngrediente(TipoIngredienteItem tipo) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into tipos_ingredientes (nombre) values (?)");
			sentencia.setString(1, tipo.getNombre());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear el tipo de ingrediente '" + tipo.getNombre() + "': " + e.getMessage());
		}		
	}
	
	public void modificarTipoIngrediente(TipoIngredienteItem tipo) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update tipos_ingredientes set nombre=? where id=?");
			sentencia.setString(1, tipo.getNombre());
			sentencia.setLong(2, tipo.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al modificar el tipo de ingrediente '" + tipo.getNombre() + "': " + e.getMessage());
		}	
	}
	
	public void eliminarTipoIngrediente(Long id) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from tipos_ingredientes where id=?");
			sentencia.setLong(1, id);
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar el tipo de ingrediente con ID '" + id + "': " + e.getMessage());
		}			
	}
	
	public List<TipoIngredienteItem> listarTiposIngredientes() throws ServiciosException {
		List<TipoIngredienteItem> tipos = new ArrayList<TipoIngredienteItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			Statement sentencia = conn.createStatement();
			ResultSet rs = sentencia.executeQuery("select * from tipos_ingredientes order by nombre");
			while (rs.next()) {
				tipos.add(resultSetToItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar los tipos de ingredientes: " + e.getMessage());
		}	
		return tipos;
	}
	
	public TipoIngredienteItem obtenerTipoIngrediente(Long id) throws ServiciosException {
		TipoIngredienteItem tipo = null;
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from tipos_anotaciones where id=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			if (rs.next()) {
				tipo = resultSetToItem(rs);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar el tipo de ingrediente con ID '" + id + "': " + e.getMessage());
		}	
		return tipo;	
	}
	
	private TipoIngredienteItem resultSetToItem(ResultSet rs) throws SQLException {
		TipoIngredienteItem item = new TipoIngredienteItem();
		item.setId(rs.getLong("id"));
		item.setNombre(rs.getString("nombre"));
		return item;
	}
	
}
