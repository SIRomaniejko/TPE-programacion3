import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;



public class Grafo {
	ArrayList<Aeropuerto> aeropuertos;
	HashMap<String, Integer> identificadores;
	HashMap<Integer, String> identificadorToName;
	ArrayList<Reserva> reservas;
	RutaAerea[][] rutas;
	
	// aca va tu codigo del constructor //
	public Grafo(String pathRootFolder){
		String line = "";
		String cvsSplitBy = ";";

		
		aeropuertos = new ArrayList<Aeropuerto>();
		identificadores = new HashMap<String, Integer>();
		identificadorToName = new HashMap<Integer, String>();
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
		            identificadores.put(nombre, idAeropuerto);
		            identificadorToName.put(idAeropuerto++, nombre);
		            
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
	            if(items[3] == "1"){
	            	System.out.println("escabotaje");
	            }
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
		
		ArrayList<String> salida = new ArrayList<String>();
		if (this.existeRutaDirecta(origen, destino)) {
			salida.add("Existe una ruta directa para el origen "+origen+" y destino "+destino);
			salida.add("La distancia es de "+this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].getDistancia()+ " km");
			int asientos = this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].asientosPorAerolinea(aerolinea);
			for(Reserva reserva: reservas){
				if ((reserva.getOrigen() == origen) && (reserva.getDestino() == destino) && (reserva.getEmpresa() == aerolinea)){
					asientos -= reserva.getCantidad();
				}
			}	
			salida.add("La cantidad de asientos disponibles es "+ asientos);
		}
		else {
			salida.add("No existe ruta directa para el origen y destino especificados");
		}
		return salida.iterator();
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
	
	public void generateGraph(String rootpath){
		BufferedWriter bw = null;
		try {
			File file = new File(rootpath + "graph.txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			// Escribo la primer linea del archivo
			String linea =  "digraph{" +  getGrafos() + "}";
			bw.write(linea);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("Error cerrando el BufferedWriter" + ex);
			}
		}
		
	}
	
	private String getGrafos(){
		String regreso = "";
		Iterator<String> aeropuertos = getAeropuertos();
		while(aeropuertos.hasNext()){
			String aeropuerto = aeropuertos.next();
			for(int x = 0; x < rutas.length; x++){
				if(rutas[this.identificadores.get(aeropuerto)][x] != null){
					String cabotaje = "";
					if(rutas[this.identificadores.get(aeropuerto)][x].esCabotaje()){
						cabotaje = "cabotaje";
					}
					else{
						cabotaje = "internacional";
					}
					String pesos = rutas[this.identificadores.get(aeropuerto)][x].getDistancia() + ", " + cabotaje;
					regreso += aeropuerto.replace("-", " ").replace(" ", "").replace(".", "") + " -> " + identificadorToName.get(x).replace("-", " ").replace(" ", "").replace(".", "") + "[label=\"" + pesos +"\"];";
				}
			}
		}
		return regreso;
	}
}
