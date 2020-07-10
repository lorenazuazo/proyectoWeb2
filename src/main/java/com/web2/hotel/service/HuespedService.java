package com.web2.hotel.service;

import com.web2.hotel.entities.Huesped;

public interface HuespedService {
	public Iterable<Huesped> getAllHuesped();
	
	public Huesped createHuesped(Huesped huesped) throws Exception;
	
	public Huesped createHuespedByDni(Huesped huesped)throws Exception;
	
	public Huesped getHuespedById(long id)throws Exception;
	
	public Huesped updateHuesped(Huesped fromHuesped)throws Exception;
	
	public void deleteHuesped(long id) throws Exception;

}
