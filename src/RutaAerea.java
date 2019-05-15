import java.util.ArrayList;
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
		ArrayList<String> aerolineasDisponibles = new ArrayList<String>();
		if(aerolineas.size() == 0){
			return null;
		}
		else{
			for(String aerolinea: aerolineas){
				if(vuelos.get(aerolinea) > 0 && !aerolinea.equals(aerolineaResto)){
					aerolineasDisponibles.add(aerolinea);
				}
			}
			String regreso = "";
			for(String aerolinea: aerolineasDisponibles){
				regreso += aerolinea + "-";
			}
			if(regreso.equals("")){
				return null;
			}else{
				return regreso.substring(0, regreso.length() - 1);				
			}
		}
	}
	
	public Iterator<String> getAerolineas(){
		return vuelos.keySet().iterator();
	}
	
	public void restarAsientos(String empresa, int cantidad){
		
		vuelos.replace(empresa, (vuelos.get(empresa) - cantidad));
	}
}
