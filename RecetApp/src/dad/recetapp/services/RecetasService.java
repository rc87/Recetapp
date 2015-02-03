package dad.recetapp.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dad.recetapp.db.BaseDatos;
import dad.recetapp.model.AnotacionItem;
import dad.recetapp.model.CategoriaItem;
import dad.recetapp.model.IngredienteItem;
import dad.recetapp.model.InstruccionItem;
import dad.recetapp.model.MedidaItem;
import dad.recetapp.model.RecetaItem;
import dad.recetapp.model.RecetaListItem;
import dad.recetapp.model.SeccionItem;
import dad.recetapp.model.TipoAnotacionItem;
import dad.recetapp.model.TipoIngredienteItem;

public class RecetasService {
	
	public void crearReceta(RecetaItem receta) throws ServiciosException {
		Connection conn = null;
		try {
			conn = BaseDatos.getConnection();
			conn.setAutoCommit(false);
			
			receta.setFechaCreacion(new Date()); 
			
			PreparedStatement sentencia = conn.prepareStatement("insert into recetas (nombre, fecha_creacion, cantidad, para, tiempo_total, tiempo_thermomix, id_categoria) values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			sentencia.setString(1, receta.getNombre());
			sentencia.setDate(2, new java.sql.Date(receta.getFechaCreacion().getTime()));
			sentencia.setInt(3, receta.getCantidad());
			sentencia.setString(4, receta.getPara());
			sentencia.setInt(5, receta.getTiempoTotal());
			sentencia.setInt(6, receta.getTiempoThermomix());
			sentencia.setLong(7, receta.getCategoria().getId());
			sentencia.executeUpdate();
	        ResultSet rs = sentencia.getGeneratedKeys();
	        Long id = null;
	        if (rs.next()) {
	            id = rs.getLong(1);
	        }
	        rs.close();
			sentencia.close();
			
			// crear las anotaciones
			for (AnotacionItem anotacion : receta.getAnotaciones()) {
				crearAnotacion(id, anotacion);
			}
			
			// crear las secciones
			for (SeccionItem seccion : receta.getSecciones()) {
				crearSeccion(id, seccion);
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServiciosException("Error al crear la receta '" + receta.getNombre() + "': " + e.getMessage());
		}
	}


	public void modificarReceta(RecetaItem receta) throws ServiciosException {
		Connection conn = null;
		try {
			conn = BaseDatos.getConnection();
			conn.setAutoCommit(false);
			
			RecetaItem recetaActual = obtenerReceta(receta.getId());
			
			PreparedStatement sentencia = conn.prepareStatement("update recetas set nombre=?, cantidad=?, para=?, tiempo_total=?, tiempo_thermomix=?, id_categoria=? where id=?");
			sentencia.setString(1, receta.getNombre());
			sentencia.setInt(2, receta.getCantidad());
			sentencia.setString(3, receta.getPara());
			sentencia.setInt(4, receta.getTiempoTotal());
			sentencia.setInt(5, receta.getTiempoThermomix());
			sentencia.setLong(6, receta.getCategoria().getId());
			sentencia.setLong(7, receta.getId());
			sentencia.executeUpdate();
			sentencia.close();
			
			// crear las anotaciones
			for (AnotacionItem anotacion : receta.getAnotaciones()) {
				if (anotacion.getId() == null) {
					crearAnotacion(receta.getId(), anotacion);
				} else {
					modificarAnotacion(anotacion);
					recetaActual.getAnotaciones().remove(anotacion);
				}
			}
			for (AnotacionItem anotacion : recetaActual.getAnotaciones()) {
				eliminarAnotacion(anotacion);
			}
			
			// crear las secciones
			for (SeccionItem seccion : receta.getSecciones()) {
				if (seccion.getId() == null) {
					crearSeccion(receta.getId(), seccion);
				} else {
					modificarSeccion(seccion);
					recetaActual.getSecciones().remove(seccion);
				}
			}
			for (SeccionItem seccion : recetaActual.getSecciones()) {
				eliminarSeccion(seccion);
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServiciosException("Error al crear la receta '" + receta.getNombre() + "': " + e.getMessage());
		}	
	}

	public void eliminarReceta(Long id) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from recetas where id=?");
			sentencia.setLong(1, id);
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la receta con ID '" + id + "': " + e.getMessage());
		}			
	}
	
	public List<RecetaListItem> buscarRecetas(String nombre, Integer tiempoTotal, Long idCategoria) throws ServiciosException {
		List<RecetaListItem> recetas = new ArrayList<RecetaListItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			
			String sql = "select r.*, c.descripcion as categoria from recetas r left join categorias c on r.id_categoria=c.id where nombre like ?";
			if (nombre == null) nombre = "";
			if (tiempoTotal != null) sql += " and tiempo_total=?";
			if (idCategoria != null) sql += " and id_categoria=?";
			
			PreparedStatement sentencia = conn.prepareStatement(sql);
			sentencia.setString(1, "%" + nombre + "%");
			
			if (tiempoTotal != null && idCategoria != null) {
				sentencia.setInt(2, tiempoTotal);
				sentencia.setLong(3, idCategoria);
			} else if (tiempoTotal != null) {
				sentencia.setInt(2, tiempoTotal);
			} else if (idCategoria != null) {
				sentencia.setLong(2, idCategoria);
			}
			
			ResultSet rs = sentencia.executeQuery(sql);
			while (rs.next()) {
				recetas.add(resultSetToListItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al buscar recetas: " + e.getMessage());
		}			
		return recetas;
	}

	public List<RecetaListItem> listarRecetas() throws ServiciosException {
		List<RecetaListItem> recetas = new ArrayList<RecetaListItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			Statement sentencia = conn.createStatement();
			ResultSet rs = sentencia.executeQuery("select r.*, c.descripcion as categoria from recetas r left join categorias c on r.id_categoria=c.id");
			while (rs.next()) {
				recetas.add(resultSetToListItem(rs));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener el listado de recetas: " + e.getMessage());
		}			
		return recetas;
	}
	
	public RecetaItem obtenerReceta(Long id) throws ServiciosException {
		RecetaItem receta = null;
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select r.*, c.descripcion as categoria from recetas r left join categorias c on r.id_categoria=c.id where id = ?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			if (rs.next()) {
				receta = new RecetaItem();
				receta.setId(rs.getLong("id"));
				receta.setNombre(rs.getString("nombre"));
				receta.setCantidad(rs.getInt("cantidad"));
				receta.setPara(rs.getString("para"));
				receta.setFechaCreacion(rs.getDate("fecha_creacion"));
				receta.setTiempoTotal(rs.getInt("tiempo_total"));
				receta.setTiempoThermomix(rs.getInt("tiempo_thermomix"));
				receta.setCategoria(new CategoriaItem());
				receta.getCategoria().setId(rs.getLong("id_categoria"));
				receta.getCategoria().setDescripcion(rs.getString("categoria"));
				receta.getAnotaciones().addAll(listarAnotaciones(id));
				receta.getSecciones().addAll(listarSecciones(id));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener la receta con ID '" + id + "': " + e.getMessage());
		}			
		return receta;
	}

	/* ==============================================================
	 * METODOS PRIVADOS
	 */

	private List<AnotacionItem> listarAnotaciones(Long id) throws ServiciosException {
		List<AnotacionItem> anotaciones = new ArrayList<AnotacionItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select a.*, t.id as id_anotacion, t.descripcion as descripcion from anotaciones a left join tipos_anotaciones t on a.id_tipo=t.id where a.id_receta=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			while (rs.next()) {
				AnotacionItem anotacion = new AnotacionItem();
				anotacion.setId(rs.getLong("id"));
				anotacion.setAnotaciones(rs.getString("anotaciones"));
				anotacion.setTipo(new TipoAnotacionItem());
				anotacion.getTipo().setId(rs.getLong("id_anotacion"));
				anotacion.getTipo().setDescripcion(rs.getString("descripcion"));
				anotaciones.add(anotacion);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener el listado de anotaciones de la receta con ID '" + id + "': " + e.getMessage());
		}			
		return anotaciones;
	}

	private List<SeccionItem> listarSecciones(Long id) throws ServiciosException {
		List<SeccionItem> secciones = new ArrayList<SeccionItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from secciones where id_receta=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			while (rs.next()) {
				SeccionItem seccion = new SeccionItem();
				seccion.setId(rs.getLong("id"));
				seccion.setNombre(rs.getString("nombre"));
				seccion.getIngredientes().addAll(listarIngredientes(id));
				seccion.getInstrucciones().addAll(listarInstrucciones(id));
				secciones.add(seccion);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener el listado de secciones de la receta con ID '" + id + "': " + e.getMessage());
		}			
		return secciones;
	}
	
	private List<IngredienteItem> listarIngredientes(Long id) throws ServiciosException {
		List<IngredienteItem> ingredientes = new ArrayList<IngredienteItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select i.*, m.nombre as nombre_medida, m.abreviatura, t.nombre as nombre_ingrediente from ingredientes i left join medidas m on i.id_medida=m.id left join tipos_ingredientes t on i.id_tipo=t.id where id_parte=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			while (rs.next()) {
				IngredienteItem ingrediente = new IngredienteItem();
				ingrediente.setId(rs.getLong("id"));
				ingrediente.setCantidad(rs.getInt("cantidad"));
				ingrediente.setMedida(new MedidaItem());
				ingrediente.getMedida().setId(rs.getLong("id_medida"));
				ingrediente.getMedida().setNombre(rs.getString("nombre_medida"));
				ingrediente.getMedida().setAbreviatura(rs.getString("abreviatura"));
				ingrediente.setTipo(new TipoIngredienteItem());
				ingrediente.getTipo().setId(rs.getLong("id_tipo"));
				ingrediente.getTipo().setNombre(rs.getString("nombre_ingrediente"));
				ingredientes.add(ingrediente);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener el listado de ingredientes de la sección con ID '" + id + "': " + e.getMessage());
		}			
		return ingredientes;
	}
	
	private List<InstruccionItem> listarInstrucciones(Long id) throws ServiciosException {
		List<InstruccionItem> instrucciones = new ArrayList<InstruccionItem>();
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from instrucciones where id_parte=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			while (rs.next()) {
				InstruccionItem instruccion = new InstruccionItem();
				instruccion.setId(rs.getLong("id"));
				instruccion.setOrden(rs.getInt("orden"));
				instruccion.setDescripcion(rs.getString("descripcion"));
				instrucciones.add(instruccion);
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener el listado de instrucciones de la sección con ID '" + id + "': " + e.getMessage());
		}			
		return instrucciones;
	}

	private RecetaListItem resultSetToListItem(ResultSet rs) throws SQLException {
		RecetaListItem receta = new RecetaListItem();
		receta.setId(rs.getLong("id"));
		receta.setNombre(rs.getString("nombre"));
		receta.setCantidad(rs.getInt("cantidad"));
		receta.setPara(rs.getString("para"));
		receta.setFechaCreacion(rs.getDate("fecha_creacion"));
		receta.setTiempoTotal(rs.getInt("tiempo_total"));
		receta.setTiempoThermomix(rs.getInt("tiempo_thermomix"));
		receta.setCategoria(rs.getString("categoria"));
		return receta;
	}
	
	private void crearAnotacion(Long idReceta, AnotacionItem anotacion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into anotaciones (anotaciones, id_tipo, id_receta) values (?, ?, ?)");
			sentencia.setString(1, anotacion.getAnotaciones());
			sentencia.setLong(2, anotacion.getTipo().getId());
			sentencia.setLong(3, idReceta);
			sentencia.executeUpdate();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear la anotación de tipo '" + anotacion.getTipo().getDescripcion() + "': " + e.getMessage());
		}	
	}

	private void crearSeccion(Long idReceta, SeccionItem seccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into partes (nombre, id_receta) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			sentencia.setString(1, seccion.getNombre());
			sentencia.setLong(2, idReceta);
			sentencia.executeUpdate();
	        ResultSet rs = sentencia.getGeneratedKeys();
	        Long idSeccion = null;
	        if (rs.next()) {
	            idSeccion = rs.getLong(1);
	        }
	        rs.close();
			sentencia.close();

			// crear ingredientes
			for (IngredienteItem ingrediente : seccion.getIngredientes()) {
				crearIngrediente(idSeccion, ingrediente);
			}
			
			// crear instrucciones
			for (InstruccionItem instruccion : seccion.getInstrucciones()) {
				crearInstruccion(idSeccion, instruccion);
			}
			
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear la sección '" + seccion.getNombre() + "': " + e.getMessage());
		}		
	}

