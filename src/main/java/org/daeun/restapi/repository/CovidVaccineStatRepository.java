package org.daeun.restapi.repository;

import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatVO, String>{
	
	String findBysido(String sido);
}
