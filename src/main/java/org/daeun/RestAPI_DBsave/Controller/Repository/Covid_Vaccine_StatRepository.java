package org.daeun.RestAPI_DBsave.Controller.Repository;

import org.daeun.RestAPI_DBsave.Controller.VO.CovidVaccineStatVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Covid_Vaccine_StatRepository extends MongoRepository<CovidVaccineStatVO, String>{
	
	String findBysido(String sido);
}
