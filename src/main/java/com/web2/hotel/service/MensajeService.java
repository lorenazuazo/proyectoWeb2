package com.web2.hotel.service;

import com.web2.hotel.DTO.MensajesDTO;
import com.web2.hotel.entities.Mensajes;

public interface MensajeService {
	
	public Iterable<Mensajes> getAllmensaje();
	
	public void regisitrarMensaje(Mensajes mensaje) throws Exception;
	
	public Iterable<MensajesDTO> getMensajes() throws Exception;

}
