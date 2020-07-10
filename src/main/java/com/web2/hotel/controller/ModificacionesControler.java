package com.web2.hotel.controller;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.web2.hotel.DTO.CambiarPassword;
import com.web2.hotel.DTO.MensajesDTO;
import com.web2.hotel.entities.Authority;
import com.web2.hotel.entities.CaracteristicasHabitacion;
import com.web2.hotel.entities.Habitacion;
import com.web2.hotel.entities.Huesped;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.TipoHabitacion;
import com.web2.hotel.entities.Usuario;
import com.web2.hotel.repositories.RoleRepository;
import com.web2.hotel.repositories.UserRepository;
import com.web2.hotel.service.CaracteristicasHabitacionService;
import com.web2.hotel.service.HabitacionService;
import com.web2.hotel.service.HuespedService;
import com.web2.hotel.service.MensajeService;
import com.web2.hotel.service.ReservaService;
import com.web2.hotel.service.RolesService;
import com.web2.hotel.service.TipoHabitacionService;
import com.web2.hotel.service.UserService;

@Controller
@RequestMapping("/modificaciones")
public class ModificacionesControler {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/")
	public String getModificaciones() {
		return "admin-form";
	}
	
	@GetMapping("/usuario")
	public String getUsuarios(Model model) {
		model.addAttribute("userForm", new Usuario());/*para guardar las modificaciones*/
		model.addAttribute("userList", userService.getAlluser());/*Lista de usuarios en BBDD*/
		model.addAttribute("roles", roleRepo.findAll());
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-user";
	}
	
