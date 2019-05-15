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
		Iterator<String> respuesta = grafo.servicio1("John F. Kennedy", "Ministro Pistarini", "United Airlines");
		
		Iterator<String> regreso = grafo.servicio2suposicionInicial("John F. Kennedy", "Ministro Pistarini", "d");
		while(regreso.hasNext()){
			System.out.println(regreso.next());
		}
	}

}
