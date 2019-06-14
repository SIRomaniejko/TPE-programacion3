import java.util.ArrayList;
import java.util.HashMap;


public class GrafoNoDirigido extends Grafo {

	public GrafoNoDirigido(ArrayList<Aeropuerto> listaAeropuertos) {
		super(listaAeropuertos);
	}
	
	protected void asignacionArco(String origen, String destino, RutaAerea nueva){
		int indiceOrigen = identificadores.get(origen);
		int indiceDestino = identificadores.get(destino);
		this.rutas[indiceOrigen][indiceDestino] = nueva;
		super.asignacionArco(origen, destino, nueva);
	}
	
}
