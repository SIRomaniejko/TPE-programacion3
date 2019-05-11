import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class Main {

	public static void main(String[] args) throws IOException {
		String output = "output/";
		String input = "input/";
		// TODO Auto-generated method stub
		Grafo grafo = new Grafo(input);
		Iterator<String> aeropuertos = grafo.getAeropuertos();
		while(aeropuertos.hasNext()){
			System.out.println(aeropuertos.next());
		}
		Iterator<String> reservas = grafo.getReservas();
		while(reservas.hasNext()){
			System.out.println(reservas.next());
		}
		
		System.out.println(grafo.existeRutaDirecta("John F. Kennedy", "Ministro Pistarini"));
		Iterator<String> vuelos = grafo.getVuelos("John F. Kennedy", "Ministro Pistarini");
		while(vuelos.hasNext()){
			System.out.println(vuelos.next());
		}
		grafo.generateGraph(output);
	}

}
