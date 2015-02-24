package dad.recetapp.utils;

public class IntegerUtils {
	public static boolean isInteger(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
}
