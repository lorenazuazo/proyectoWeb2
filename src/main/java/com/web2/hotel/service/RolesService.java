package com.web2.hotel.service;

import com.web2.hotel.entities.Authority;

public interface RolesService {

	public Iterable<Authority> getAllRoles();
	
	public Authority createRole(Authority fromRole) throws Exception;
	
	public Authority getRoleById(long id)throws Exception;
	
	public Authority updateRole(Authority role)throws Exception;
	
	public void deleteRole(long id) throws Exception;
}
