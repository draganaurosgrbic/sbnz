package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeDTO {
	
	@NotBlank(message = "Old password cannot be blank")
	private String oldPassword;
	
	@NotBlank(message = "New password cannot be blank")
	private String newPassword;

}
