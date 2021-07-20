package org.daeun.db.repository;

import org.daeun.db.dao.CovidVaccineStatDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatDAO, String>{

//	@Query("SELECT new org.daeun.restapi.vo.CovidVaccineStatVO(c.baseDate, c.sido) FROM covidPractice c WHERE c.baseDate = :baseDate")
	List<CovidVaccineStatDAO> findByBaseDateAndSido(String baseDate, String sido);
	List<CovidVaccineStatDAO> findAllByBaseDateBetweenAndSidoIn(String startDate, String endDate, List<String> sido);
//	List<CovidVaccineStatVO> findByBaseDate(String startDate);
//	@Query(value = "SELECT e FROM covidPractice e WHERE e.sido IN (:sidos)")
//	List<CovidVaccineStatVO> findAllByBaseDateAndSidoIn(String baseDate, List<String> sido);

}
