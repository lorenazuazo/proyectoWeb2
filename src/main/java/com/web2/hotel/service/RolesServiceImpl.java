package com.web2.hotel.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.Authority;
import com.web2.hotel.repositories.RoleRepository;

@Service
public class RolesServiceImpl implements RolesService{
	
	@Autowired
	RoleRepository roleRepo;

	@Override
	public Iterable<Authority> getAllRoles() {
		return roleRepo.findAll();
	}
	
	/*chequear que el username este disponible*/
	private boolean checkRoleDisponible(Authority role) throws Exception{
		Optional<Authority> roleFound= roleRepo.findById(role.getId());
		if(roleFound.isPresent()) {
			throw new Exception("Rol no disponible");
		}
		return true;
	}

	@Override
	public Authority createRole(Authority fromRole) throws Exception {
		Authority rol = null;
		if(checkRoleDisponible(fromRole)) {
			rol = roleRepo.save(fromRole);
		}
		return rol;
	}

	@Override
	public Authority getRoleById(long id) throws Exception {
		Authority rol=roleRepo.findById(id).orElseThrow(()->new Exception("No existe Rol"));
		return rol;
	}

	@Override
	public Authority updateRole(Authority fromRole) throws Exception {
		Optional<Authority> role=roleRepo.findById(fromRole.getId());
		if (!role.isPresent()) {
			   throw new Exception("No existe el Rol");
			}

		Authority toRole = role.get();
		mapRole(fromRole, toRole);
		return roleRepo.save(toRole);
	}
	/*mapeo el Rol desde form a to*/
	protected void mapRole(Authority from,Authority toRole) {
		toRole.setAuthority(from.getAuthority());	
	}

	@Override
	public void deleteRole(long id) throws Exception {
		Authority role=roleRepo.findById(id).orElseThrow(()-> new Exception("No existe el Rol"));
		roleRepo.delete(role);
		
	}

}
