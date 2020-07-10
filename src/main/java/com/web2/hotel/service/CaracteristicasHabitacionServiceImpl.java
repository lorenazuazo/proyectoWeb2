package com.web2.hotel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.CaracteristicasHabitacion;
import com.web2.hotel.repositories.CaracteristicasHabitacionRepository;

@Service
public class CaracteristicasHabitacionServiceImpl implements CaracteristicasHabitacionService{

	@Autowired
	CaracteristicasHabitacionRepository caracteristicasHabRepo;
	@Override
	public Iterable<CaracteristicasHabitacion> getAllCaractHabitacion() {
		return caracteristicasHabRepo.findAll();
	}
	
	private boolean checkCaracteristicaDisponible(CaracteristicasHabitacion carateristica) throws Exception{
		Optional<CaracteristicasHabitacion> caratFound= caracteristicasHabRepo.findById(carateristica.getId());
		if(caratFound.isPresent()) {
			throw new Exception("Caracteristica ya registrada");
		}
		return true;
	}
	@Override
	public CaracteristicasHabitacion createCaracteristica(CaracteristicasHabitacion fromCaract) throws Exception {
		CaracteristicasHabitacion caracteristica = null;
		if(checkCaracteristicaDisponible(fromCaract)) {
			caracteristica = caracteristicasHabRepo.save(fromCaract);
		}
		return caracteristica;
	}
	
	@Override
	public CaracteristicasHabitacion getCaracteristicaById(long id) throws Exception {
		CaracteristicasHabitacion caract=caracteristicasHabRepo.findById(id).orElseThrow(()->new Exception("No existe Caracteristica"));
		return caract;
	}
	@Override
	public CaracteristicasHabitacion updateCaracteristica(CaracteristicasHabitacion fromCaracteristica) throws Exception {
		Optional<CaracteristicasHabitacion> caract= caracteristicasHabRepo.findById(fromCaracteristica.getId());
		if (!caract.isPresent()) {
			   throw new Exception("No existe caracteristica");
			}

		CaracteristicasHabitacion toCaract = caract.get();
		mapCaracteristica(fromCaracteristica, toCaract);
		return caracteristicasHabRepo.save(toCaract);
	}
	/*mapeo el Caract desde formCaract a toCaract*/
	protected void mapCaracteristica(CaracteristicasHabitacion from,CaracteristicasHabitacion toCaract) {
		toCaract.setDetalle(from.getDetalle()); 
		toCaract.setEstado(from.getEstado());
	}
	
	@Override
	public void deleteCaracteristica(long id) throws Exception {
		CaracteristicasHabitacion caract= caracteristicasHabRepo.findById(id).orElseThrow(()-> new Exception("No existe caracteristica"));
		 caracteristicasHabRepo.delete(caract);
	}
}
