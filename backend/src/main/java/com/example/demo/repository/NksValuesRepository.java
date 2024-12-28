package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BillType;
import com.example.demo.model.NksValues;

@Repository
public interface NksValuesRepository extends JpaRepository<NksValues, Long> {

	public NksValues findByLevelAndType(long level, BillType type);
	
}
