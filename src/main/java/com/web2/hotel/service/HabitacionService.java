package com.web2.hotel.service;


import java.util.Set;

import com.web2.hotel.entities.Habitacion;
import com.web2.hotel.entities.Reservas;

public interface HabitacionService {
	
	public Iterable<Habitacion> getAllHabitacion();
	
	public Set<Habitacion>getHabitacionesDisponibles(Reservas reserva)throws Exception;
	
	public Reservas getTarifaReserva(Reservas reserva)throws Exception;
	
	public Habitacion getHabitacionById(long id)throws Exception;
	
	public Habitacion createHabitacion(Habitacion habitacion)throws Exception;
	
	public Habitacion updateHabitacion(Habitacion habitacion)throws Exception;
	
	public void deleteHabitacion(long id)throws Exception;

}
