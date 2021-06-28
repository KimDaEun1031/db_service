package org.daeun.RestAPI_DBsave.Controller;

import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

import org.daeun.RestAPI_DBsave.Controller.Repository.Covid_Vaccine_StatRepository;
import org.daeun.RestAPI_DBsave.Controller.VO.Covid_Vaccine_StatVO;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@RestController
public class RestDBController {
	
	@Autowired
	private Covid_Vaccine_StatRepository repository;
	
	@GetMapping("/covid_vaccine_stat_DB")
	public String callAPI() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		String jsonInString = "";
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);
			
			int day_1 = 10;
			for(int j=0; j<19; j++) {
				
				for(int i=0; i<18; i++) {
					
					String day = Integer.toString(day_1);
					
					String url = "http://localhost:9090/covid_vaccine_stat?month=06&day="+day;
					
					ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
					result.put("statusCode", resultMap.getStatusCodeValue());
		            result.put("header", resultMap.getHeaders());
		            result.put("body", resultMap.getBody());
		           
		            
		            Gson gson = new Gson();
		            
		            jsonInString = gson.toJson(resultMap.getBody());
		            JsonParser gsonParser =  new JsonParser();   
		            JsonElement element = gsonParser.parse(jsonInString);
		            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");
		            JsonObject rowList = (JsonObject) row.get(i);
		            
		            Covid_Vaccine_StatVO covidVO = gson.fromJson(rowList, Covid_Vaccine_StatVO.class);
		            
//		            repository.insert(Arrays.asList(covidVO));

		            
		            jsonInString = repository.findBysido("전국");
//		            System.out.println(day);
				}
	            day_1++;
			}
			
						
			
            
            
            
//            String jsonStr = gson.toJson(resultMap.getBody()); //map -> json
//            jsonInString = gson.toJson(resultMap.getBody()); //map -> json
           
            
//            JsonParser gsonParser =  new JsonParser();   
//            JsonElement element = gsonParser.parse(jsonStr);
//            JsonObject stnTimeSearch = (JsonObject) element.getAsJsonObject().get("SearchSTNTimeTableByIDService");
//            JsonArray row = (JsonArray) stnTimeSearch.get("row");        
//            JsonObject rowList = (JsonObject) row.get(0);
//            
//            STNTimeVO stnVO = gson.fromJson(rowList, STNTimeVO.class);

            
        	//삽입
//    		Repository.insert(Arrays.asList(stnVO));

    		
    		//조회
//    		stnTime = Repository.findBySUBWAYSNAME("약수");

 
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
 
		
		return jsonInString;
	}
	
	
	

}
