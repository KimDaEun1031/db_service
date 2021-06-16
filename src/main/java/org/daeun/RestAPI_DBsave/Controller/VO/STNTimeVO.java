package org.daeun.RestAPI_DBsave.Controller.VO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "stntime")
public class STNTimeVO {
	
	@Id
	private String id;
	
	private String LINE_NUM;
	private String FR_CODE;
	private String station_cd;
	private String STATION_NM;
	private String TRAIN_NO;
	private String ARRIVETIME;
	private String LEFTTIME;
	private String ORIGINSTATION;
	private String DESTSTATION;
	private String SUBWAYSNAME;
	private String WEEK_TAG;
	private String INOUT_TAG;
	private String FL_FLAG;
	private String DESTSTATION2;
	private String EXPRESS_YN;
	private String BRANCH_LINE;
	
}
