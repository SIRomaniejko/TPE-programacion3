
public class Aeropuerto {
	String nombre;
	String ciudad;
	String pais;
	
	public Aeropuerto(String nombre, String ciudad, String pais){
		this.nombre = nombre;
		this.pais = pais;
		this.ciudad = ciudad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
	
}
