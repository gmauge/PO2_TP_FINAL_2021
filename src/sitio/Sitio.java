package sitio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import calificacion.Calificable;
import calificacion.Calificacion;
import categoria.Categoria;
import formasDePago.FormaDePago;
import inmueble.Inmueble;
import publicacion.Publicacion;
import reserva.Reserva;
import usuario.Usuario;
import servicios.Servicio;
import tipoInmueble.TipoDeInmueble;

public class Sitio {
	private static Sitio sitio;
	private static ObserverManager gestorDeNotificaciones;
	private static List<Usuario> usuario;
	private Set<Categoria> categorias; 
	private Set<FormaDePago> formasDePago;
	private Set<Servicio> servicios;
	private Set<TipoDeInmueble> tiposDeInmuebles;
	
	private Sitio() {
		this.setGestorDeNotificaciones( ObserverManager.getInstance() );
		this.setUsuario( new ArrayList<>());
		servicios = new HashSet<Servicio>();
		categorias =new HashSet<Categoria>();
		formasDePago = new HashSet<FormaDePago>();	
		tiposDeInmuebles= new HashSet<TipoDeInmueble>();
	}
	
	public static Sitio getInstance() {
		if (sitio == null) {
			sitio = new Sitio( );
		}
		return sitio;
	}

	private static ObserverManager getGestorDeNotificaciones() {
		return gestorDeNotificaciones;
	}

	private void setGestorDeNotificaciones(ObserverManager gestorDeNotificaciones) {
		Sitio.gestorDeNotificaciones = gestorDeNotificaciones;
	}

	public List<Usuario> getUsuario() {
		return usuario;
	}

	private void setUsuario(List<Usuario> usuario) {
		Sitio.usuario = usuario;
	}
	
	public void addUsuario(Usuario usuario) {
		this.getUsuario().add(usuario);
	}

	public static void procesarReservaCancelada(Reserva reserva) {
		reserva.getPublicacion().aplicarPoliticaCancelacion(reserva);
		getGestorDeNotificaciones().alertarCancelacion(reserva);
		asignarProximaReservaCondicional(reserva);
	}
	
	private static void asignarProximaReservaCondicional(Reserva reserva) {
		List<Reserva> reservaCoincide = new ArrayList<>();
		Reserva reservaSiguiente;
		reservaCoincide = getReservasCondicionalesQueCoincidenCon(reserva);
		if (reservaCoincide.size() > 0) {
			reservaSiguiente = reservaCoincide.stream().min((Comparator<? super Reserva>) reservaCoincide.stream().map(res -> res.getFecgaHoraReserva())).get();
			reservaSiguiente.aceptar();
		}
	}

	private static List<Reserva> getReservasCondicionales(){
		List<Reserva> reservaCondicional = new ArrayList<>();
		usuario.forEach(usuario -> reservaCondicional.addAll( usuario.todasLasReservas().stream().filter(res -> res.esCondicional()).collect(Collectors.toList()) )) ;
		return reservaCondicional;
	}
	
	private static List<Reserva> getReservasCondicionalesQueCoincidenCon(Reserva reserva){
		List<Reserva> reservaCondicional = new ArrayList<>();
		reservaCondicional = getReservasCondicionales();
		return  reservaCondicional.stream().filter(res -> res.getPublicacion().getInmueble().equals(reserva.getPublicacion().getInmueble())  && res.getFechaInicio().equals(reserva.getFechaInicio())  && res.getFechaFin().equals(reserva.getFechaFin()) ).collect(Collectors.toList());
		
	}
	
	public static void procesarBajaDePrecio(Publicacion publicacion) {
		getGestorDeNotificaciones().alertarBajaDePrecio(publicacion);
	} 
	
	public void addCategoria(Categoria categoria) {
		categorias.add(categoria);
	}
	
	public void  calificar (Calificable calificable ,Reserva unaReserva, Calificacion unaCalificacion ) {
		if(unaReserva.estaFinalizada()) {
			calificable.setCalificacion(unaCalificacion);
		}
	}
	
	public void addFormaDePago(FormaDePago unaFormaDePago) {
		this.formasDePago.add(unaFormaDePago);
	}
	
