package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	private final UserService userService;
		
	@PostMapping(value = "/login")
	public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO){
		return ResponseEntity.ok(this.userService.login(loginDTO));
	}

	@GetMapping(value = "/activate/{code}", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> activate(@PathVariable String code){
		String html;
		try {
			this.userService.activate(code);
			html = "<div style='height: 100%; width: 100%; "
					+ "display: flex; flex-direction: column; "
					+ "justify-content: center; align-items: center; "
					+ "font-weight: bold; font-size: 24px;'>"
					+ "<h1>ACCOUNT ACTIVATED</h1><br>"
					+ "Click on the following link to sign in:<br>"
					+ "<a href='" + Constants.FRONTEND + "'>Sign In</a>"
					+ "</div>";
		}
		catch(Exception e) {
			html = "<div style='height: 100%; width: 100%; "
					+ "display: flex; flex-direction: column; "
					+ "justify-content: center; align-items: center;"
					+ "font-weight: bold; font-size: 24px;'>"
					+ "<h1>ERROR</h1><br>"
					+ "Check your email again.";
		}
		return ResponseEntity.ok(html);
	}
			
}
