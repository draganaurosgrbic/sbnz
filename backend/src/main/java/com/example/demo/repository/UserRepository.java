package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("select u from User u where "
			+ "lower(u.email) like lower(concat('%', :search, '%')) or "
			+ "lower(u.firstName) like lower(concat('%', :search, '%')) or "
			+ "lower(u.lastName) like lower(concat('%', :search, '%')) "
			+ "order by u.email")
	public Page<User> findAll(Pageable pageable, String search);

	public User findByAccountId(long accountId);
	public User findByEmail(String email);
	public User findByActivationLink(String code);

}
