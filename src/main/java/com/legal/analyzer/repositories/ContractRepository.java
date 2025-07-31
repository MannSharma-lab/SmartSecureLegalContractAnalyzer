package com.legal.analyzer.repositories;
import com.legal.analyzer.models.User;
import com.legal.analyzer.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

	List<Contract>findByTitleContainingIgnoreCase(String kryword);
	
	//new method to get contracts by user
	List<Contract> findByUser(User user);
}