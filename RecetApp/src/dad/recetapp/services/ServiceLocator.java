package dad.recetapp.services;

public class ServiceLocator {
	
	private static CategoriasService cs = new CategoriasService();
	private static MedidasService ms = new MedidasService();
	private static TiposAnotacionesService tas = new TiposAnotacionesService();
	private static TiposIngredientesService tis = new TiposIngredientesService();
	private static RecetasService rs = new RecetasService();
	
	public static CategoriasService getCategoriasService() {
		return cs;
	}
	
	public static MedidasService getMedidasService() {
		return ms;
	}
	
	public static TiposAnotacionesService getTiposAnotacionesService() {
		return tas;
	}
	
	public static TiposIngredientesService getTiposIngredientesService() {
		return tis;
	}
	
	public static RecetasService getRecetasService() {
		return rs;
	}

}
