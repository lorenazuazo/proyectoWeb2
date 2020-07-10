package com.web2.hotel.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.Usuario;

@Repository
public interface UserRepository extends CrudRepository<Usuario, Long>{
	
	public Optional<Usuario> findByUsername(String username);
	
	public Optional<Usuario> findByCorreo(String email);
	
	
}