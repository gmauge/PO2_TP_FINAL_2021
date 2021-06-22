package inmueble;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import calificacion.Calificable;
import calificacion.Calificacion;
import formasDePago.FormaDePago;
import politicaCancelacion.PoliticaDeCancelacion;
import tipoInmueble.TipoDeInmueble;
import usuario.Usuario;
import servicios.Servicio;


public class Inmueble implements Calificable{
	
	private ArrayList<Calificacion> calificaciones; 
	private ArrayList<FormaDePago> formaDePago;
	private TipoDeInmueble tipo; 
	private Integer superficie;
	private String pais;
	private String ciudad;
	private String direccion ;
	private ArrayList <Servicio> servicios;
	private Integer capacidad; 
	private Calendar checkIN;
	private Calendar checkOUT;
	private Integer cantidadDeVecesAlquilado;
	private PoliticaDeCancelacion politicaCancelacion;

	public Inmueble(TipoDeInmueble tipoInmueble,double superficie, 
			String pais, String ciudad,String direccion, 
			ArrayList<Servicio> servicios, int capacidad) {
	}
	
	public void setModoDePago (ArrayList <FormaDePago> formasDePago) {
			
		this.formaDePago = formasDePago;
	}
	
	public void aumentarCantidadVecesAlquilado() {
		this.cantidadDeVecesAlquilado = this.getCantidadDeVecesAlquilado() + 1;
	}

	public Integer getCantidadVecesAlquilado() {
		return this.cantidadDeVecesAlquilado;
	}
	

	public ArrayList<FormaDePago> getFormaDePago() {
		return formaDePago;
	}
	
	
	public void setFormaDePago(ArrayList<FormaDePago> formaDePago) {
		this.formaDePago = formaDePago;
	}
	
	public TipoDeInmueble getTipo() {
		return tipo;
	}

	
	public void setTipo(TipoDeInmueble tipo) {
		this.tipo = tipo;
	} 
	
	public boolean estaOcupadoEn(Calendar fecha) {
		return this.checkIN.after(fecha) && this.checkOUT.before(fecha);
	}

	
	public Integer getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Integer getCantidadDeVecesAlquilado() {
		return cantidadDeVecesAlquilado;
	}

	public void setCantidadDeVecesAlquilado(Integer cantidadDeVecesAlquilado) {
		this.cantidadDeVecesAlquilado = cantidadDeVecesAlquilado;
	}

	public PoliticaDeCancelacion getPoliticaCancelacion() {
		return politicaCancelacion;
	}

	public void setPoliticaCancelacion(PoliticaDeCancelacion politicaCancelacion) {
		this.politicaCancelacion = politicaCancelacion;
	} 
	
	@Override
	public void setCalificacion(Usuario unUsuario, String comentario, int puntaje) {

		Calificacion calificacionDeUsuario;				
		calificacionDeUsuario = this.calificaciones.stream()
							.filter(unaCalificacion -> unaCalificacion.getOrigen() == unUsuario)
							.collect(Collectors.toList()).get(0);
		calificacionDeUsuario.setComentario(comentario);
		calificacionDeUsuario.setPuntaje(puntaje);
		
	}
	
	@Override
	public List <Calificacion> getCalificaciones() {
		return this.getCalificaciones();
		
	}

	public Calendar getCheckOUT() {
		return this.checkOUT;
	}
	
}