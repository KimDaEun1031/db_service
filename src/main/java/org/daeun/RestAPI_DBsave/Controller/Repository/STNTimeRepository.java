package org.daeun.RestAPI_DBsave.Controller.Repository;

import org.daeun.RestAPI_DBsave.Controller.VO.STNTimeVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface STNTimeRepository extends MongoRepository<STNTimeVO, String>{
	
	String findBySUBWAYSNAME(String SUBWAYSNAME);
}
