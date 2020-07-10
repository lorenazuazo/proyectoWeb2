package com.web2.hotel.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.DTO.MensajesDTO;
import com.web2.hotel.entities.Mensajes;
import com.web2.hotel.entities.MesajesUsuarioAnonimo;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.repositories.MensajeRepository;
import com.web2.hotel.repositories.MesajesUsuarioAnonimoRepository;
import com.web2.hotel.repositories.UserRepository;

@Service
public class MensajeServiceImpl implements MensajeService{
	
	@Autowired
	MensajeRepository mensajeRepo;
	

	@Autowired
	UserRepository usuarioRepo;
	
	@Autowired
	MesajesUsuarioAnonimoRepository mensajeAnonimoRepo;
	
	@Override
	public Iterable<Mensajes> getAllmensaje() {
		return mensajeRepo.findAll();
	}
	


	@Override
	public void regisitrarMensaje(Mensajes mensaje) throws Exception {
		Optional<Usuario> userFound= usuarioRepo.findByCorreo(mensaje.getCorreo());
			if(userFound.isPresent()) {
				 Mensajes mensajeTo=new Mensajes();
				 mensajeTo.setNombre(mensaje.getNombre());
				 mensajeTo.setApellido(mensaje.getApellido());
				 mensajeTo.setCorreo(mensaje.getCorreo());
				 mensajeTo.setMensaje(mensaje.getMensaje());
				 mensajeTo.setUsuario(userFound.get());
				 mensajeRepo.save(mensajeTo);
			}else {
				MesajesUsuarioAnonimo mensajeTo=new MesajesUsuarioAnonimo();
				mensajeTo.setNombre(mensaje.getNombre());
				mensajeTo.setApellido(mensaje.getApellido());
				mensajeTo.setCorreo(mensaje.getCorreo());
				mensajeTo.setMensaje(mensaje.getMensaje());
				mensajeAnonimoRepo.save(mensajeTo);
			}
	}

	
	
	@Override
	public Iterable<MensajesDTO> getMensajes() throws Exception {
			
			ArrayList<Mensajes> mensajesUsuarioRegistrado=(ArrayList<Mensajes>) mensajeRepo.findAll();
			ArrayList<MesajesUsuarioAnonimo>mensajeUsuarioAnonimo=(ArrayList<MesajesUsuarioAnonimo>) mensajeAnonimoRepo.findAll();
			ArrayList<MensajesDTO> todosJuntos= new ArrayList<>();
			long sizemensajesUsuarioRegistrado = mensajesUsuarioRegistrado.spliterator().getExactSizeIfKnown();
			long sizemensajeUsuarioAnonimo = mensajeUsuarioAnonimo.spliterator().getExactSizeIfKnown();
			
			if((sizemensajesUsuarioRegistrado == 0)&&(sizemensajeUsuarioAnonimo == 0)) {
				throw new Exception("No existen mensajes");
			}
			
			
			if(!(sizemensajesUsuarioRegistrado == 0)) {
				for(Mensajes mens:mensajesUsuarioRegistrado) {
					MensajesDTO mensajeDTO = new MensajesDTO();
					String idUsuarioString=Long.toString(mens.getUsuario().getId());
					
					mensajeDTO.setNombre(mens.getNombre());
					mensajeDTO.setApellido(mens.getApellido());
					mensajeDTO.setCorreo(mens.getCorreo());
					mensajeDTO.setMensaje(mens.getMensaje());
					mensajeDTO.setEstado(idUsuarioString);
					
					todosJuntos.add(mensajeDTO);
					
				}	
			}
		
		    if (!(sizemensajeUsuarioAnonimo == 0)) {
				for(MesajesUsuarioAnonimo mens:mensajeUsuarioAnonimo) {
					MensajesDTO mensajeDTO = new MensajesDTO();
					
					mensajeDTO.setNombre(mens.getNombre());
					mensajeDTO.setApellido(mens.getApellido());
					mensajeDTO.setCorreo(mens.getCorreo());
					mensajeDTO.setMensaje(mens.getMensaje());
					mensajeDTO.setEstado("Anonimo");
					
					todosJuntos.add(mensajeDTO);
				}
			}
		return todosJuntos;
	}

}
