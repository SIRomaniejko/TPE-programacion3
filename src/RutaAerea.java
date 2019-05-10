import java.util.HashMap;


public class RutaAerea {
	double distancia;
	boolean esCabotaje;
	HashMap<String, Integer> vuelos;
	
	public double getDistancia() {
		return distancia;
	}
	public boolean EsCabotaje() {
		return esCabotaje;
	}
	public boolean contieneAerolinea(String aerolinea){
		return vuelos.get(aerolinea) != null;
	}
}
