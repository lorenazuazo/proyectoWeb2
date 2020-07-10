package com.web2.hotel.service;

import com.web2.hotel.entities.CaracteristicasHabitacion;

public interface CaracteristicasHabitacionService {
	
	public Iterable<CaracteristicasHabitacion> getAllCaractHabitacion();
	
	public CaracteristicasHabitacion createCaracteristica(CaracteristicasHabitacion fromCaract) throws Exception;
	
	public CaracteristicasHabitacion getCaracteristicaById(long id)throws Exception;
	
	public CaracteristicasHabitacion updateCaracteristica(CaracteristicasHabitacion caracteristica)throws Exception;
	
	public void deleteCaracteristica(long id) throws Exception;

}
