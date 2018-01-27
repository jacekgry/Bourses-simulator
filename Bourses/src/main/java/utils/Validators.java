package utils;

public class Validators {

	public static Boolean isPESEL(String string){
		return string.matches("^[0-9]{11}$");
	}
	
	public static Boolean isNumber(String string) {
		return string.matches("[0-9]+\\.?[0-9]{0,2}$");
	}
	
	
	public static Boolean isName(String string) {
		return string.replaceAll("\\s", "").matches("^[a-zA-Z]+$");
	} 
	
	public static Boolean isAddress(String string) {
		return string.replaceAll("\\s", "").matches("^[0-9]+[a-zA-Z]+$");
	} 
	
}
