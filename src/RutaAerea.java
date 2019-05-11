import java.util.HashMap;
import java.util.Iterator;


public class RutaAerea {
	double distancia;
	private boolean esCabotaje;
	HashMap<String, Integer> vuelos;
	
	public RutaAerea(double distancia, boolean esCabotaje, HashMap<String, Integer> vuelos){
		this.distancia = distancia;
		this.esCabotaje = esCabotaje;
		this.vuelos = vuelos;
	}
	public double getDistancia() {
		return distancia;
	}
	public boolean esCabotaje() {
		return esCabotaje;
	}
	public boolean contieneAerolinea(String aerolinea){
		return vuelos.get(aerolinea) != null;
	}
	public int asientosPorAerolinea(String aerolinea) {
		return vuelos.get(aerolinea);		
	}
	
	public Iterator<String> getVuelos(){
		return vuelos.keySet().iterator();
	}
}
