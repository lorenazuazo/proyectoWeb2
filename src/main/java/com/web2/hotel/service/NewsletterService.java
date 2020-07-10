package com.web2.hotel.service;

import com.web2.hotel.entities.Newsletters;

public interface NewsletterService {
	
	public Iterable<Newsletters> getAll();
	
	public void registratEmail(Newsletters email) throws Exception;

}
