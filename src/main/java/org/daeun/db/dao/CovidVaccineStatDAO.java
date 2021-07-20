package org.daeun.db.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "covidPractice")
public class CovidVaccineStatDAO {
	
	@Id
	private String id;
	
	private String baseDate;
	private String sido;
	private int firstCnt;
	private int secondCnt;
	private int totalFirstCnt;
	private int totalSecondCnt;
	private int accumulatedFirstCnt;
	private int accumulatedSecondCnt;


}
