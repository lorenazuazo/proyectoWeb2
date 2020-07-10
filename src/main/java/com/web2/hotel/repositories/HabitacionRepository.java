package com.web2.hotel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.Habitacion;
import com.web2.hotel.entities.Habitacion.EstadoHabitacion;;

@Repository
public interface HabitacionRepository extends CrudRepository<Habitacion, Long>{
	
	public Iterable<Habitacion> findAllByEstadoOrderByCantidadhuespedDescTarifaDesc(EstadoHabitacion estadoHabitacion);

}
