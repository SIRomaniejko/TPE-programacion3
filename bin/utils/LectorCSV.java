package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LectorCSV {

	protected static ArrayList<Aeropuerto> cargaDeAeropuertos(String csvFile) {
		ArrayList listaAeropuertos<Aeropuerto> = new ArrayList<Aeropuerto>();
	    String line = "";
	    String cvsSplitBy = ";";
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			    line = br.readLine();                                                               // salteo el encabezado
		        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
		            String[] items = line.split(cvsSplitBy);
		            String nombre = items[0];
		            String ciudad = items[1];
		            String pais = items[2];
		            Aeropuerto nuevo = new Aeropuerto(nombre, ciudad, pais);
		            listaAeropuertos.add(nuevo);
		            }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return listaAeropuertos;
		}
	
	protected static ArrayList<Reserva> cargaDeReservas(String csvFile) {
		ArrayList listaReservas<Reserva> = new ArrayList<Reserva>();
		Reserva nodo = new Reserva();
	    String line = "";
	    String cvsSplitBy = ";";
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			    line = br.readLine();                                                               // salteo el encabezado
		        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
		            String[] items = line.split(cvsSplitBy);
		            String origen = items[0];
		            String destino = items[1];
		            String empresa = items[2];
		            String cantidad = items[3];
                    Reserva nueva = new Reserva(origen, destino, empresa, cantidad);
                    listaReservas.add(nueva);
		            }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return listaReservas;
		}
	
	protected static ArrayList<RutaAerea> cargaDeRutas(String csvFile) {
		ArrayList<RutaAerea> listaRutas= new ArrayList<RutaAerea>();
	    String line = "";
	    String cvsSplitBy = ";";
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			    line = br.readLine();                                                                   // salteo el encabezado
		        while ((line = br.readLine()) != null) {						                    // mientras haya lineas para leer en el csv		
		            String[] items = line.split(cvsSplitBy);
		            String origen = items[0];
		            String destino = items[1];
		            String distancia = items[2];
		            String cabotaje = items[3]; 
		            //String[] lineas = items[4].split(",");
		            //System.out.println(lineas);
		            String linea = items[4].replace("{","");
		            linea = linea.replace("}","");
		            String[] lineas = linea.split(",");
		            HashMap<String, Integer> aerolineas = new HashMap<String, Integer>();
		            for (int x=0; x < lineas.length; x++) {
		            	String[]aerolinea = lineas[x].split("-");
		            	aerolineas.put(aerolinea[0], Integer.parseInt(aerolinea[1]));
		            }
		            RutaAerea nuevaRuta = new RutaAerea(origen, destino, distancia, cabotaje, aerolineas);
                    listaRutas.add(nuevaRuta);
		        }
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return listaRutas;
		}
}	