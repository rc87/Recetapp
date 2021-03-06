package dad.recetapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.BaseDatos;
import dad.recetapp.model.CategoriaItem;

public class CategoriasService {

	public void crearCategoria(CategoriaItem categoria) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into categorias (descripcion) values (?)");
			sentencia.setString(1, categoria.getDescripcion());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear la categor�a '" + categoria.getDescripcion() + "': " + e.getMessage());
		}		
	}
	
	public void modificarCategoria(CategoriaItem categoria) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update categorias set descripcion=? where id=?");
			sentencia.setString(1, categoria.getDescripcion());
			sentencia.setLong(2, categoria.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al modificar la categor�a '" + categoria.getDescripcion() + "': " + e.getMessage());
		}		
	}
	
	public void eliminarCategoria(Long id) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from categorias where id=?");
			sentencia.setLong(1, id);
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la categor�a con ID '" + id + "': " + e.getMessage());
		}	
	}
	
	public List<CategoriaItem> listarCategorias() throws ServiciosException {
		List<CategoriaItem> categorias = new ArrayList<CategoriaItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			Statement sentencia = conn.createStatement();
			ResultSet rs = sentencia.executeQuery("select * from categorias order by descripcion");
			while (rs.next()) {
				categorias.add(resultSetToItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar las categor�as: " + e.getMessage());
		}	
		return categorias;
	}
	
	public CategoriaItem obtenerCategoria(Long id) throws ServiciosException {
		CategoriaItem categoria = null;
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from categorias where id=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			if (rs.next()) {
				categoria = resultSetToItem(rs);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al recuperar la categor�a con ID '" + id + "': " + e.getMessage());
		}	
		return categoria;
	}
	
	private CategoriaItem resultSetToItem(ResultSet rs) throws SQLException {
		CategoriaItem item = new CategoriaItem();
		item.setId(rs.getLong("id"));
		item.setDescripcion(rs.getString("descripcion"));
		return item;
	}
	
}
