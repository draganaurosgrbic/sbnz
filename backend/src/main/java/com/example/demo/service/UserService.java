  
package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.PasswordChangeDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenUtils;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authManager;
	private final TokenUtils tokenUtils;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		return this.userRepository.findByEmail(username);	
	}

	public Page<User> findAll(Pageable pageable, String search) {
		return this.userRepository.findAll(pageable, search);
	}

	@Transactional(readOnly = false)
	public User save(User user) {
		if (user.getId() == null) {
			this.emailService.sendEmail(
				user.getEmail(),
				EmailService.ACTIVATION_TITLE,
				String.format(EmailService.ACTIVATION_TEXT, 
						user.getFirstName(), user.getLastName(), 
						user.getActivationLink(), user.getPassword())
			);		
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		}
		return this.userRepository.save(user);
	}

	@Transactional(readOnly = false)
	public void delete(long id) {
		this.userRepository.deleteById(id);
	}
	
	public UserDTO login(LoginDTO loginDTO) {
		User user = (User) this.authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())).getPrincipal();
		return new UserDTO(user, this.tokenUtils.generateToken(loginDTO.getEmail()));
	}

	@Transactional(readOnly = false)
	public void activate(String code) {
		User user = this.userRepository.findByActivationLink(code);
		user.setEnabled(true);
		this.userRepository.save(user);
	}

	@Transactional(readOnly = false)
	public void changePassword(PasswordChangeDTO passwordChangeDTO) {
		User user = (User) this.authManager.
				authenticate(new UsernamePasswordAuthenticationToken(
						this.currentUser().getEmail(), passwordChangeDTO.getOldPassword())).getPrincipal();
		user.setPassword(this.passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
		this.save(user);
	}
	
	public User currentUser() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch(Exception e) {
			return null;
		}
	}

}