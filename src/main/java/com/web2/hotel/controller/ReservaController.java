package com.web2.hotel.controller;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.service.HabitacionService;
import com.web2.hotel.service.ReservaService;
import com.web2.hotel.service.UserService;


@Controller
public class ReservaController {
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	HabitacionService habitacionService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/consultar-disponibilidad")
	public String getReserva(Model model) {
		model.addAttribute("reservaForm", new Reservas());
		return "consultardisponibilidad";
	}
	
	@PostMapping("/disponibilidad")
	public String getDisponibilidad(@Valid @ModelAttribute("reservaForm")Reservas reserva, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("reservaForm", reserva);
		}else {
			try {
				reserva.setHabitacion(habitacionService.getHabitacionesDisponibles(reserva));
				//busco habitaciones disponibles, tarifa total, cantidad de noches
				Reservas reservaAux=habitacionService.getTarifaReserva(reserva);
				String username=userService.getUsuarioLogueado();
				Optional<Usuario> usuarioLogueado=userService.getUserByUsername(username);
				boolean isAdmin=false;
				if(usuarioLogueado.isPresent()) {
					isAdmin=userService.isAdmin(usuarioLogueado);
				}
							
				if(!username.equals("anonymousUser")&&(!isAdmin)) {
					reservaAux.setUsername(username);
					reservaAux.setApellido(usuarioLogueado.get().getApellido());
					reservaAux.setNombre(usuarioLogueado.get().getNombre());
					reservaAux.setDni(usuarioLogueado.get().getDni());
				}
				
				model.addAttribute("reservaForm", reservaAux);
				model.addAttribute("newUser", new Usuario()); 
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("reservaForm", reserva);
			}
		}
		return "confirmahabitacion";
	}
	
	@PostMapping("/confirmareserva")
	public String setReserva(@Valid @ModelAttribute("reservaForm")Reservas reserva, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("reservaForm", reserva);
		}else {
			try {
				reservaService.registrarReserva(reserva);
				model.addAttribute("formMessage", "La reserva se realizo con exito");
				model.addAttribute("exito", "true");
			} catch (Exception e) {
				model.addAttribute("reservaForm", reserva);
				model.addAttribute("formMessage", e.getMessage());
				model.addAttribute("exito", "false");
			}
		}
		
		return "muestrareserva";
	}

}
