package com.web2.hotel.service;

import com.web2.hotel.entities.TipoHabitacion;

public interface TipoHabitacionService {
	
	public Iterable<TipoHabitacion>getAllTiposHabitacion();
	
	public TipoHabitacion createTipoHabitacion(TipoHabitacion tipoHabitacion) throws Exception;
	
	public TipoHabitacion getTipoHabitacionById(long id)throws Exception;
	
	public TipoHabitacion updateTipoHabitacion(TipoHabitacion tipoHabitacion)throws Exception;
	
	public void deleteTipoHabitacion(long id) throws Exception;

}
