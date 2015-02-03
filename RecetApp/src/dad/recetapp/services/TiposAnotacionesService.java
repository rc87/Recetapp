package dad.recetapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.BaseDatos;
import dad.recetapp.model.TipoAnotacionItem;

public class TiposAnotacionesService {

	public void crearTipoAnotacion(TipoAnotacionItem tipo) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into tipos_anotaciones (descripcion) values (?)");
			sentencia.setString(1, tipo.getDescripcion());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear el tipo de anotación '" + tipo.getDescripcion() + "': " + e.getMessage());
		}			
	}
	
	public void modificarTipoAnotacion(TipoAnotacionItem tipo) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update tipos_anotaciones set descripcion=? where id=?");
			sentencia.setString(1, tipo.getDescripcion());
			sentencia.setLong(2, tipo.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al modificar el tipo de anotación '" + tipo.getDescripcion() + "': " + e.getMessage());
		}	
	}
	
	public void eliminarTipoAnotacion(Long id) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from tipos_anotaciones where id=?");
			sentencia.setLong(1, id);
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar el tipo de anotación con ID '" + id + "': " + e.getMessage());
		}		
	}
	
	public List<TipoAnotacionItem> listarTiposAnotaciones() throws ServiciosException {
		List<TipoAnotacionItem> tipos = new ArrayList<TipoAnotacionItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			Statement sentencia = conn.createStatement();
			ResultSet rs = sentencia.executeQuery("select * from tipos_anotaciones order by descripcion");
			while (rs.next()) {
				tipos.add(resultSetToItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar los tipos de anotaciones: " + e.getMessage());
		}	
		return tipos;
	}
	
	public TipoAnotacionItem obtenerTipoAnotacion(Long id) throws ServiciosException {
		TipoAnotacionItem tipo = null;
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
			throw new ServiciosException("Error al recuperar el tipo de anotación con ID '" + id + "': " + e.getMessage());
		}	
		return tipo;
	}
	
	private TipoAnotacionItem resultSetToItem(ResultSet rs) throws SQLException {
		TipoAnotacionItem item = new TipoAnotacionItem();
		item.setId(rs.getLong("id"));
		item.setDescripcion(rs.getString("descripcion"));
		return item;
	}
	
}
