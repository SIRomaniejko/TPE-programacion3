import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class Grafo {
	ArrayList<Aeropuerto> aeropuertos;
	HashMap<String, Integer> identificadores;
	ArrayList<Reserva> reservas;
	RutaAerea[][] rutas;
	
	// aca va tu codigo del constructor //
	
	public Iterator<String> getAeropuertos(){
		return identificadores.keySet().iterator();
	}
	
	public Iterator<String> getReservas(){
		LinkedList<String> regreso = new LinkedList<String>();
		for(Reserva reserva: reservas){
			regreso.add(reserva.toString());
		}
		return regreso.iterator();
	}
	
	public Iterator<String> servicio1(String origen, String destino, String aerolinea){
		return null;
	}
	
	public Iterator<String> servicio2(String origen, String destino, String aerolineaEvitar){
		return null;
	}
	
	public Iterator<String> servicio3(String paisA, String paisB){
		return null;
	}
}
