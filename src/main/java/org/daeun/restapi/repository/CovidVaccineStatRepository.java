package org.daeun.restapi.repository;

import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatVO, String>{

//	@Query("SELECT new org.daeun.restapi.vo.CovidVaccineStatVO(c.baseDate, c.sido) FROM covidPractice c WHERE c.baseDate = :baseDate")
	List<CovidVaccineStatVO> findByBaseDateAndSido(String baseDate, String sido);
	List<CovidVaccineStatVO> findByBaseDateBetweenAndSido(String startDate, String endDate, String sido);
	List<CovidVaccineStatVO> findByBaseDate(String startDate);
}
