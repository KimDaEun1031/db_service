package org.daeun.RestAPI_DBsave.Controller.VO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "covid")
public class Covid_Vaccine_StatVO {
	
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
