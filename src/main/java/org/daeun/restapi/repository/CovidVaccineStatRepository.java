package org.daeun.restapi.repository;

import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatVO, String>{

	List<CovidVaccineStatVO> findByBaseDateOrSido(String baseDate, String sido);
}
