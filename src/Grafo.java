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
	
	
	
	public Grafo(ArrayList<Aeropuerto> listaAeropuertos) {
		this.aeropuertos = listaAeropuertos;
		identificadores = new HashMap<String, Integer>();
		identificadorToName = new HashMap<Integer, String>();
		for (int i=0; i< aeropuertos.size(); i++){
			Aeropuerto aActual = aeropuertos.get(i);
	        identificadores.put(aActual.getNombre(), i);
            identificadorToName.put(i, aActual.getNombre());
		}
		this.rutas = new RutaAerea[aeropuertos.size()][aeropuertos.size()];
	}
				
	public void setReserva(String origen, String destino, String empresa, int cantidad) {	
 	    this.reservas = new HashMap<String, Integer>();
	            this.reservas.put(origen+"-"+destino+"-"+empresa, cantidad);
	            RutaAerea ruta = this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)];
	            if(ruta != null){
	            	ruta.restarAsientos(empresa, cantidad);
	            }
	        }
	    		
	protected void asignacionArco(String origen, String destino,RutaAerea nueva){
		int indiceOrigen = identificadores.get(origen);
		int indiceDestino = identificadores.get(destino);
		this.rutas[indiceOrigen][indiceDestino] = nueva;
	}
	
	public boolean esVueloCabotaje(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].esCabotaje();
	}
	
	public Iterator<String> getAeropuertos(){
		return identificadores.keySet().iterator();
	}
	
	public boolean existeRutaDirecta(String origen, String destino){
		return getRutaAerea(origen, destino) != null;
	}
	
	public Iterator<String> getReservas(){
		ArrayList<String> regreso = new ArrayList<String>();
		Set<String> keys = this.reservas.keySet();
		for(String key: keys){
			regreso.add(key + "-" + this.reservas.get(key));
		}
		return regreso.iterator();
	}
	
	public void setReservas(HashMap<String, Integer> reservas) {
		this.reservas = reservas;
	}
	
	public Iterator<String> servicio1(String origen, String destino, String aerolinea){
		ArrayList<String> salida = new ArrayList<String>();
		if (this.existeRutaDirecta(origen, destino) && getRutaAerea(origen, destino).contieneAerolinea(aerolinea)){
			salida.add("Existe una ruta directa para el origen "+origen+" y destino "+destino);
			RutaAerea encontrada = getRutaAerea(origen, destino);
			salida.add("La distancia es de "+ encontrada.getDistancia()+ " km");
			salida.add("La cantidad de asientos disponibles es "+ encontrada.asientosPorAerolinea(aerolinea));
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
		//llama a el algoritmo backtracking
		ArrayList<String> regreso = new ArrayList<String>();
		int numRecorrido = 1;
		for(Stack<HashMap<String, Object>> recorrido: rutas){
			regreso.add("recorrido " + numRecorrido++ + ": ");
			while(!recorrido.isEmpty()){
				HashMap<String, Object> vuelo = recorrido.pop();
				regreso.add("destino: " + vuelo.get("destino") + "; distancia restante: " + vuelo.get("distancia") + "; escalas restantes: " + vuelo.get("saltos") + "; aerolineas: " + vuelo.get("aerolineas"));
			}
			//prepara un iterador para devolver al main
		}
		return regreso.iterator();
	}
	
	protected ArrayList<Stack<HashMap<String, Object>>> back(int idActual, int idAnterior, int idDestino, String aerolineaEvitar, boolean[] aeropuertosExplorados){
		
		ArrayList<Stack<HashMap<String, Object>>>regreso = new ArrayList<Stack<HashMap<String, Object>>>();
		
		String aerolineas = null;
		if(this.rutas[idAnterior][idActual] != null){
			aerolineas = this.rutas[idAnterior][idActual].getAerolineasDisponiblesMenos(aerolineaEvitar);
		}
		//si no es el aeropuerto incial pide por las aerolineas que conectan a la seleccionada con la anterior
		if(aerolineas == null && idActual != idAnterior || aeropuertosExplorados[idActual]){
			return regreso;
		}
		//condicion de corte, si no aerolineas disponibles o ya esta marcada esta aerolinea en el recorrido
		if(idActual == idDestino){ //si es la solucion
			Stack<HashMap<String, Object>> recorrido = new Stack<HashMap<String, Object>>();
			//genera un recorrido
			HashMap<String, Object> viaje = new HashMap<String, Object>();
			viaje.put("distancia", this.rutas[idAnterior][idActual].getDistancia());
			viaje.put("destino", this.identificadorToName.get(idActual));
			viaje.put("saltos", 0);
			viaje.put("aerolineas", this.rutas[idAnterior][idActual].getAerolineasDisponiblesMenos(aerolineaEvitar));
			recorrido.push(viaje);
			regreso.add(recorrido);
			//agrega los datos y los pone en la pila
		}
		else{
			for(int x = 0; x < aeropuertos.size(); x++){
				if(this.rutas[idActual][x] != null ){
					//recorre la matriz buscando aeropuertos adyacentes
					aeropuertosExplorados[idActual] = true;
					ArrayList<Stack<HashMap<String, Object>>> respuestaBack = this.back(x, idActual, idDestino, aerolineaEvitar, aeropuertosExplorados);
					//hace la recursion con un aeropuerto adyacente
					if(!respuestaBack.isEmpty()){
						//si la respuesta no esta vacia (hay solucion)
						if(idActual != idAnterior){ 
							for(Stack<HashMap<String, Object>> recorrido: respuestaBack){
								//si no es el aeropuerto inicial agrega el recorrido hacia el aeropuerto actual en la cima de cada recorrido que haya encontrado el aeropuerto adyacente
								HashMap<String, Object> salto = new HashMap<String, Object>();
								salto.put("destino", this.identificadorToName.get(idActual));
								salto.put("distancia", (Double)recorrido.peek().get("distancia") + this.rutas[idAnterior][idActual].getDistancia()); 
								salto.put("saltos", (Integer)recorrido.peek().get("saltos")+ 1);
								salto.put("aerolineas",  aerolineas);
								recorrido.push(salto);
							}
						}
						regreso.addAll(respuestaBack);
						//agrega todos los recorridos a el regreso
					}
					aeropuertosExplorados[idActual] = false;
				}
			}
		}
		return regreso;
	}
	
	public Iterator<String> servicio3(String paisA, String paisB){
		ArrayList<Aeropuerto> aeropuertosPaisA = new ArrayList<Aeropuerto>();
		ArrayList<Aeropuerto> aeropuertosPaisB = new ArrayList<Aeropuerto>();
		ArrayList<String> resultado = new ArrayList<String>();
		for (Aeropuerto aeropuerto : aeropuertos) {
	    	if (aeropuerto.getPais().equals(paisA)){
	    		aeropuertosPaisA.add(aeropuerto);
	    	}
	    	if (aeropuerto.getPais().equals(paisB)){
	    		aeropuertosPaisB.add(aeropuerto);
	    	} 	
	    }
		for (Aeropuerto aeropuertoA: aeropuertosPaisA) {
			for (Aeropuerto aeropuertoB: aeropuertosPaisB) {
				RutaAerea ruta = this.getRutaAerea(aeropuertoA.getNombre(), aeropuertoB.getNombre());
				if (ruta != null) {
				   String aerolineas = ruta.getAerolineasDisponiblesMenos("");
				   if(aerolineas != null) {
					   resultado.add("Origen: "+aeropuertoA.getNombre()+" Destino: "+aeropuertoB.getNombre()+" Aerolineas con pasajes:"+aerolineas+" Distancia: "+ruta.getDistancia());
				   }
			   }
			}
		}
		return resultado.iterator();
	}
	
	public Iterator<String>getAerolineas(String origen, String destino){
		return this.rutas[this.identificadores.get(origen)][this.identificadores.get(destino)].getAerolineas();
	}
	
	public HashMap getIdentificadores() {
	    return this.identificadores;
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
