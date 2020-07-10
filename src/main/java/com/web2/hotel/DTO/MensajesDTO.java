package com.web2.hotel.DTO;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MensajesDTO {
	
	public String nombre;
	public String apellido;
	public String correo;
	public String mensaje;
	public String estado;

}
