import java.util.ArrayList;
import java.util.HashMap;


public class GrafoNoDirigido extends Grafo {

	public GrafoNoDirigido(ArrayList<Aeropuerto> aero) {
		super(aero);
	}
	protected void asignacionArco(String origen, String destino, RutaAerea nueva){
		int indiceOrigen = identificadores.get(origen);
		int indiceDestino = identificadores.get(destino);
		this.rutas[indiceDestino][indiceOrigen] = nueva;
		super.asignacionArco(origen, destino, nueva);
	}
	
}
