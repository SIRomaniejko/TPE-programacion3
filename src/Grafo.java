import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Grafo {
	ArrayList<Aeropuerto> aeropuertos;
	HashMap<String, Integer> identificadores;
	ArrayList<Reserva> reservas;
	RutaAerea[][] rutas;
	
	// aca va tu codigo del constructor //
	public Grafo(String pathRootFolder){
		String line = "";
		String cvsSplitBy = ";";

		
		aeropuertos = new ArrayList<Aeropuerto>();
		identificadores = new HashMap<String, Integer>();
		//carga del aeropuerto y los identificadores
	    try (BufferedReader br = new BufferedReader(new FileReader((pathRootFolder + "Aeropuertos.csv")))) {
			    int idAeropuerto = 0;
		        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
		            String[] items = line.split(cvsSplitBy);
		            String nombre = items[0];
		            String ciudad = items[1];
		            String pais = items[2];
		            Aeropuerto nuevo = new Aeropuerto(nombre, ciudad, pais);
		            aeropuertos.add(nuevo);
		            identificadores.put(nombre, idAeropuerto++);
		        }
		        rutas = new RutaAerea[idAeropuerto+1][idAeropuerto+1];
		        
	    }
	    catch (IOException e) {
		        e.printStackTrace();
		        rutas = new RutaAerea[0][0];
	    }
	    //creacion de rutasAereas y su carga en la matriz ruta
	    try (BufferedReader br = new BufferedReader(new FileReader(pathRootFolder + "Rutas.csv"))) {
	        while ((line = br.readLine()) != null) {
	            String[] items = line.split(cvsSplitBy);
	            int origen = identificadores.get(items[0]);
	            int destino = identificadores.get(items[1]);
	            double distancia = Double.parseDouble(items[2]);
	            boolean esCabotaje = items[3] == "1"; 
	            String aeroLineas = items[4].replace("{","");
	            aeroLineas = aeroLineas.replace("}","");
	            String[] aeroLinea = aeroLineas.split(",");
	            HashMap<String, Integer> aeroLineasFinal = new HashMap<String, Integer>();
	            for (int x=0; x < aeroLinea.length; x++){
	            	String[]aerolineaFinal = aeroLinea[x].split("-");
	            	aeroLineasFinal.put(aerolineaFinal[0], Integer.parseInt(aerolineaFinal[1]));
	            }
	            this.rutas[origen][destino] = new RutaAerea(distancia, esCabotaje, aeroLineasFinal);
	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    this.reservas = new ArrayList<Reserva>();
	    //carga de las reservas en la lista
	    try (BufferedReader br = new BufferedReader(new FileReader(pathRootFolder + "Reservas.csv"))) {                                                              // salteo el encabezado
	        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
	            String[] items = line.split(cvsSplitBy);
	            String origen = items[0];
	            String destino = items[1];
	            String empresa = items[2];
	            int cantidad = Integer.parseInt(items[3]);
	            this.reservas.add(new Reserva(origen, destino, empresa, cantidad));
	            }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public Iterator<String> getAeropuertos(){
		return identificadores.keySet().iterator();
	}
	
	public boolean existeRutaDirecta(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)] != null;
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
	
	public Iterator<String>getVuelos(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].getVuelos();
	}
}
