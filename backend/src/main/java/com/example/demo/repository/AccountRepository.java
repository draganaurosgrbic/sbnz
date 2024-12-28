package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a where "
			+ "lower(a.user.email) like lower(concat('%', :search, '%')) or "
			+ "lower(a.user.firstName) like lower(concat('%', :search, '%')) or "
			+ "lower(a.user.lastName) like lower(concat('%', :search, '%')) or "
			+ "lower(a.jmbg) like lower(concat('%', :search, '%')) or "
			+ "lower(a.address) like lower(concat('%', :search, '%')) or "
			+ "lower(a.city) like lower(concat('%', :search, '%')) or "
			+ "lower(a.zipCode) like lower(concat('%', :search, '%')) "
			+ "order by a.date desc")
	public Page<Account> findAll(Pageable pageable, String search);
	
	public Account findByUserId(long id);
		
}
