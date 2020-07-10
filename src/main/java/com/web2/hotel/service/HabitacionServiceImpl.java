package com.web2.hotel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.Habitacion;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.entities.Reservas.Estado;
import com.web2.hotel.entities.Habitacion.EstadoHabitacion;
import com.web2.hotel.repositories.HabitacionRepository;
import com.web2.hotel.repositories.ReservaRepository;
import com.web2.hotel.repositories.TipoHabitacionRepository;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class HabitacionServiceImpl implements HabitacionService{

	@Autowired
	HabitacionRepository habitacionRepo;
	
	@Autowired
	HabitacionService habitacionService;
	
	@Autowired
	ReservaRepository reservaRepo;
	
	@Autowired
	TipoHabitacionRepository tipoHabitacionRepo;

	@Override
	public Iterable<Habitacion> getAllHabitacion() {
		return habitacionRepo.findAll();
	}

	@Override
	public Habitacion getHabitacionById(long id) throws Exception {
		Optional<Habitacion> habitacion= habitacionRepo.findById(id);
		if(!habitacion.isPresent()) {
			throw new Exception("No existe habitacion con ese id");
		}
		return habitacion.get();
	}

	@Override
	public Habitacion createHabitacion(Habitacion habitacion) throws Exception {
		Habitacion hab= habitacionRepo.save(habitacion);
		return hab;
	}

	@Override
	public Habitacion updateHabitacion(Habitacion habitacionFrom) throws Exception {
		Optional<Habitacion> hab=habitacionRepo.findById(habitacionFrom.getId_habitacion());
		if(!hab.isPresent()) {
			throw new Exception("No existe habitacion");
		}
		
		Habitacion habitacionTo=hab.get();
		habMap(habitacionFrom,habitacionTo);
		return habitacionRepo.save(habitacionTo);
	}
	
	protected void habMap(Habitacion from,Habitacion habitacionTo) {
		habitacionTo.setNumerohabitacion(from.getNumerohabitacion());
		habitacionTo.setTarifa(from.getTarifa());
		habitacionTo.setDescripcion(from.getDescripcion());
		habitacionTo.setTipoHabitacion(from.getTipoHabitacion());
		habitacionTo.setCaractehabitacion(from.getCaractehabitacion());
		habitacionTo.setEstado(from.getEstado());
	}

	@Override
	public void deleteHabitacion(long id) throws Exception {
		Habitacion habit=habitacionRepo.findById(id)
				.orElseThrow(()->new Exception("No existe la habitacion"));
		habitacionRepo.delete(habit);
		
	}
	
	//aca calculo si coincide el numero de huesped con la cantidad de habitaciones pedidas
	//si son mas habitaciones las que corresponden las calculo en base a que como maximo
	//entran 4 por habitacion quad
	
	public int getCalcularHabitaciones(double canthuesped,double cantidadhabitaciones) {
		double a=(double)(canthuesped/cantidadhabitaciones);
		int parte_entera=(int)a;
		int habi=(int)cantidadhabitaciones;
		double parte_decimal=a-parte_entera;
		
		 while (parte_entera>4) {          
	            habi=habi+1;
	            parte_entera=parte_entera-4;
	            
	        } 
		 if(parte_decimal>0 && parte_entera>4) {
				habi=habi+1;
			}
		return habi;
	}

	@Override
	public Set<Habitacion> getHabitacionesDisponibles(Reservas reservaConsulta) throws Exception {
		ArrayList<Reservas> reservasConfirmadas=(ArrayList<Reservas>) reservaRepo.findAllByEstado(Estado.CONFIRMADA);
		ArrayList<Habitacion> habitaciones=(ArrayList<Habitacion>) habitacionRepo.findAllByEstadoOrderByCantidadhuespedDescTarifaDesc(EstadoHabitacion.VIGENTE);	
		Set<Habitacion>habitparaReserva=new HashSet<>();
		LocalDate fechaIn=reservaConsulta.getFechaEntrada();
		LocalDate fechaOut=reservaConsulta.getFechaSalida();
		
		
		for(Reservas reserva: reservasConfirmadas) {
			LocalDate fechaEntradaResevada=reserva.getFechaEntrada();
			LocalDate fechaSalidaReservada=reserva.getFechaSalida();
			
			//consulto si la fecha de entrada o salida que quiero reservar ya hay reservas Confirmadas
			//entonces elimino de la lista habitaciones las habitaciones ocupadas para esa fecha
			if((fechaEntradaResevada.isBefore(fechaIn) && fechaIn.isBefore(fechaSalidaReservada)) || 
					(fechaEntradaResevada.isBefore(fechaOut)&& fechaOut.isBefore(fechaSalidaReservada)) ||
					(fechaEntradaResevada.isEqual(fechaIn)) || (fechaSalidaReservada.isEqual(fechaOut))){
				//para cada habitacion de la reserva sacarla de la lista de habitaciones disponibles
				for(Habitacion h : reserva.getHabitacion()) {
					if(habitaciones.contains(h)){
						habitaciones.remove(h);
					}
				}
			}
		}
		if(habitaciones.isEmpty() ) {
			throw new Exception("No hay habitaciones disponibles para esas fechas");
		}
		
		//Para calcular el numero de habitacionesque necesitan
		int cantidadhabitaciones=reservaConsulta.getCantHabitaciones();
		int canthuesped=reservaConsulta.getCantAdultos()+reservaConsulta.getCantNinios();
		int habitacionesAReservar=getCalcularHabitaciones(canthuesped, cantidadhabitaciones);
		int personasPorHabitacion=(canthuesped/habitacionesAReservar);
		int cont=0;
		
		/*1-busco las cantidad de habitciones pedidas*/
		for (int i=habitaciones.size()-1;i>=0;i--) {    
			if(cont < habitacionesAReservar) {
				if(habitaciones.get(i).getCantidadhuesped()>=personasPorHabitacion) {
					habitparaReserva.add(habitaciones.get(i));
					habitaciones.remove(i);
					cont++;
				}				
			}	  
		}
		
		/*2-controlo si todos los huespedes tienen habitacion*/
		int huspedasignados=0;
		for(Habitacion h : habitparaReserva) {
			huspedasignados= huspedasignados + h.getCantidadhuesped();
		}
		
		
		int huespsinhabitacion=(int) (canthuesped-huspedasignados);
		/*int contador=huespsinhabitacion;*/
		/*3-si no encuentra la cantidad de habitaciones pedidas para cubrir todos los huespedes
		 * revisa cuantos huesped quedaron sin habitacion y empieza a buscar habitaciones disponibles
		 * que cubran los huespedes que no tienen habitacion 
		 */
		while(huespsinhabitacion > 0 && !habitaciones.isEmpty()) {
			boolean bandera=false;
			for(int a=huespsinhabitacion;a>=0;a--) {
				for (int i=habitaciones.size()-1;i>=0;i--) {
					if((habitaciones.get(i).getCantidadhuesped()>= a) && (bandera==false)) {
						habitparaReserva.add(habitaciones.get(i));
						huespsinhabitacion=huespsinhabitacion-habitaciones.get(i).getCantidadhuesped();
						habitaciones.remove(i);
						bandera=true;
					}
				}
			}
		}
		
		if(huespsinhabitacion > 0 ) {
			throw new Exception("No hay habitaciones disponibles para los huesped solicitados");
		}
		
		return habitparaReserva;
	}

	@Override
	public Reservas getTarifaReserva(Reservas reserva) throws Exception {	
		LocalDate fechaIn=reserva.getFechaEntrada();
		LocalDate fechaOut=reserva.getFechaSalida();

		long dias = DAYS.between(fechaIn, fechaOut);
		if(fechaIn.equals(fechaOut)) {
			dias=1;
		}
		int monto=0;
		int cantHabitaciones=0;
		for(Habitacion h:reserva.getHabitacion()) {
			monto=h.getTarifa()+ monto;	
			cantHabitaciones++;
		}
		
		reserva.setCantDias((int) dias);
		reserva.setTarifaReserva((int) (monto*dias));
		reserva.setCantHabitaciones(cantHabitaciones);
		
		return reserva;
	}

}
