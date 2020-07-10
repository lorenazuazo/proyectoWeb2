package com.web2.hotel.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.Authority;

@Repository
public interface RoleRepository extends CrudRepository<Authority, Long>{
	
	public Set<Authority> findByAuthority(String authority);

}
