package com.web2.hotel.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web2.hotel.entities.Newsletters;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.repositories.TipoHabitacionRepository;
import com.web2.hotel.service.NewsletterService;



@Controller
@RequestMapping("/")
public class AdministratorController {
	
	@GetMapping("/home")
	public String getIndex(Model model) {
		model.addAttribute("email", new Newsletters());/*creando un newsletter para que registre el mail de newsletter*/
		model.addAttribute("reservaForm", new Reservas());
		return "index";
	}
	
	@GetMapping("/fotos")
	public String mostrarGaleria() {
		return "galeria";
	}
	
	@GetMapping("/tarifas")
	public String mostrarTarifas() {
		return "tarifas";
	}

	@Autowired
	TipoHabitacionRepository tipohabrepo;
	
	@GetMapping("/habitaciones")
	public String getHabitacion(Model model){
		model.addAttribute("hab",tipohabrepo.findAll());/**para consultar las habitaciones**/
		return "habitaciones";
	}
	
	@Autowired
	NewsletterService newletterService;
	
	
	/*para guerdar el correo de newsletter*/
	@PostMapping("/save-correo")
	public String setCorreo(@ModelAttribute Newsletters email, Model model) throws Exception {
		try {
			newletterService.registratEmail(email);
			
		} catch (Exception e) {
			model.addAttribute("formErrorMessage",e.getMessage());
		}
		
		return getIndex(model);
	}
	
}
