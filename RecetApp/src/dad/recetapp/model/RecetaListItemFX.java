package dad.recetapp.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class RecetaListItemFX extends RecetaListItem implements Observable {
	
	
	private StringProperty nombre;

	private StringProperty para;
	
	private IntegerProperty tiempoTotal;
	
	private StringProperty fechaCreacion;
	
	private StringProperty categoria;
	
	

	public StringProperty getNombreProperty() {
		return nombre;
	}

	public void setNombreProperty(StringProperty nombre) {
		this.nombre = nombre;
	}
	
	
	public StringProperty getParaProperty() {
		return para;
	}

	public void setParaProperty(StringProperty para) {
		this.para = para;
	}

	public IntegerProperty getTiempoTotalProperty() {
		return tiempoTotal;
	}

	public void setTiempoTotalProperty(IntegerProperty tiempo) {
		this.tiempoTotal = tiempo;
	}

	public StringProperty getFechaCreacionProperty() {
		return fechaCreacion;
	}

	public void setFechaCreacionProperty(StringProperty fecha) {
		this.fechaCreacion = fecha;
	}

	public StringProperty getCategoriaProperty() {
		return categoria;
	}

	public void setCategoriaProperty(StringProperty categoria) {
		this.categoria = categoria;
	}

	@Override
	public void addListener(InvalidationListener listener) {
		
	}

	@Override
	public void removeListener(InvalidationListener listener) {

	}

}
