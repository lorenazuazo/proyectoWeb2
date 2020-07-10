package com.web2.hotel.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.Newsletters;
import com.web2.hotel.repositories.NewslattersRepository;

@Service
public class NewslettersServiceImpl implements NewsletterService{
	
	@Autowired
	NewslattersRepository newsletterRepo;

	@Override
	public Iterable<Newsletters> getAll() {
		return newsletterRepo.findAll();	
	}
	
	private boolean checkEmailDisponible(Newsletters news) throws Exception{
		Optional<Newsletters> email= newsletterRepo.findByCorreo(news.getCorreo());
		if(email.isPresent()) {
			throw new Exception("Email ya registrado");
		}
		return true;
	}


	@Override
	public void registratEmail(Newsletters email) throws Exception {
		if(checkEmailDisponible(email)) {
			email=newsletterRepo.save(email);
		}
	}

}
