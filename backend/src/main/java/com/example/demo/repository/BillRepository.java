package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

	@Query("select b from Bill b where "
			+ "b.account.user.id = :userId and "
			+ "(b.type = 0 and :rsd = true or b.type > 0 and :rsd = false) and "
			+ "lower(b.status) like lower(concat('%', :search, '%')) "
			+ "order by b.startDate desc")
	public Page<Bill> findAll(long userId, boolean rsd, Pageable pageable, String search);
	
}
