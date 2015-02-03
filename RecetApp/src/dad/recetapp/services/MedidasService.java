package dad.recetapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.BaseDatos;
import dad.recetapp.model.MedidaItem;

public class MedidasService {

	public void crearMedida(MedidaItem medida) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into medidas (nombre, abreviatura) values (?, ?)");
			sentencia.setString(1, medida.getNombre());
			sentencia.setString(2, medida.getAbreviatura());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear la medida '" + medida.getNombre() + "': " + e.getMessage());
		}		
	}
	
	public void modificarMedida(MedidaItem medida) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update medidas set nombre=?, abreviatura=? where id=?");
			sentencia.setString(1, medida.getNombre());
			sentencia.setString(2, medida.getAbreviatura());
			sentencia.setLong(3, medida.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al modificar la medida '" + medida.getNombre() + "': " + e.getMessage());
		}			
	}
	
	public void eliminarMedida(Long id) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from medidas where id=?");
			sentencia.setLong(1, id);
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la medida con ID '" + id + "': " + e.getMessage());
		}			
	}
	
	public List<MedidaItem> listarMedidas() throws ServiciosException {
		List<MedidaItem> medidas = new ArrayList<MedidaItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			Statement sentencia = conn.createStatement();
			ResultSet rs = sentencia.executeQuery("select * from medidas order by nombre");
			while (rs.next()) {
				medidas.add(resultSetToItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar las medidas: " + e.getMessage());
		}	
		return medidas;
	}
	
	public MedidaItem obtenerMedida(Long id) throws ServiciosException {
		MedidaItem medida = null;
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from medidas where id=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			if (rs.next()) {
				medida = resultSetToItem(rs);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar la medida con ID '" + id +"': " + e.getMessage());
		}	
		return medida;
	}
	
	private MedidaItem resultSetToItem(ResultSet rs) throws SQLException {
		MedidaItem item = new MedidaItem();
		item.setId(rs.getLong("id"));
		item.setNombre(rs.getString("nombre"));
		item.setAbreviatura(rs.getString("abreviatura"));
		return item;
	}
}
