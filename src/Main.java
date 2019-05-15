import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class Main {

	public static void main(String[] args) throws IOException {
		String output = "output/";
		String input = "input/";
		// TODO Auto-generated method stub
		Grafo grafo = new Grafo(input);
		grafo.generateGraph(output);
		Iterator<String> respuesta = grafo.servicio1("Westchester", "Logan", "United Ailines");

		Iterator<String> regreso = grafo.servicio2("John F. Kennedy", "Ministro Pistarini", "LATAM");
		while(regreso.hasNext()){
			System.out.println(regreso.next());
		}
	}

}
