import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {
	
	public static void main(String[] args) throws IOException {
		String output = "output/";
		String input = "input/";
		Grafo grafo = new Grafo(input);
		String entrada = "start";
		Iterator<String> respuesta = null;
		String origen;
		String destino;
		String aerolinea;
		String paisA;
		String paisB;
		while(!entrada.equals("esc")){
			System.out.println("entrar que opcion usar");
			System.out.println("1. listar todos los aeropuertos");
			System.out.println("2. listar todas las reservas realizadas");
			System.out.println("3. servicio 1: verificar vuelo directo");
			System.out.println("4. servicio 2: obtener recorridos posibles sin aerolinea");
			System.out.println("5. servicio 3: vuelos disponibles entre dos paises");
			System.out.println("6. generar archivo con grafo");
			System.out.println("esc. cerrar el programa");
			entrada = leerEntrada();
			switch(entrada){
			case "esc":
				break;
			case "1":
				printRespuesta(grafo.getAeropuertos());
				break;
			case "2":
				printRespuesta(grafo.getReservas());
				break;
			case "3":
				System.out.println("entrar aeropuerto origen");
				origen = leerEntrada();
				System.out.println("entrar aeropuerto destino");
				destino = leerEntrada();
				System.out.println("entrar aerolinea");
				aerolinea = leerEntrada();
				printRespuesta(grafo.servicio1(origen, destino, aerolinea));
				break;
			case "4":
				System.out.println("entrar aeropuerto origen");
				origen = leerEntrada();
				System.out.println("entrar aeropuerto destino");
				destino = leerEntrada();
				System.out.println("entrar aerolinea a evitar");
				aerolinea = leerEntrada();
				printRespuesta(grafo.servicio2(origen, destino, aerolinea));
				break;
			case "5":
				System.out.println("entrar pais origen");
				paisA = leerEntrada();
				System.out.println("entrar pais destino");
				paisB = leerEntrada();
				printRespuesta(grafo.servicio3(paisA, paisB));
			}
			leerEntrada();
		}
	}
	public static void printRespuesta(Iterator<String> respuesta){
		while(respuesta.hasNext()){
			System.out.println(respuesta.next());
		}
	}
	
	public static String leerEntrada(){
		String regreso = null;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		try{
			regreso = input.readLine();
		}
		catch(Exception exc){
			System.out.println(exc);
		}
		return regreso;
	}

}
