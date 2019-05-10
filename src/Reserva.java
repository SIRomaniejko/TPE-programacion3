
public class Reserva {
	String origen;
	String destino;
	String empresa;
	int cantidad;
	
	public Reserva(String origen, String destino, String empresa, int cantidad) {
		this.origen = origen;
		this.destino = destino;
		this.empresa = empresa;
		this.cantidad = cantidad;
	}

	public String toString(){
		return "origen: " + this.origen + "; destino: " + this.destino + "; empresa: " + this.empresa + "; cantidad: " + this.cantidad;
	}
}
