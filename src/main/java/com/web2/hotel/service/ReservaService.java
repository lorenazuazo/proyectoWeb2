package com.web2.hotel.service;


import java.util.Set;

import com.web2.hotel.entities.Reservas;

public interface ReservaService {
	
	public Iterable<Reservas> getAllReservas();
	
	public Reservas getReservaById(long id)throws Exception;
	
	public Set<Reservas> getMisReservas(String dni) throws Exception;
	
	public Iterable<Reservas> getAllReservasConfirmadas();
	
	public void registrarReserva(Reservas reserva) throws Exception;
	
	public Reservas updateReserva(Reservas reserva)throws Exception;
	
	public void deleteReserva(long id) throws Exception;

}
