package org.daeun.db.repository;

import org.daeun.db.vo.CovidVaccineStatVO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatVO, String>{

//	@Query("SELECT new org.daeun.restapi.vo.CovidVaccineStatVO(c.baseDate, c.sido) FROM covidPractice c WHERE c.baseDate = :baseDate")
	List<CovidVaccineStatVO> findByBaseDateAndSido(String baseDate, String sido);
	List<CovidVaccineStatVO> findAllByBaseDateBetweenAndSidoIn(String startDate, String endDate, List<String> sido);
//	List<CovidVaccineStatVO> findByBaseDate(String startDate);
//	@Query(value = "SELECT e FROM covidPractice e WHERE e.sido IN (:sidos)")
//	List<CovidVaccineStatVO> findAllByBaseDateAndSidoIn(String baseDate, List<String> sido);

}
