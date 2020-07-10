package com.web2.hotel.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.web2.hotel.entities.Mensajes;
import com.web2.hotel.repositories.MensajeRepository;
import com.web2.hotel.service.MensajeService;

@Controller
public class MensajeController {
	
	@Autowired
	MensajeRepository mensajeRepo;
	
	@Autowired
	MensajeService mensajeService;
	
	@GetMapping("/mensaje")
	public String getMensaje(Model model) {
		model.addAttribute("mensajeForm", new Mensajes());/*creando un mensaje que se guarda en la variable mensaje*/
		return "mensaje";
	}
	
	@PostMapping("/guardar-mensaje")
	public String setMensaje(@Valid @ModelAttribute("mensajeForm")Mensajes mensaje, BindingResult result, Model model) throws Exception{
		if(result.hasErrors()) {
			model.addAttribute("mensajeForm", mensaje);
		}else {
			try {
				mensajeService.regisitrarMensaje(mensaje);
				model.addAttribute("mensajeForm", new Mensajes());
				model.addAttribute("formOKMessage", "El mensaje se envio correctamente");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("mensajeForm", mensaje);
			}
		}
		
		return "mensaje";
	}
	

}
