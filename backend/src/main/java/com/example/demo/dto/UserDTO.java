package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.model.Role;
import com.example.demo.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	private String token;

	@NotNull(message = "Role cannot be null")
	private Role role;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email must be valid")
	private String email;
	
	@NotBlank(message = "First name cannot be blank")
	private String firstName;
	
	@NotBlank(message = "Last name cannot be blank")
	private String lastName;
	
	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.role = user.getAuthority().getName();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
	}
	
	public UserDTO(User user, String token) {
		this(user);
		this.token = token;
	}

}
