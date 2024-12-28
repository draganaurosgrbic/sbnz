package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Authority;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserMapper {

	private final AuthorityRepository authorityRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public User map(UserDTO userDTO) {
		User user = new User();
		Set<Authority> authorities = new HashSet<>();
		authorities.add(this.authorityRepository.findByName(Role.SLUZBENIK));
		user.setAuthorities(authorities);
		user.setPassword(UUID.randomUUID().toString());
		this.setModel(user, userDTO);
		return user;
	}
	
	@Transactional(readOnly = true)
	public User map(long id, UserDTO userDTO) {
		User user = this.userRepository.findById(id).get();
		this.setModel(user, userDTO);
		return user;
	}
	
	private void setModel(User user, UserDTO userDTO) {
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
	}
	
}
