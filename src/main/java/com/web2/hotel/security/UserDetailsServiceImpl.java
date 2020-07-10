package com.web2.hotel.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.Authority;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
	
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	     //Buscar el usuario con el repositorio y si no existe lanzar una exepcion
    	Optional<Usuario> appUser = userRepository.findByUsername(username);
				//.orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));
			
	    //Mapear nuestra lista de Authority con la de spring security 
	    List grantList = new ArrayList();
	    for (Authority authority: appUser.get().getAuthority()) {
	        // ROLE_USER, ROLE_ADMIN,..
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
	            grantList.add(grantedAuthority);
	    }
			
	    //Crear El objeto UserDetails que va a ir en sesion y retornarlo.
	    UserDetails user = (UserDetails) new User(appUser.get().getUsername(), appUser.get().getPassword(), grantList);
	         return user;
	    }
}