import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
	public static String output = "output/";
	public static String input = "input2/";
	public static Grafo grafo;
	public static String line = "";
	public static String cvsSplitBy = ";";
	
	public static void main(String[] args) throws IOException {
		grafo = cargarGrafo();
	    cargarRutas();
	    cargarReservas();
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
			System.out.println("6. recorrer todos aeropuertos Greedy");
			System.out.println("7. recorrer todos aeropuertos Backtracking");
			System.out.println("create. generar archivo para visualizar grafo");
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
			case "6":
				System.out.println("entrar origen");
				origen = leerEntrada();
				printRespuesta(grafo.recorridoMasCortoGreedy(origen));
				break;
			case "7":
				System.out.println("entrar origen");
				origen = leerEntrada();
				printRespuesta(grafo.recorridoCortoBacktracking(origen));
			case "create":
				grafo.generateGraph(output);
			}
			if(!entrada.equals("esc")){
				leerEntrada();
			}
		}
	}
	
	public static Grafo cargarGrafo(){
		ArrayList<Aeropuerto> listaAeropuertos = new ArrayList<Aeropuerto>();
		try (BufferedReader br = new BufferedReader(new FileReader((input + "Aeropuertos.csv")))) {
	        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
	            String[] items = line.split(cvsSplitBy);
	            String nombre = items[0];
	            String ciudad = items[1];
	            String pais = items[2];
	            Aeropuerto nuevo = new Aeropuerto(nombre, ciudad, pais);
	            listaAeropuertos.add(nuevo);
	        }
	        Grafo g = new GrafoNoDirigido(listaAeropuertos);
	        return g;	
    }
    catch (IOException e) {
	        e.printStackTrace();
	          }	
	return null;
	}
	
	public static void cargarRutas() {
		 try (BufferedReader br = new BufferedReader(new FileReader(input + "Rutas.csv"))) {
		        while ((line = br.readLine()) != null) {
		            String[] items = line.split(cvsSplitBy);
		            String origen = items[0];
		            String destino = items[1];
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
		            RutaAerea nueva = new RutaAerea(distancia, esCabotaje, aeroLineasFinal);
		            grafo.asignacionArco(origen, destino, nueva);
		    
		        } 
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	  
	}
	
	public static void cargarReservas() {   //carga de las reservas en el grafo
	    try (BufferedReader br = new BufferedReader(new FileReader(input + "Reservas.csv"))) {                                                              // salteo el encabezado
	        while ((line = br.readLine()) != null) {          // mientras haya lineas para leer en el csv		
	            String[] items = line.split(cvsSplitBy);
	            String origen = items[0];
	            String destino = items[1];
	            String empresa = items[2];
	            int cantidad = Integer.parseInt(items[3]);
	            grafo.setReserva(origen, destino, empresa, cantidad);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
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
