package com.web2.hotel.repositories;

import java.util.ArrayList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.Reservas.Estado;

@Repository
public interface ReservaRepository extends CrudRepository<Reservas, Long>{
	
	public ArrayList<Reservas> findAllByEstado(Estado estado);
	

}
