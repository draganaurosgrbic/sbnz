package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Authority;
import com.example.demo.model.Role;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	public Authority findByName(Role name);
	
}
