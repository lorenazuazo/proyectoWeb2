package com.web2.hotel.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.web2.hotel.entities.Newsletters;

public interface NewslattersRepository extends CrudRepository<Newsletters, Long>{

	Optional<Newsletters> findByCorreo(String correo);

}