	private void crearIngrediente(Long idSeccion, IngredienteItem ingrediente) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into ingredientes (cantidad, id_medida, id_tipo, id_parte) values (?, ?, ?, ?)");
			sentencia.setInt(1, ingrediente.getCantidad());
			sentencia.setLong(2, ingrediente.getMedida().getId());
			sentencia.setLong(3, ingrediente.getTipo().getId());
			sentencia.setLong(4, idSeccion);
			sentencia.executeUpdate();
			sentencia.close();		
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear el ingrediente '" + ingrediente.getTipo().getNombre() + "': " + e.getMessage());
		}
	}

	private void crearInstruccion(Long idSeccion, InstruccionItem instruccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("insert into instrucciones (orden, descripcion, id_parte) values (?, ?, ?)");
			sentencia.setInt(1, instruccion.getOrden());
			sentencia.setString(2, instruccion.getDescripcion());
			sentencia.setLong(3, idSeccion);
			sentencia.executeUpdate();
			sentencia.close();		
		} catch (SQLException e) {
			throw new ServiciosException("Error al crear la instruccion nº '" + instruccion.getOrden() + "': " + e.getMessage());
		}	
	}

	
	private void modificarAnotacion(AnotacionItem anotacion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update anotaciones set anotaciones=?, id_tipo=? where id=?");
			sentencia.setString(1, anotacion.getAnotaciones());
			sentencia.setLong(2, anotacion.getTipo().getId());
			sentencia.setLong(3, anotacion.getId());
			sentencia.executeUpdate();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al actualizar la anotación con ID '" + anotacion.getId() + "': " + e.getMessage());
		}	
	}


	private void eliminarAnotacion(AnotacionItem anotacion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from anotaciones where id=?");
			sentencia.setLong(1, anotacion.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la anotacion con ID '" + anotacion.getId() + "': " + e.getMessage());
		}		
	}


	private void modificarSeccion(SeccionItem seccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			
			SeccionItem seccionActual = obtenerSeccion(seccion.getId()); 

			PreparedStatement sentencia = conn.prepareStatement("update partes set nombre=? where id=?");
			sentencia.setString(1, seccion.getNombre());
			sentencia.setLong(2, seccion.getId());
			sentencia.executeUpdate();
			sentencia.close();
			
			// modificar los ingredientes
			for (IngredienteItem ingrediente : seccion.getIngredientes()) {
				if (ingrediente.getId() == null) {
					crearIngrediente(seccion.getId(), ingrediente);
				} else {
					modificarIngrediente(ingrediente);
					seccionActual.getIngredientes().remove(ingrediente);
				}
			}
			for (IngredienteItem ingrediente : seccionActual.getIngredientes()) {
				eliminarIngrediente(ingrediente);
			}
			
			// modificar las instrucciones
			for (InstruccionItem instruccion : seccion.getInstrucciones()) {
				if (instruccion.getId() == null) {
					crearInstruccion(seccion.getId(), instruccion);
				} else {
					modificarInstruccion(instruccion);
					seccionActual.getInstrucciones().remove(instruccion);
				}
			}
			for (InstruccionItem instruccion : seccionActual.getInstrucciones()) {
				eliminarInstruccion(instruccion);
			}
			
		} catch (SQLException e) {
			throw new ServiciosException("Error al actualizar la sección con ID '" + seccion.getId() + "': " + e.getMessage());
		}		
	}

	private SeccionItem obtenerSeccion(Long id) throws ServiciosException {
		SeccionItem seccion = null;
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("select * from partes where id=?");
			sentencia.setLong(1, id);
			ResultSet rs = sentencia.executeQuery();
			if (rs.next()) {
				seccion = new SeccionItem();
				seccion.setId(rs.getLong("id"));
				seccion.setNombre(rs.getString("nombre"));
			}
			rs.close();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al obtener la sección con ID '" + id + "': " + e.getMessage());
		}			
		return seccion;
	}

	private void modificarIngrediente(IngredienteItem ingrediente) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update ingredientes set cantidad=?, id_medida=?, id_tipo=? where id=?");
			sentencia.setInt(1, ingrediente.getCantidad());
			sentencia.setLong(2, ingrediente.getMedida().getId());
			sentencia.setLong(3, ingrediente.getTipo().getId());
			sentencia.executeUpdate();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al actualizar el ingrediente con ID '" + ingrediente.getId() + "': " + e.getMessage());
		}		
	}


	private void eliminarIngrediente(IngredienteItem ingrediente) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from ingredientes where id=?");
			sentencia.setLong(1, ingrediente.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar el ingrediente con ID '" + ingrediente.getId() + "': " + e.getMessage());
		}		
	}

	private void modificarInstruccion(InstruccionItem instruccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("update instrucciones set orden=?, descripcion=? where id=?");
			sentencia.setLong(1, instruccion.getOrden());
			sentencia.setString(2, instruccion.getDescripcion());
			sentencia.setLong(3, instruccion.getId());
			sentencia.executeUpdate();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al actualizar la instrucción con ID '" + instruccion.getId() + "': " + e.getMessage());
		}		
	}

	private void eliminarInstruccion(InstruccionItem instruccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from instrucciones where id=?");
			sentencia.setLong(1, instruccion.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la instrucción con ID '" + instruccion.getId() + "': " + e.getMessage());
		}			
	}


	private void eliminarSeccion(SeccionItem seccion) throws ServiciosException {
		try {
			Connection conn = BaseDatos.getConnection();
			PreparedStatement sentencia = conn.prepareStatement("delete from partes where id=?");
			sentencia.setLong(1, seccion.getId());
			sentencia.execute();
			sentencia.close();
		} catch (SQLException e) {
			throw new ServiciosException("Error al eliminar la sección con ID '" + seccion.getId() + "': " + e.getMessage());
		}		
	}
	
}
