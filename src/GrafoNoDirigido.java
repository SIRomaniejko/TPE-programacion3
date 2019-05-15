import java.util.HashMap;


public class GrafoNoDirigido extends Grafo {

	public GrafoNoDirigido(String pathRootFolder) {
		super(pathRootFolder);
	}
	protected void asignacionArco(int origen, int destino, boolean esCabotaje, double distancia, HashMap<String, Integer> aeroLineasFinal){
		this.rutas[destino][origen] = new RutaAerea(distancia, esCabotaje, aeroLineasFinal);
		super.asignacionArco(origen, destino, esCabotaje, distancia, aeroLineasFinal);
	}
	
}
