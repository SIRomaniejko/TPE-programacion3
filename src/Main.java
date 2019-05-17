import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class Main {
	public static String output = "output/";
	public static String input = "input/";
	public static void main(String[] args) throws IOException {
		Grafo grafo = new Grafo(input);
		String entrada = "start";
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
			if(!entrada.equals("esc")){
				leerEntrada();
			}
		}
	}
	public static void printRespuesta(Iterator<String> respuesta){
		BufferedWriter bw = null;
		int numUso = 0;
		try {
			File file = new File(output + "log" + numUso + ".txt");
			while(file.exists()){
				numUso++;
				file = new File(output + "log" + numUso + ".txt");
			}
			file.createNewFile();


			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			while(respuesta.hasNext()){
				String contenidoLinea1 = respuesta.next();
				System.out.println(contenidoLinea1);
				bw.newLine();
				bw.write(contenidoLinea1);
			}
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
