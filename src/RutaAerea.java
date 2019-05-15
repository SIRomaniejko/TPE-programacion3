import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


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
	public String getAerolineasDisponiblesMenos(String aerolineaResto){
		Set<String> aerolineas = vuelos.keySet();
		aerolineas.remove(aerolineaResto);
		if(aerolineas.size() == 0){
			return null;
		}
		else{
			for(String aerolinea: aerolineas){
				if(vuelos.get(aerolinea) <= 0){
					aerolineas.remove(aerolinea);
				}
			}
			String regreso = "";
			for(String aerolinea: aerolineas){
				regreso += aerolinea + "-";
			}
			return regreso.substring(0, regreso.length() - 1);
		}
	}
	
	public Iterator<String> getVuelos(){
		return vuelos.keySet().iterator();
	}
}
