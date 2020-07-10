package com.web2.hotel.service;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.DTO.CambiarPassword;
import com.web2.hotel.entities.Authority;
import com.web2.hotel.entities.Mensajes;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.repositories.RoleRepository;
import com.web2.hotel.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository usuarioRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Iterable<Usuario> getAlluser() {
		return usuarioRepo.findAll() ;
	}

	/*chequear que el username este disponible*/
	private boolean checkUsernameDisponible(Usuario user) throws Exception{
		Optional<Usuario> userFound= usuarioRepo.findByUsername(user.getUsername());
		if(userFound.isPresent()) {
			throw new Exception("Nombre de Usuario no disponible");
		}
		return true;
	}
	
	/*chequear pass valido*/
	private boolean checkPassValido(Usuario user)throws Exception{
		if(user.getConfirmPassword().isEmpty()) {
			throw new Exception("Confirmar Password es obligatorio");
		}
		if(!user.getPassword().equals(user.getConfirmPassword())){
			throw new Exception("Clave y Confirmar Clave no son iguales");			
		}
		return true;
	}
	

	public boolean checkEmailDisponible(Usuario user) throws Exception {
		Optional<Usuario> userFound= usuarioRepo.findByCorreo(user.getCorreo());
		if(userFound.isPresent()) {
			throw new Exception("Usted ya tiene una cuenta Inicie Sesion");
		}
		return true;
	}
	
	@Override
	public Usuario createUser(Usuario user) throws Exception {
		if(checkEmailDisponible(user) && checkUsernameDisponible(user)&& checkPassValido(user)) {
			Set<Authority> setString=user.getAuthority();
			if(setString == null) {
				Set<Authority>rol=roleRepo.findByAuthority("ROLE_USER");
				user.setAuthority(rol);
			}
			String encodePassword=bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			user = usuarioRepo.save(user);
		}
		return user;
	}


	@Override
	public Usuario getUserById(long id) throws Exception {
		Usuario user=usuarioRepo.findById(id).orElseThrow(()->new Exception("No existe Usuario"));
		return user;
	}

	@Override
	public Optional<Usuario> getUserByUsername(String username) {
		Optional<Usuario> user= usuarioRepo.findByUsername(username);
		return user;
	}

	@Override
	public Usuario updateUser(Usuario formUser) throws Exception {
		Optional<Usuario> user= usuarioRepo.findById(formUser.getId());
		if (!user.isPresent()) {
			   throw new Exception("No existe el usuario");
			}

		Usuario toUser = user.get();
		mapUser(formUser, toUser);
		return usuarioRepo.save(toUser);
	}
	/*mapeo el usuario desde formUser a toUser*/
	protected void mapUser(Usuario from,Usuario toUser) {
		toUser.setNombre(from.getNombre());          
		toUser.setApellido(from.getApellido());
		toUser.setCorreo(from.getCorreo());
		toUser.setDni(from.getDni());
		toUser.setTelefono(from.getTelefono());
		toUser.setUsername(from.getUsername());
		toUser.setPassword(from.getPassword());
		toUser.setAuthority(from.getAuthority());
	
	}
	
	@Override
	public Usuario updateDatosOcultos(Usuario user) throws Exception {
		Optional<Usuario>us=usuarioRepo.findById(user.getId());
		Set<Authority>rol=us.get().getAuthority();
		Set<Reservas>reservas=us.get().getReservas();
		Set<Mensajes>mensajes=us.get().getMensaje();
		
		user.setAuthority(rol);
		user.setReservas(reservas);
		user.setMensaje(mensajes);
		return user;
	}

	@Override
	public void deleteUser(long id) throws Exception {
		Usuario user=usuarioRepo.findById(id).orElseThrow(()-> new Exception("No existe el usuario"));
		usuarioRepo.delete(user);
	}

	@Override
	public Usuario changePassword(CambiarPassword form) throws Exception {
		Usuario storedUser = usuarioRepo.findById(form.getId())
				.orElseThrow(() -> new Exception("Usuario no encontrado -"+this.getClass().getName()));

		/*if(!storedUser.getPassword().equals(pw)) {
			throw new Exception("Clave actual incorecta");
		}
		if(!bCryptPasswordEncoder.matches(pwguardadoString, pw1)) {
			throw new Exception("Clave actual incorecta");
		}
		
		if ( form.getPasswordActual().equals(form.getNewPassword())) {
			throw new Exception("Nueva clave debe ser diferenta a clave actual");
		}*/
		
		if( !form.getNewPassword().equals(form.getConfirmaPassword())) {
			throw new Exception("la nueva clave y confirme clave no son iguales");
		}
		
		String encodePass=bCryptPasswordEncoder.encode(form.getNewPassword());
		storedUser.setPassword(encodePass);
		return usuarioRepo.save(storedUser);
	}
	@Override
	public String getUsuarioLogueado() throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		
		
		if (principal instanceof UserDetails) { 
			 username = ((UserDetails)principal).getUsername(); 
			} else { 
			username = principal.toString(); 
			}
		
		return username;
	}

	@Override
	public boolean isAdmin(Optional<Usuario> usuarioLogueado) throws Exception {
		Set<Authority> roles = usuarioLogueado.get().getAuthority();
		boolean isAdmin=false;
		for(Authority rol: roles) {
			if(rol.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin=true;
			}
		}
		return isAdmin;
	}
	

}
