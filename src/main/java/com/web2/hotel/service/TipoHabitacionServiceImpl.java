package com.web2.hotel.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.TipoHabitacion;
import com.web2.hotel.repositories.TipoHabitacionRepository;

@Service
public class TipoHabitacionServiceImpl implements TipoHabitacionService{

	@Autowired
	TipoHabitacionRepository tipoHabRepo;
	@Override
	public Iterable<TipoHabitacion> getAllTiposHabitacion() {
		return tipoHabRepo.findAll();
	}
	
	private boolean checkTipoHabitacionDisponible(TipoHabitacion tipoHabitacion) throws Exception{
		Optional<TipoHabitacion> tipoHabitFound= tipoHabRepo.findById(tipoHabitacion.getId_tipo());
		if(tipoHabitFound.isPresent()) {
			throw new Exception("Tipo de Habitacion ya registrada");
		}
		return true;
	}
	@Override
	public TipoHabitacion createTipoHabitacion(TipoHabitacion tipoHabitacion) throws Exception {
		TipoHabitacion tipoHabit = null;
		if(checkTipoHabitacionDisponible(tipoHabitacion)) {
			tipoHabit = tipoHabRepo.save(tipoHabitacion);
		}
		return tipoHabit;
	}
	@Override
	public TipoHabitacion getTipoHabitacionById(long id) throws Exception {
		TipoHabitacion tipoHabit=tipoHabRepo.findById(id).orElseThrow(()->new Exception("No existe Tipo de Habitacion"));
		return tipoHabit;
	}
	@Override
	public TipoHabitacion updateTipoHabitacion(TipoHabitacion fromTipoHabitacion) throws Exception {
		Optional<TipoHabitacion> caract= tipoHabRepo.findById(fromTipoHabitacion.getId_tipo());
		if (!caract.isPresent()) {
			   throw new Exception("No existe cTipo de Habitacion");
			}

		TipoHabitacion toTipoHabitacion = caract.get();
		mapTipoHabitacion(fromTipoHabitacion, toTipoHabitacion);
		return tipoHabRepo.save(toTipoHabitacion);
	}
	/*mapeo el tipo de habitacion desde form a to*/
	protected void mapTipoHabitacion(TipoHabitacion from,TipoHabitacion toTipoHabitacion) {
		toTipoHabitacion.setClase(from.getClase()); 
		toTipoHabitacion.setDescripcion(from.getDescripcion());
		toTipoHabitacion.setDimension(from.getDimension());
		toTipoHabitacion.setCamas(from.getCamas());
		toTipoHabitacion.setEstado(from.getEstado());
	}
	
	@Override
	public void deleteTipoHabitacion(long id) throws Exception {
		TipoHabitacion tipoHabit= tipoHabRepo.findById(id).orElseThrow(()-> new Exception("No existe Tipo de Habitacion"));
		tipoHabRepo.delete(tipoHabit);
		
	}

}
