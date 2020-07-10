package com.web2.hotel.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="authority")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column
	@NotBlank
	private String authority;
	
}
