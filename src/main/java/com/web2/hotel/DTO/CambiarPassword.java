package com.web2.hotel.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPassword {
	@NotNull
	private Long id;

	@NotBlank(message="Nueva contraseña no puede estar vacio")
	private String newPassword;

	@NotBlank(message="Confirme contraseña no puede estar vacio")
	private String confirmaPassword;

	public CambiarPassword(Long id) {
		this.id = id;
		}
}


