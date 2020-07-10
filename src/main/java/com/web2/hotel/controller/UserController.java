package com.web2.hotel.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.web2.hotel.DTO.CambiarPassword;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.repositories.RoleRepository;
import com.web2.hotel.repositories.UserRepository;
import com.web2.hotel.service.ReservaService;
import com.web2.hotel.service.UserService;



@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	ReservaService reservaService;
	
	@GetMapping("/login")
	public String login (Model model) {
		return "login-form";
	}

	@GetMapping("/registro")
	public String mostratFormRegistro(Model model) {
		model.addAttribute("nuevoUsuario", new Usuario());
		return "crear-cuenta";
	}
	
	
	//este end point es para registrar un usuario antes de que reserve-desde form que le confirma las habitaciones
	@PostMapping("/registrar-usuario")
	public ResponseEntity<String> setNuevoUsuario(@Valid @RequestBody Usuario userform, Errors errors) {
		/*recibe los datos y los mapea en el objeto form de tipo dto Usuario*/
		try {
			//si hay errorer lo mete en un string y desde ahi se manejan las excepciones
	        if (errors.hasErrors()) {
	            String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);
	        }
	        //Usuario nuevoUsuario=userService.mapUsuarioDto(userform);
			userService.createUser(userform);/*por aca va si no hay errores*/
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());/*esto anda al jquery los errores y los maneja el script*/
		}
		return ResponseEntity.ok("success");
	}
	
	
	@PostMapping("/crear-cuenta")
	public String setCrearCuenta(@Valid @ModelAttribute("nuevoUsuario")Usuario user, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("nuevoUsuario", user);
		}else {
			try {
				userService.createUser(user);
				model.addAttribute("nuevoUsuario", new Usuario());
				model.addAttribute("formOkMessage","Registrado con exito");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("nuevoUsuario", user);
			}
		}
		return "crear-cuenta";
	}
	
	@GetMapping("/mis-datos/{username}")
	public String getDatos(Model model,@PathVariable(name="username")String username){
		Optional<Usuario> user=userService.getUserByUsername(username);
		model.addAttribute("userLogin",userService.getUserByUsername(username));
		//model.addAttribute("roles",roleRepo.findAll());
		model.addAttribute("passwordForm",new CambiarPassword(user.get().getId()));
		return "modif-datos";
	}

	@PostMapping("/editar-mis-datos")
	public String setDatos(Model model,@Valid @ModelAttribute("userLogin")Usuario user, BindingResult result) {
		if(result.hasErrors()) {
			model.addAttribute("userLogin", user);
			model.addAttribute("passwordForm",new CambiarPassword(user.getId()));
			
		}else {
			try {
				userService.updateDatosOcultos(user);
				userService.updateUser(user);
				model.addAttribute("formOkMessage","Actualizado con exito");
				model.addAttribute("passwordForm",new CambiarPassword(user.getId()));
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userLogin", user);
				model.addAttribute("passwordForm",new CambiarPassword(user.getId()));
				
			}
			
		}
		return "modif-datos";
	}
	
	@PostMapping("/cambiar-mi-clave")
	public ResponseEntity<String> postEditUseChangePassword(@Valid @RequestBody CambiarPassword form, Errors errors) {
		/*recibe los datos y los mapea en el objeto form de tipo dto cambiarPasword*/
		try {
			//si hay errorer lo mete en un string y desde ahi se manejan las excepciones
	        if (errors.hasErrors()) {
	            String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);
	        }
	        
			userService.changePassword(form);/*por aca va si no hay errores*/
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());/*esto anda al jquery los errores y los maneja el script*/
		}
		return ResponseEntity.ok("success");
	}
	
	
	@GetMapping("/mis-datos/cancel")
	public String cancelarEditUser(Model model) {
		return "redirect:/home";
	}
	
	
	@GetMapping("/mis-reservas/{username}")
	public String getReservas(Model model,@PathVariable(name="username")String username){
		try {
			Optional<Usuario> user=userService.getUserByUsername(username);
			String dni=user.get().getDni();
			Set<Reservas>reservas=reservaService.getMisReservas(dni);
			model.addAttribute("reservaList",reservas);
		} catch (Exception e) {
			model.addAttribute("formErrorMessage", e.getMessage());
		}
		
		return "mis-reservas";
	}

}