	public double promedioGeneralCalificaciones(Calificable unCalificable) {
		double suma = 0.0;
		List<Calificacion> calificaciones = new ArrayList<Calificacion>();
		calificaciones  = unCalificable.getCalificaciones();
		for (Calificacion calificacion:calificaciones) {
			suma += calificacion.getPuntaje();
		}
		return suma/calificaciones.size();
	}	
	
	
	public Map<Categoria,Double> promedioPorCategoria (Calificable calificable) {
		Map<Categoria,Double> categorias = new HashMap<Categoria,Double>();
		List<Calificacion> calificaciones = new ArrayList<Calificacion>();
		
		calificaciones = calificable.getCalificaciones();
		
		for (Calificacion calificacion:calificaciones) {
			categorias.put(calificacion.getCategoria(),this.promedioDe(calificable, calificacion.getCategoria())); 
		}
		return categorias;
	
	}
	
	public double promedioDe(Calificable calificable, Categoria unaCategoria) {
		List<Calificacion> calificaciones = new ArrayList<Calificacion>();
		double acumuladorDePuntaje = 0.0;
		calificaciones = calificable.getCalificaciones().stream().
				filter(filtroCategoria-> filtroCategoria.getCategoria() == unaCategoria)
				.collect(Collectors.toList());
		for (Calificacion unaCalificacion : calificaciones) {
			acumuladorDePuntaje += unaCalificacion.getPuntaje();
		}
		return acumuladorDePuntaje / calificaciones.size();
			
	}
	
	
	
	public void addTipoInmueble (TipoDeInmueble unTipoDeInmueble) {
		tiposDeInmuebles.add(unTipoDeInmueble);
	}
	
	public Collection<TipoDeInmueble> getTiposDeInmuebles() {
		return this.tiposDeInmuebles;
	}
	
	public void addServicio(Servicio unServicio) {
		servicios.add(unServicio);
	}
	
	public Collection<Servicio> getServicios(){
		return this.servicios;
	}

	public ArrayList<Publicacion> buscarPublicacion(CriterioBasico criterio){
		return criterio.lasQueCumplen(usuario.stream().flatMap(u -> u.getPublicaciones().stream()).collect(Collectors.toCollection(ArrayList::new)));
	
	}
	
	
	
public List <Inmueble> inmueblesLibresHoy() {
		
		List<Inmueble> inmueblesLibresHoy = new ArrayList<>(); 
			for (Usuario unUsuario: Sitio.usuario) {
				for (Publicacion unaPublicacion : unUsuario.getPublicaciones()) {
					if (unaPublicacion.disponibleHoy()) {
						inmueblesLibresHoy.add(unaPublicacion.getInmueble());
					}
				}
			}
		return inmueblesLibresHoy;		
	}
	
	/**
	 * Retorna los 10 primeros inquilinos que mas alquilaron sus inmuebles
	 * @return List<Usuario>
	 */
	public List<Usuario> topTenInquilinos() {
		
		List<Usuario> topten = this.getUsuario();
		topten.sort((u1, u2) -> this.compare(u1, u2));
		Collections.reverse(topten);
		return topten.stream().limit(10).collect(Collectors.toList());
	}
	
	/**
	 * Retorna la tasa de ocupacion (inmuebles ocupados sobre todos los inmuebles) en la fecha dada (actual)
	 * @param fechaHoy Calendar
	 * @return double
	 */
	public double tasaDeOcupacion(Calendar fechaHoy) {
		return (this.todasLasPublicaciones() - this.cantidadDeDisponiblesHoy(fechaHoy)) / this.todasLasPublicaciones();
	}
	
	/**
	 * Retorna la cantidad de publicaciones/inmuebles del sitio
	 * @return double
	 */
	public double todasLasPublicaciones() {
		return this.getUsuario().stream().flatMap(u -> u.getPublicaciones().stream()).count();
	}
	
	/**
	 * Retorna la cantidad de inmuebles disponibles en la fecha dada (actual)
	 * @param fechaHoy
	 * @return double
	 */
	public double cantidadDeDisponiblesHoy(Calendar fechaHoy) {
		return this.getUsuario().stream().flatMap(u -> u.getPublicaciones().stream()).filter(p -> p.disponibleHoy(fechaHoy)).count();
	}
	
	  /**
	   * Compara dos usuarios a traves de la cantidad de sus alquileres concretados
	   * @param o1 Usuario
	   * @param o2 Usuario
	   * @return int
	   */
	  public int compare(Usuario o1, Usuario o2) {
	        Usuario persona1 = (Usuario)o1;
	        Usuario persona2 = (Usuario)o2;
	        return persona1.cantidadDeAlquileres().
	                compareTo(persona2.cantidadDeAlquileres());
	               
	    }
	  
	  public Set<FormaDePago> getFormasDePago(){
		  return this.formasDePago;
	  }
	
	
	}


