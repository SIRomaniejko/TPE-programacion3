import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
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
	HashMap<String, Integer> reservas; //String es <origen>-<destino>-<aerolinea>
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
	            boolean esCabotaje = items[3].equals("1");
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
	            this.asignacionArco(origen, destino, esCabotaje, distancia, aeroLineasFinal);
	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    this.reservas = new HashMap<String, Integer>();
	    //carga de las reservas en la lista
	    try (BufferedReader br = new BufferedReader(new FileReader(pathRootFolder + "Reservas.csv"))) {                                                              // salteo el encabezado
	        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
	            String[] items = line.split(cvsSplitBy);
	            String origen = items[0];
	            String destino = items[1];
	            String empresa = items[2];
	            int cantidad = Integer.parseInt(items[3]);
	            this.reservas.put(origen+"-"+destino+"-"+empresa, cantidad);
	            RutaAerea ruta = this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)];
	            if(ruta != null){
	            	ruta.restarAsientos(empresa, cantidad);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	protected void asignacionArco(int origen, int destino, boolean esCabotaje, double distancia, HashMap<String, Integer> aeroLineasFinal){
		this.rutas[origen][destino] = new RutaAerea(distancia, esCabotaje, aeroLineasFinal);
	}
	
	public boolean esVueloCabotaje(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].esCabotaje();
	}
	
	public Iterator<String> getAeropuertos(){
		return identificadores.keySet().iterator();
	}
	
	public boolean existeRutaDirecta(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)] != null;
	}
	
	
	public Iterator<String> getReservas(){
		ArrayList<String> regreso = new ArrayList<String>();
		Set<String> keys = this.reservas.keySet();
		for(String key: keys){
			regreso.add(key + "-" + this.reservas.get(key));
		}
		return regreso.iterator();
	}
	
	public Iterator<String> servicio1(String origen, String destino, String aerolinea){
		ArrayList<String> salida = new ArrayList<String>();
		if (this.existeRutaDirecta(origen, destino) && getRutaAerea(origen, destino).contieneAerolinea(aerolinea)) {
			salida.add("Existe una ruta directa para el origen "+origen+" y destino "+destino);
			salida.add("La distancia es de "+this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].getDistancia()+ " km");
			int asientos = this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].asientosPorAerolinea(aerolinea);
			salida.add("La cantidad de asientos disponibles es "+ asientos);
		}
		else {
			salida.add("No existe ruta directa para el origen y destino, con la aerolinea especificada");
		}
		return salida.iterator();
	}
	
	
	public Iterator<String> servicio2(String origen, String destino, String aerolineaEvitar){
		
		int idOrigen = this.identificadores.get(origen);
		int idDestino = this.identificadores.get(destino);
		boolean[] aeropuertosExplorados = new boolean[this.aeropuertos.size()];
		ArrayList<Stack<HashMap<String, Object>>> rutas = this.back(idOrigen, idOrigen, idDestino, aerolineaEvitar, aeropuertosExplorados);
		ArrayList<String> regreso = new ArrayList<String>();
		int numRecorrido = 1;
		for(Stack<HashMap<String, Object>> recorrido: rutas){
			regreso.add("recorrido " + numRecorrido++ + ": ");
			while(!recorrido.isEmpty()){
				HashMap<String, Object> vuelo = recorrido.pop();
				regreso.add("destino: " + vuelo.get("destino") + "; distancia restante: " + vuelo.get("distancia") + "; escalas restantes: " + vuelo.get("saltos") + "; aerolineas: " + vuelo.get("aerolineas"));
			}
		}
		return regreso.iterator();
	}
	
	protected ArrayList<Stack<HashMap<String, Object>>> back(int idActual, int idAnterior, int idDestino, String aerolineaEvitar, boolean[] aeropuertosExplorados){
		
		ArrayList<Stack<HashMap<String, Object>>>regreso = new ArrayList<Stack<HashMap<String, Object>>>();
		
		String aerolineas = null;
		if(this.rutas[idAnterior][idActual] != null){
			aerolineas = this.rutas[idAnterior][idActual].getAerolineasDisponiblesMenos(aerolineaEvitar);
		}
		if(aerolineas == null && idActual != idAnterior || aeropuertosExplorados[idActual]){
			return regreso;
		}
		if(idActual == idDestino){ //el top tiene que traer la distancia
			Stack<HashMap<String, Object>> recorrido = new Stack<HashMap<String, Object>>();
			HashMap<String, Object> viaje = new HashMap<String, Object>();
			viaje.put("distancia", this.rutas[idAnterior][idActual].getDistancia());
			viaje.put("destino", this.identificadorToName.get(idActual));
			viaje.put("saltos", 0);
			viaje.put("aerolineas", this.rutas[idAnterior][idActual].getAerolineasDisponiblesMenos(aerolineaEvitar));
			recorrido.push(viaje);
			regreso.add(recorrido);
		}
		else{
			for(int x = 0; x < aeropuertos.size(); x++){
				if(this.rutas[idActual][x] != null ){ //falta verificar que haya vuelos disponibles
					
					aeropuertosExplorados[idActual] = true;
					ArrayList<Stack<HashMap<String, Object>>> respuestaBack = this.back(x, idActual, idDestino, aerolineaEvitar, aeropuertosExplorados);
					if(!respuestaBack.isEmpty()){
						if(idActual != idAnterior){ 
							for(Stack<HashMap<String, Object>> recorrido: respuestaBack){
								HashMap<String, Object> salto = new HashMap<String, Object>();
								salto.put("destino", this.identificadorToName.get(idActual));
								salto.put("distancia", (Double)recorrido.peek().get("distancia") + this.rutas[idAnterior][idActual].getDistancia()); 
								salto.put("saltos", (Integer)recorrido.peek().get("saltos")+ 1);
								salto.put("aerolineas",  aerolineas);
								recorrido.push(salto);
							}
						}
						regreso.addAll(respuestaBack);
					}
					aeropuertosExplorados[idActual] = false;
				}
			}
		}
		return regreso;
	}
	
	public Iterator<String> servicio3(String paisA, String paisB){
		return null;
	}
	
	public Iterator<String>getAerolineas(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].getAerolineas();
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
	
	protected RutaAerea getRutaAerea(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)];
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
