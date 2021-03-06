package dad.recetapp.model;

public class TipoAnotacionItem {
	private Long id;
	private String descripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoAnotacionItem) {
			TipoAnotacionItem tipo = (TipoAnotacionItem) obj;
			return tipo.getId() == this.id;
		}
		return false;
	}

}
