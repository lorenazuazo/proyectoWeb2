package com.web2.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.MesajesUsuarioAnonimo;
import com.web2.hotel.repositories.MesajesUsuarioAnonimoRepository;

@Service
public class MesajesUsuarioAnonimoServiceImpl implements MesajesUsuarioAnonimoService{
	
	@Autowired
	MesajesUsuarioAnonimoRepository mensajeAnonimoRepo;

	@Override
	public Iterable<MesajesUsuarioAnonimo> getAllmensaje() {
		return mensajeAnonimoRepo.findAll();
	}

}
