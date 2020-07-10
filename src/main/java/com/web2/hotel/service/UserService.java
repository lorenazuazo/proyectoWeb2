package com.web2.hotel.service;

import java.util.Optional;

import com.web2.hotel.DTO.CambiarPassword;
import com.web2.hotel.entities.Usuario;

public interface UserService {
	
	public Iterable<Usuario> getAlluser();
	
	public Usuario createUser(Usuario formUser) throws Exception;
	
	public Usuario getUserById(long id)throws Exception;

	public Optional<Usuario> getUserByUsername(String username);
	
	public Usuario updateUser(Usuario user)throws Exception;
	
	public Usuario updateDatosOcultos(Usuario user)throws Exception;
	
	public void deleteUser(long id) throws Exception;
	
	public Usuario changePassword(CambiarPassword form) throws Exception;
	
	public String getUsuarioLogueado()throws Exception;
	
	public boolean isAdmin(Optional<Usuario> usuarioLogueado)throws Exception;


}
