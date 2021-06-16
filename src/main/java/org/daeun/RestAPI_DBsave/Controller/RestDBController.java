package org.daeun.RestAPI_DBsave.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.daeun.RestAPI_DBsave.Controller.Repository.STNTimeRepository;
import org.daeun.RestAPI_DBsave.Controller.VO.STNTimeVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class RestDBController {
	
	@Autowired
	private STNTimeRepository Repository;
	
	@GetMapping("/STNtimeDB")
	public String callAPI() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		String stnTime = "";
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(5000);
			factory.setReadTimeout(5000);
			RestTemplate restTemplate = new RestTemplate(factory);
			
			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);
			
			String url = "http://localhost:9090/STNtime";
			
			 UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
			
			ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
			result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인  
            
            ObjectMapper mapper = new ObjectMapper();
            
            String jsonInString = mapper.writeValueAsString(resultMap.getBody());
        	
            
        	JSONParser jsonParse = new JSONParser();
        	
        	JSONObject jsonObj = (JSONObject) jsonParse.parse(jsonInString);
        	JSONObject stnTimeSearch = (JSONObject) jsonObj.get("SearchSTNTimeTableByIDService");
        	JSONArray row = (JSONArray) stnTimeSearch.get("row");

//        	for(int i=0; i < row.size(); i++) { 
//        		JSONObject list = (JSONObject) row.get(0);
//        		
//        		String LINE_NUM = (String) list.get("LINE_NUM");
	//    		String FR_CODE = (String) list.get("FR_CODE");
	//    		String STATION_CD = (String) list.get("STATION_CD");
	//    		String STATION_NM = (String) list.get("STATION_NM");
	//    		String TRAIN_NO = (String) list.get("TRAIN_NO");
	//    		String ARRIVETIME = (String) list.get("ARRIVETIME");
	//    		String LEFTTIME = (String) list.get("LEFTTIME");
	//    		String ORIGINSTATION = (String) list.get("ORIGINSTATION");
	//    		String DESTSTATION = (String) list.get("DESTSTATION");
	//    		String SUBWAYSNAME = (String) list.get("SUBWAYSNAME");
	//    		String WEEK_TAG = (String) list.get("WEEK_TAG");
	//    		String INOUT_TAG = (String) list.get("INOUT_TAG");
	//    		String FL_FLAG = (String) list.get("FL_FLAG");
	//    		String DESTSTATION2 = (String) list.get("DESTSTATION2");
	//    		String EXPRESS_YN = (String) list.get("EXPRESS_YN");
	//    		String BRANCH_LINE = (String) list.get("BRANCH_LINE");
////        		
//        		System.out.println("LINE_NUM : " +LINE_NUM);
//        	}

      	
        	JSONObject list = (JSONObject) row.get(1);
    		
    		String LINE_NUM = (String) list.get("LINE_NUM");
    		String FR_CODE = (String) list.get("FR_CODE");
    		String STATION_CD = (String) list.get("STATION_CD");
    		String STATION_NM = (String) list.get("STATION_NM");
    		String TRAIN_NO = (String) list.get("TRAIN_NO");
    		String ARRIVETIME = (String) list.get("ARRIVETIME");
    		String LEFTTIME = (String) list.get("LEFTTIME");
    		String ORIGINSTATION = (String) list.get("ORIGINSTATION");
    		String DESTSTATION = (String) list.get("DESTSTATION");
    		String SUBWAYSNAME = (String) list.get("SUBWAYSNAME");
    		String WEEK_TAG = (String) list.get("WEEK_TAG");
    		String INOUT_TAG = (String) list.get("INOUT_TAG");
    		String FL_FLAG = (String) list.get("FL_FLAG");
    		String DESTSTATION2 = (String) list.get("DESTSTATION2");
    		String EXPRESS_YN = (String) list.get("EXPRESS_YN");
    		String BRANCH_LINE = (String) list.get("BRANCH_LINE");
    		
    		
    		STNTimeVO stnTimeVO = new STNTimeVO(null, LINE_NUM, FR_CODE, STATION_CD, STATION_NM, TRAIN_NO, ARRIVETIME, LEFTTIME, ORIGINSTATION, DESTSTATION, SUBWAYSNAME, WEEK_TAG, INOUT_TAG, FL_FLAG, DESTSTATION2, EXPRESS_YN, BRANCH_LINE);
    		
    		
    		//Repository.insert(Arrays.asList(stnTimeVO));
    		String stnSubwayName = Repository.findBySUBWAYSNAME("약수");
    		 
    		stnTime = stnSubwayName;


           
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());
	 
		} catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }
 
		
		return stnTime;
	}
	
	
	

}
