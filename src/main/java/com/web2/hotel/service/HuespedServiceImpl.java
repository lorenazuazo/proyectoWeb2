package com.web2.hotel.service;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web2.hotel.entities.Huesped;
import com.web2.hotel.entities.Reservas;
import com.web2.hotel.repositories.HuespedRepository;
import com.web2.hotel.repositories.ReservaRepository;

@Service
public class HuespedServiceImpl implements HuespedService{

	@Autowired
	HuespedRepository huespedRepo;
	
	@Autowired
	ReservaRepository reservarepo;
	
	@Autowired
	ReservaService reservaService;
	
	@Override
	public Iterable<Huesped> getAllHuesped() {
		return huespedRepo.findAll() ;
	}
	
	/*chequear que el huesped este disponible*/
	private boolean checkHuespedDisponible(Huesped huesped) throws Exception{
		Optional<Huesped> huespedFound= huespedRepo.findById(huesped.getId());
		if(huespedFound.isPresent()) {
			throw new Exception("Huesped ya creado");
		}
		return true;
	}
	
	public Huesped createHuespedByDni(Huesped huesped) throws Exception {
		Optional<Huesped> hues=huespedRepo.findByDni(huesped.getDni());
		Reservas reserva=reservarepo.findById(huesped.getResAux()).get();
		Set<Huesped> listaHuespeded=reserva.getHuespedGrupo();
		Optional<Huesped> huespedCargado=null;
		boolean bandera=false;
		
		if(!hues.isPresent()) {
			huesped=huespedRepo.save(huesped);
			huespedCargado=huespedRepo.findByDni(huesped.getDni());
			listaHuespeded.add(huespedCargado.get());
			reserva.setHuespedGrupo(listaHuespeded);
		}else {
			for(Huesped h:listaHuespeded) {
				if(h.getDni().equals(huesped.getDni())) {
					bandera=true;
					throw new Exception("El huesped ya eta registrado en la reserva");
				}
			}
			
			if(bandera==false) {
				listaHuespeded.add(hues.get());
				reserva.setHuespedGrupo(listaHuespeded);
			}
		}
		
		if(bandera==false) {
			reservaService.updateReserva(reserva);
		}
			
		return huesped;
	}

	@Override
	public Huesped createHuesped(Huesped huesped) throws Exception {
		Huesped huesp = null;
		if(checkHuespedDisponible(huesped)) {
			huesp = huespedRepo.save(huesped);
		}
		return huesp;
	}

	@Override
	public Huesped getHuespedById(long id) throws Exception {
		Huesped huesped=huespedRepo.findById(id).orElseThrow(()->new Exception("No existe Huesped"));
		return huesped;
	}

	@Override
	public Huesped updateHuesped(Huesped fromHuesped) throws Exception {
		Optional<Huesped> huesp= huespedRepo.findById(fromHuesped.getId());
		if (!huesp.isPresent()) {
			   throw new Exception("No existe el huesped");
			}

		Huesped toHuesped = huesp.get();
		mapHuesped(fromHuesped, toHuesped);
		return huespedRepo.save(toHuesped);
	}
	/*mapeo el usuario desde formUser a toUser*/
	protected void mapHuesped(Huesped from,Huesped toHuesped) {
		toHuesped.setNombre(from.getNombre());          
		toHuesped.setApellido(from.getApellido());
		toHuesped.setDni(from.getDni());
		toHuesped.setReserva(from.getReserva());
	}

	@Override
	public void deleteHuesped(long id) throws Exception {
		Huesped huesped=huespedRepo.findById(id).orElseThrow(()-> new Exception("No existe el huesped"));
		huespedRepo.delete(huesped);
		
	}

}
