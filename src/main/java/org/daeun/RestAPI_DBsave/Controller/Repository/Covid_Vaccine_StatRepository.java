package org.daeun.RestAPI_DBsave.Controller.Repository;

import org.daeun.RestAPI_DBsave.Controller.VO.Covid_Vaccine_StatVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Covid_Vaccine_StatRepository extends MongoRepository<Covid_Vaccine_StatVO, String>{
	
	String findBysido(String sido);
}
