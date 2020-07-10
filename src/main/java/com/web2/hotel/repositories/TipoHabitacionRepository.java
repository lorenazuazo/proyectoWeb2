package com.web2.hotel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web2.hotel.entities.TipoHabitacion;

@Repository
public interface TipoHabitacionRepository extends CrudRepository<TipoHabitacion, Long>{

}