	@PostMapping("/crearUser")
	public String setModificarDatos(@Valid @ModelAttribute("userForm")Usuario user, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
		}else {
			try {
				userService.createUser(user);
				model.addAttribute("userForm", new Usuario());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAlluser());
				model.addAttribute("roles", roleRepo.findAll());
			}
		}
		
		model.addAttribute("userList", userService.getAlluser());
		model.addAttribute("roles", roleRepo.findAll());
		return "admin-form-user";
	}
	
	@GetMapping("/editUser/{id}")
	public String getEditUserForm(Model model,@PathVariable(name="id")Long id) throws Exception {
		Usuario user= userService.getUserById(id);
		model.addAttribute("userList",userService.getAlluser());
		model.addAttribute("roles",roleRepo.findAll());
		model.addAttribute("userForm",user);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		model.addAttribute("passwordForm",new CambiarPassword(id));
		return "admin-form-user";
	}
	
	@PostMapping("/editUser")
	public String setUserEdit(@Valid @ModelAttribute("userForm")Usuario user, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode","true");/*activo el modo edicion*/
			model.addAttribute("passwordForm",new CambiarPassword(user.getId()));
		}else {
			try {
				userService.updateUser(user);
				model.addAttribute("userForm", new Usuario());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAlluser());
				model.addAttribute("roles", roleRepo.findAll());
				model.addAttribute("editMode","true");/*activo el modo edicion*/
				model.addAttribute("passwordForm",new CambiarPassword(user.getId()));

			}
		}
		
		model.addAttribute("userList", userService.getAlluser());
		model.addAttribute("roles", roleRepo.findAll());
		return "admin-form-user";		
	}
	
	
	@PostMapping("/cambiar-clave")
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
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name="id") Long id) {
		try {
			userService.deleteUser(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar el usuario");
		}
		return getUsuarios(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/usuario/cancel")
	public String cancelEditUser(Model model) {
		return "redirect:/modificaciones/usuario";
	}
	
	/*modificaciones de habitaciones*/
	@Autowired
	HabitacionService habitacionService;
	@Autowired
	TipoHabitacionService tipoHabService;
	@Autowired
	CaracteristicasHabitacionService caractHabitService;
	
	
	@GetMapping("/habitaciones")
	public String getHabitaciones(Model model) {
		model.addAttribute("habForm", new Habitacion());/*para guardar las modificaciones*/
		model.addAttribute("habitacionList", habitacionService.getAllHabitacion());/*Lista de habit en BBDD*/
		model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
		model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-habitacion";
	}
	
	@PostMapping("/crearHabitacion")
	public String setModificarHabitacion(@Valid @ModelAttribute("habForm")Habitacion habitacion, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("habForm", habitacion);
			model.addAttribute("formTab", "active");
		}else {
			try {
				habitacionService.createHabitacion(habitacion);
				model.addAttribute("habForm", new Habitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("habForm", habitacion);
				model.addAttribute("formTab", "active");
				model.addAttribute("habitacionList", habitacionService.getAllHabitacion());
				model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
				model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
			}
			
			model.addAttribute("habitacionList",habitacionService.getAllHabitacion());
			model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
			model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
		}

		return "admin-form-habitacion";
	}
	
	@GetMapping("/editHabitacion/{id}")
	public String getStringEditHabitcion(Model model,@PathVariable(name="id")Long id)throws Exception {
		Habitacion hab= habitacionService.getHabitacionById(id);
		model.addAttribute("habitacionList", habitacionService.getAllHabitacion());
		model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
		model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
		model.addAttribute("habForm",hab);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-habitacion";
	}
	
	@PostMapping("/editHabitacion")
	public String seteditHabitacion(@Valid @ModelAttribute("habForm")Habitacion habitacion, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("habForm", habitacion);
			model.addAttribute("formTab", "active");
		}else {
			try {
				habitacionService.updateHabitacion(habitacion);
				model.addAttribute("habForm", new Habitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("habForm", habitacion);
				model.addAttribute("formTab", "active");
				model.addAttribute("habitacionList", habitacionService.getAllHabitacion());
				model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
				model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
			}
			
			model.addAttribute("habitacionList",habitacionService.getAllHabitacion());
			model.addAttribute("tipos",tipoHabService.getAllTiposHabitacion());
			model.addAttribute("caractHabitacion",caractHabitService.getAllCaractHabitacion());
		}

		return "admin-form-habitacion";
	}
	
	@GetMapping("/deleteHabitacion/{id_habitacion}")
	public String deleteHabitacion(Model model, @PathVariable(name="id_habitacion") Long id) {
		try {
			habitacionService.deleteHabitacion(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar la habitacion");
		}
		return getHabitaciones(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/habitaciones/cancel")
	public String cancelEditHabitacion(Model model) {
		return "redirect:/modificaciones/habitaciones";
	}
	
	/*modificaciones de las caracteristicas de las habitaciones*/
	@GetMapping("/caracteristicas")
	public String getCaracteristicas(Model model) {
		model.addAttribute("caractForm", new CaracteristicasHabitacion());/*para guardar las modificaciones*/
		model.addAttribute("caractList", caractHabitService.getAllCaractHabitacion());/*Lista de caract en BBDD*/
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-caracthab";
	}
	
	@PostMapping("/crearCaracteristica")
	public String setModificarCaracteristicas(@Valid @ModelAttribute("caractForm")CaracteristicasHabitacion caract, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("caractForm", caract);
			model.addAttribute("formTab", "active");
		}else {
			try {
				caractHabitService.createCaracteristica(caract);
				model.addAttribute("caractForm", new CaracteristicasHabitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("caractForm", caract);
				model.addAttribute("formTab", "active");
				model.addAttribute("caractList", caractHabitService.getAllCaractHabitacion());
			}
		}
		model.addAttribute("caractList", caractHabitService.getAllCaractHabitacion());
		return "admin-form-caracthab";
	}
	
	@GetMapping("/editar-Caracteristica/{id}")
	public String getEditCaracteristicas(Model model,@PathVariable(name="id")Long id) throws Exception {
		CaracteristicasHabitacion caract= caractHabitService.getCaracteristicaById(id);
		model.addAttribute("caractList",caractHabitService.getAllCaractHabitacion());
		model.addAttribute("caractForm",caract);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-caracthab";
	}
	
	@PostMapping("/editar-Caracteristica")
	public String setCaracteristicaEdit(@Valid @ModelAttribute("caractForm")CaracteristicasHabitacion caract, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("caractForm", caract);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode","true");/*activo el modo edicion*/
		}else {
			try {
				caractHabitService.updateCaracteristica(caract);
				model.addAttribute("caractForm", new CaracteristicasHabitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("caractForm", caract);
				model.addAttribute("formTab", "active");
				model.addAttribute("caractList", caractHabitService.getAllCaractHabitacion());
				model.addAttribute("editMode","true");/*activo el modo edicion*/

			}
		}
		
		model.addAttribute("caractList", caractHabitService.getAllCaractHabitacion());
		return "admin-form-caracthab";		
	}
	
	@GetMapping("/eliminar-caracteristica/{id}")
	public String deleteCaracteristica(Model model, @PathVariable(name="id") Long id) {
		try {
			caractHabitService.deleteCaracteristica(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar la caracteristica");
		}
		return getCaracteristicas(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/caracteristica/cancel")
	public String cancelEditCaracteristica(Model model) {
		return "redirect:/modificaciones/caracteristicas";
	}
	
	/*modificaciones de los Tipos de habitaciones*/
	@GetMapping("/tiposHabitacion")
	public String getTiposHabitacion(Model model) {
		model.addAttribute("tipohabForm", new TipoHabitacion());/*para guardar las modificaciones*/
		model.addAttribute("tipohabList", tipoHabService.getAllTiposHabitacion());/*Lista de habitaciones en BBDD*/
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-tipohab";
	}
	
	@PostMapping("/crearTiposHabitacion")
	public String setModificarTiposHabitacion(@Valid @ModelAttribute("tipohabForm")TipoHabitacion tipoHabitacion, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("tipohabForm", tipoHabitacion);
			model.addAttribute("formTab", "active");
		}else {
			try {
				tipoHabService.createTipoHabitacion(tipoHabitacion);
				model.addAttribute("tipohabForm", new TipoHabitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("tipohabForm", tipoHabitacion);
				model.addAttribute("formTab", "active");
				model.addAttribute("tipohabList", tipoHabService.getAllTiposHabitacion());
			}
		}
		model.addAttribute("tipohabList", tipoHabService.getAllTiposHabitacion());
		return "admin-form-tipohab";
	}
	
	@GetMapping("/editar-TipoHabitacion/{id_tipo}")
	public String getEditTipoHabitacion(Model model,@PathVariable(name="id_tipo")Long id) throws Exception {
		TipoHabitacion tipohab= tipoHabService.getTipoHabitacionById(id);
		model.addAttribute("tipohabList",tipoHabService.getAllTiposHabitacion());
		model.addAttribute("tipohabForm",tipohab);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-tipohab";
	}
	
	@PostMapping("/editar-TipoHabitacion")
	public String setTipoHabitacionEdit(@Valid @ModelAttribute("tipohaForm")TipoHabitacion tipoHabitacion, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("tipohabForm", tipoHabitacion);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode","true");/*activo el modo edicion*/
		}else {
			try {
				tipoHabService.updateTipoHabitacion(tipoHabitacion);
				model.addAttribute("tipohabForm", new TipoHabitacion());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("tipohabForm", tipoHabitacion);
				model.addAttribute("formTab", "active");
				model.addAttribute("tipohabList", tipoHabService.getAllTiposHabitacion());
				model.addAttribute("editMode","true");/*activo el modo edicion*/

			}
		}
		
		model.addAttribute("tipohabList", tipoHabService.getAllTiposHabitacion());
		return "admin-form-tipohab";		
	}
	
	@GetMapping("/eliminar-tipoHabitacion/{id_tipo}")
	public String deleteTipoHabitacion(Model model, @PathVariable(name="id_tipo") Long id) {
		try {
			tipoHabService.deleteTipoHabitacion(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar lel tipo de habitacion");
		}
		return getTiposHabitacion(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/tipoHabitacion/cancel")
	public String cancelEditTipoHabitacion(Model model) {
		return "redirect:/modificaciones/tiposHabitacion";
	}
	
	/*modificaciones de las reservas*/
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	UserRepository usuarioRepo;
	
	@GetMapping("/reservas")
	public String getReservas(Model model) {
		model.addAttribute("reservaForm", new Reservas());/*para guardar las modificaciones*/
		model.addAttribute("reservaList", reservaService.getAllReservas());/*Lista de reservas en BBDD*/
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		ArrayList<Huesped>huespedList=new ArrayList<>();
		model.addAttribute("huespedList", huespedList);
		return "admin-form-reserva";
	}
	
	
	@PostMapping("/editar-Reserva")
	public String seteditReserva(@Valid @ModelAttribute("reservaForm")Reservas reserva, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("reservaForm", reserva);
			model.addAttribute("usuariosList", usuarioRepo.findAll());
			model.addAttribute("formTab", "active");
		}else {
			try {
				reservaService.updateReserva(reserva);
				model.addAttribute("reservaForm", new Reservas());
				model.addAttribute("usuariosList", usuarioRepo.findAll());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("reservaForm", reserva);
				model.addAttribute("formTab", "active");
				model.addAttribute("reservaList", reservaService.getAllReservas());
			}
			
			model.addAttribute("reservaList",reservaService.getAllReservas());
		}

		return "admin-form-reserva";
	}
	@PostMapping("/editar-Reserva-Huesped")
	public String agregarHuesped(@Valid @ModelAttribute("nuevoHuesped")Huesped huesped,BindingResult result,Model model) throws Exception {
		try {
				huespedService.createHuespedByDni(huesped);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
			}
		
		
		return getEditReserva(model, huesped.getResAux());
	}
	
	@GetMapping("/editReserva/{idReserva}")
	public String getEditReserva(Model model,@PathVariable(name="idReserva")Long id)throws Exception {
		model.addAttribute("nuevoHuesped", new Huesped());
		Reservas reserva= reservaService.getReservaById(id);
		model.addAttribute("reservaList", reservaService.getAllReservas());
		model.addAttribute("reservaForm",reserva);
		Iterable<Usuario> usuarios=usuarioRepo.findAll();
		model.addAttribute("usuariosList", usuarios);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-reserva";
	}
	
	
	@GetMapping("/deleteReserva/{idReserva}")
	public String deleteReserva(Model model, @PathVariable(name="idReserva") Long id) {
		try {
			reservaService.deleteReserva(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar la reserva");
		}
		return getReservas(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/reserva/cancel")
	public String cancelEditReserva(Model model) {
		return "redirect:/modificaciones/reservas";
	}

	
	
	/*modificaciones en roles*/
	@Autowired
	RolesService roleService;
	
	@GetMapping("/roles")
	public String getRoles(Model model) {
		model.addAttribute("rolesForm", new Authority());/*para guardar las modificaciones*/
		model.addAttribute("rolesList", roleService.getAllRoles());/*Lista de roles en BBDD*/
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-roles";
	}
	
	@PostMapping("/crear-Roles")
	public String setModificarDatos(@Valid @ModelAttribute("rolesForm")Authority rol, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("rolesForm", rol);
			model.addAttribute("formTab", "active");
		}else {
			try {
				roleService.createRole(rol);
				model.addAttribute("rolesForm", new Authority());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("rolesForm", rol);
				model.addAttribute("formTab", "active");
				model.addAttribute("rolesList", roleService.getAllRoles());
			}
		}
		
		model.addAttribute("rolesList", roleService.getAllRoles());
		return "admin-form-roles";
	}
	
	@GetMapping("/editar-Roles/{id}")
	public String getEditRoles(Model model,@PathVariable(name="id")Long id) throws Exception {
		Authority rol= roleService.getRoleById(id);
		model.addAttribute("rolesList",roleService.getAllRoles());
		model.addAttribute("rolesForm",rol);
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-roles";
	}
	
	@PostMapping("/editar-Roles")
	public String setRolesEdit(@Valid @ModelAttribute("rolesForm")Authority rol, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("rolesForm", rol);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode","true");/*activo el modo edicion*/
		}else {
			try {
				roleService.updateRole(rol);
				model.addAttribute("rolesForm", new Authority());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("rolesForm", rol);
				model.addAttribute("formTab", "active");
				model.addAttribute("rolesList", roleService.getAllRoles());
				model.addAttribute("editMode","true");/*activo el modo edicion*/

			}
		}
		
		model.addAttribute("rolesList", roleService.getAllRoles());
		return "admin-form-roles";		
	}
	
	@GetMapping("/eliminar-Rol/{id}")
	public String deleteRol(Model model, @PathVariable(name="id") Long id) {
		try {
			roleService.deleteRole(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar el rol");
		}
		return getRoles(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/rol/cancel")
	public String cancelEditRole(Model model) {
		return "redirect:/modificaciones/roles";
	}
	
	/*modoficaciones de Huesped*/
	@Autowired
	HuespedService huespedService;
	
	@GetMapping("/huesped")
	public String getHuespedes(Model model) {
		model.addAttribute("huespedForm", new Huesped());/*para guardar las modificaciones*/
		model.addAttribute("huespedList", huespedService.getAllHuesped());/*Lista de huesped en BBDD*/
		model.addAttribute("listTab","active");/*esto dice que ventana esta activa cuando entra*/
		return "admin-form-huesped";
	}
	
	@PostMapping("/crear-Huesped")
	public String setModificarHuesped(@Valid @ModelAttribute("huespedForm")Huesped huesped, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("huespedForm", huesped);
			model.addAttribute("formTab", "active");
		}else {
			try {
				huespedService.createHuesped(huesped);
				model.addAttribute("huespedForm", new Huesped());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("huespedForm", huesped);
				model.addAttribute("formTab", "active");
				model.addAttribute("huespedList", huespedService.getAllHuesped());
			}
		}
		
		model.addAttribute("huespedList", huespedService.getAllHuesped());
		return "admin-form-huesped";
	}
	
	@GetMapping("/editar-Huesped/{id}")
	public String getEditHuesped(Model model,@PathVariable(name="id")Long id) throws Exception {
		Huesped huesped= huespedService.getHuespedById(id);
		model.addAttribute("huespedList",huespedService.getAllHuesped());
		model.addAttribute("huespedForm",huesped);
		model.addAttribute("reservaForm", reservaService.getAllReservasConfirmadas());
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");/*activo el modo edicion*/
		return "admin-form-huesped";
	}
	
	@PostMapping("/editar-Huesped")
	public String setHuespedEdit(@Valid @ModelAttribute("huespedForm")Huesped huesped, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("huespedForm", huesped);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode","true");/*activo el modo edicion*/
		}else {
			try {
				huespedService.updateHuesped(huesped);
				model.addAttribute("huespedForm", new Huesped());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("huespedForm", huesped);
				model.addAttribute("formTab", "active");
				model.addAttribute("huespedList", huespedService.getAllHuesped());
				model.addAttribute("editMode","true");/*activo el modo edicion*/
			}
		}
		
		model.addAttribute("huespedList", huespedService.getAllHuesped());
		return "admin-form-huesped";		
	}
	
	@GetMapping("/eliminar-Huesped/{id}")
	public String deleteHuesped(Model model, @PathVariable(name="id") Long id) {
		try {
			huespedService.deleteHuesped(id);
		} catch (Exception e) {
			model.addAttribute("deleteError","No se pudo eliminar el huesped");
		}
		return getHuespedes(model); //es lo mismo que redirect per mantengo el mensaje de error
	}
	
	@GetMapping("/huesped/cancel")
	public String cancelEditHuesped(Model model) {
		return "redirect:/modificaciones/huesped";
	}
	
	@Autowired
	MensajeService mensajeService;
	
	/*para visualizar los mensajes*/
	@GetMapping("/ver-mensajes")
	public String getMensajes(Model model) {
		try {
			Iterable<MensajesDTO>mensajes=mensajeService.getMensajes();
			model.addAttribute("mensajeList", mensajes);
			model.addAttribute("listTab","active");
		} catch (Exception e) {
			model.addAttribute("deleteError",e.getMessage());
		}
			
		return "admin-form-mensajes";
	}

}


