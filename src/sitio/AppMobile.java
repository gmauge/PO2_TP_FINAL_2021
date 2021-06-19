package sitio;

import publicacion.Publicacion;

public class AppMobile implements Observador{
	private Publicacion publicacion;

	@Override
	public Publicacion getPublicacion() {
		return this.publicacion;
	}

	@Override
	public void notificar() {
		String mensaje = "Se ha cancelado una resserva en " + this.getPublicacion().getInmueble().getCiudad();
		String color = "Azul";
		Integer fontSize = 14;
		/*
		 APP_EXTERNA.popUp(mensaje, color, fontSize);
		 */
		
	}
	
	

}