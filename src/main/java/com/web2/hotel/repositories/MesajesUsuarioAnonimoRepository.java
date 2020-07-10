package com.web2.hotel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.MesajesUsuarioAnonimo;

@Repository
public interface MesajesUsuarioAnonimoRepository extends CrudRepository<MesajesUsuarioAnonimo, Long>{

}
