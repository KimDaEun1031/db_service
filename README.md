# Spring Boot 기반 Microservice Architecture 구현
![db](https://user-images.githubusercontent.com/73865700/126262509-477eb926-59f1-443d-83d8-7dc6827d2018.png)
> NOTE

> + Covid19 예방접종 통계 Data를 MSA 기반으로 구축해본다.
> + Service는 Spring boot Project로 구현된다.
> + JPA repository로 DB(MongoDB)에 접근한다.
> + MSA는 서비스 별로 형성관리를 분리함으로 이번 Study에서 분리 개발했다.
> + Collector Service는 DB Service에 데이터를 Push하거나 Search Service에 받은 요청에 따라 값을 return 한다.
> + https://github.com/KimDaEun1031/collector_service
> + Search Service는 Collector Service에 값을 요청에 데이터를 return 받아 화면에 송출한다.
> + https://github.com/KimDaEun1031/search_service

## DB Service Description
#### Project directory tree
```
.
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ src/main/java/org/daeun/db
│       │                   ├─ DbApplication.java
│       │                   ├─ controller
│       │                   │     ├─ CovidApiBatchInsertController.java
│       │                   │     ├─ CovidApiRowInsertController.java
│       │                   │     ├─ CovidApiSaveController.java
│       │                   │     └─ CovidApiSearchController.java
│       │                   ├─ repository
│       │                   │     └─ CovidVaccineStatRepository.java
│       │                   └─ dao
│       │                         └─ CovidVaccineStatDAO.java
│       └─ resources
│           └─ application.properties
│  
└─ target
     ├─classes
     ├─generated-sources ...
```
DB Service는 Collector Service에서 Push한 데이터를 MongoDB에 Insert한다.  
Insert 시 DB 안에 데이터가 들어가있으면 Insert 하지 않고 들어가 있지 않으면 Insert 한다.
> + 참고 사이트들을 블로그에 올려놓았다.  
> https://relaxed-it-study.tistory.com/category/JAVA/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8

## 1. Dependency
MongoDB와 Spring Web, Gson, Lombok를 추가한다.
Gson을 제외하고는 Spring boot를 설치할 때 나오는 dependency에서 추가할 수 있다.
```
	<parent>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-starter-parent</artifactId>
	      <version>2.5.1</version>
	      <relativePath/> <!-- lookup parent from repository -->
	</parent>
  
	<groupId>org.daeun</groupId>
	<artifactId>db</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>db</name>
	<description>Demo project for Spring Boot</description>
	
        <properties>
		<java.version>1.8</java.version>
	</properties>
  
	<dependencies>
	      <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-mongodb</artifactId>
	      </dependency>
	      <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	      </dependency>
	      <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web-services</artifactId>
	      </dependency>
	      <dependency>
		  <groupId>org.apache.httpcomponents</groupId>
		  <artifactId>httpclient</artifactId>
		  <version>4.5.13</version>
	      </dependency>
	      <dependency>
		  <groupId>com.google.code.gson</groupId>
		  <artifactId>gson</artifactId>
		  <version>2.8.5</version>
	      </dependency>
	      <dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<optional>true</optional>
	      </dependency>
	      <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	      </dependency>
	</dependencies>

       <build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

## 2. Configuration
application.yml에서는 application의 port와 db connection을 진행한다.
```
spring.data.mongodb.uri= mongodb://127.0.0.1:27017/admin

server.port=9091
```

## 3. REST API Server

#### REST API
|METHOD|PATH + PARAMETER|DESCRIPTION|Controller|
|----|----|----|----|
|GET|/batchInsertCovidVaccineStat|전체 데이터 배치 삽입|CovidApiBatchInsertController|
|GET|/insertCovidVaccineStat|지정한 날짜와 지역 데이터 삽입|CovidApiRowInsertController|
|GET|/searchCovidVaccineStat|지정한 날짜와 지역 데이터 검색|CovidApiSearchController|
|GET|/searchPeriodDataCovidVaccineStat|지정한 기간와 지역 데이터 검색|CovidApiSearchController|
|POST|/saveCovidVaccineStat|Collector Service에서 Push한 데이터 삽입|CovidApiSaveController|

#### Collection(collection name = covidPractice) description
JPA를 이용해 DB에 접근한다.  
+ DB name = admin   

|Field|Type|Key|Des|
|----|----|----|----|
|id|String|PRI|@Id 사용|
|baseDate|String||통계 기준일자|
|sido|String||지역명칭|
|firstCnt|int||당일 통계(1차 접종)|
|secondCnt|int||당일통계(2차 접종)|
|totalFirstCnt|int||전체 누적 통계(1차 접종)|
|totalSecondCnt|int||전체 누적 통계(2차 접종)|
|accumulatedFirstCnt|int||전일까지의 누적 통계(1차 접종)|
|accumulatedSecondCnt|int||전일까지의 누적 통계(2차 접종)|  

#### JPA(Java Persistence API)란?
+ 자바 ORM 기술에 대한 표준 명세로, JAVA에서 제공하는 API이다.(스프링 제공 X) 
+ 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스다. 인터페이스이기 때문에 실제로 작동하지 않는다. 
+ 스프링의 PSA(Portable Service Abstraction)에 의해서 표준 인터페이스를 정해두었는데, 그 중 ORM을 사용하기 위해 만든 인터페이스라고 할 수 있다.
+ 기존 EJB에서 제공되던 엔티티 빈을 대체하는 기술이다.
+ 참고 사이트 : https://velog.io/@adam2/JPA%EB%8A%94-%EB%8F%84%EB%8D%B0%EC%B2%B4-%EB%AD%98%EA%B9%8C-orm-%EC%98%81%EC%86%8D%EC%84%B1-hibernate-spring-data-jpa

#### PSA(Portable Service Abstraction)란?
+ Spring은 Spring Triangle이라고 부르는 세가지 개념을 제공해준다.(loC, AOP, PSA)
+ Spring은 추상화 계층을 사용해서 어떤 기술을 내부에 숨기고 개발자에게 편의성을 제공해준다. -> Service Abstracion
+ Service Abstraction으로 제공되는 기술을 다른 기술 스택으로 간편하게 바꿀 수 있는 확장성을 갖고 있는 것이 **PSA**이다.
+ Portable - 이동이 쉬운
+ 참고 사이트 : https://atoz-develop.tistory.com/entry/Spring-%EC%8A%A4%ED%94%84%EB%A7%81-PSA

#### ORM(Object-Relation Mapping)란?
+ 객체와 RDB를 Mapping한다. 객체를 통해 간접적으로 DB 데이터를 다룬다.
+ SQL 쿼리가 아닌 메서드로 데이터를 조작하며 객체 간의 관계를 바탕으로 SQL을 자동으로 생성한다.
+ Mybatis는 ORM이 아닌 SQL Query를 Mapping하여 실행하는 구조이다.

#### Spring-Data-JPA
JPA를 쉽게 사용하기 위해 스프링에서 제공하고 있는 프레임워크이다.

#### JPA를 사용해야하는 이유
1. 생산성 => 반복적인 SQL 작업과 CRUD 작업을 개발자가 직접하지 않아도 된다.
2. 성능 => Caching을 지원하며 SQL이 여러번 수행되는 것을 최적화한다.
3. 표준 => 표준을 알아두면 다른 구현 기술로 쉽게 변경할 수 있다.

#### 많이 쓰이는 JPA Annotation
+ Entity
![entity](https://user-images.githubusercontent.com/73865700/126289965-869cbc71-9788-4d9c-847a-6740eec53e5b.png)

+ Join
![join](https://user-images.githubusercontent.com/73865700/126290047-11b793aa-d8ce-4e8d-a8f1-aaad7afe2d54.png)

+ Override
![재정의](https://user-images.githubusercontent.com/73865700/126290162-011c86ce-9a01-41ee-8133-ac6e11755709.png)

+ 참고 사이트 : https://dzone.com/articles/all-jpa-annotations-mapping-annotations

## 3-1 JPA DAO
DB에 접근하기 위해 DAO를 설정한다.  
#### CovidVaccineStatDAO.java
```
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
```

## 3-2 Repository
DAO가 DB에 접근하기 위해 이용할 Repository Interface를 생성한다.
#### CovidVaccineStatRepository.java
```
@Repository
@Transactional
public interface CovidVaccineStatRepository extends MongoRepository<CovidVaccineStatDAO, String>{

	List<CovidVaccineStatDAO> findByBaseDateAndSido(String baseDate, String sido);
	
	List<CovidVaccineStatDAO> findAllByBaseDateBetweenAndSidoIn(String startDate, String endDate, List<String> sido);
	
}
```
+ 참고 사이트 : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference

## 4. Controller - batch Insert
RestTemplate의 exchange 메소드를 이용해 Today(EndDate) 데이터부터 StartDate 데이터까지 List에 넣어서 한 번에 DB에 Insert 한다.

+ batch Insert Code
```
 @GetMapping("/batchInsertCovidVaccineStat")
    public void batchInsertCovidVaccineStat() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            LocalDate date = LocalDate.now().minusDays(1);

            String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d", date.getMonthValue(), date.getDayOfMonth());
            log.info("url = {}",url);

            ResponseEntity<Map> resultMap = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, Map.class);

            String jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = jsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");

            List<CovidVaccineStatVO> batchList = new ArrayList<>();

            for (int j = 0; j < row.size(); j++) {
                JsonObject rowList = (JsonObject) row.get(j);

                CovidVaccineStatVO covidVO = gson.fromJson(rowList, CovidVaccineStatVO.class);
                batchList.add(covidVO);

            }

          covidVaccineStatRepository.insert(batchList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info(e.toString());

        } catch (Exception e) {
            log.info(e.toString());
        }
    }
```
**HOST**  
localhost:9091

**PATH(GET)**  
/batchInsertCovidVaccineStat

## 4-1. Controller - Row Insert
RestTemplate의 exchange 메소드를 이용해 지정된 지역과 Today 데이터를 DB에 Insert한다.
```
@GetMapping("/insertCovidVaccineStat")
    public void insertCovidVaccineStat() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser gsonParser = new JsonParser();

            LocalDate date = LocalDate.now();
            String sido = "전국";

            String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d&sido=%s",date.getMonthValue(),date.getDayOfMonth(), URLEncoder.encode(sido, "UTF-8"));
            log.info("url = {}",url);

            ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            String jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = gsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");
            for (int i=0; i<row.size(); i++) {
                JsonObject rowList = (JsonObject) row.get(i);

                CovidVaccineStatDAO covidDAO = gson.fromJson(rowList, CovidVaccineStatDAO.class);

		covidVaccineStatRepository.insert(covidDAO);
                log.info("result = {}", covidDAO);
	    }


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info(e.toString());

        } catch (Exception e) {
            log.info(e.toString());
        }

    }
```
**HOST**  
localhost:9091

**PATH(GET)**  
/insertCovidVaccineStat

## 4-3. Controller - Search
epository를 쓰기 위해 @Autowired를 사용해 Bean을 자동으로 매핑해준다.
```
@Autowired
CovidVaccineStatRepository covidVaccineStatRepository;
```

설정한 날짜와 지역에 맞는 값을 Search해 return 한다.
```
@GetMapping("/searchCovidVaccineStat")
public String searchCovidVaccineStat(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateTime,
                                     @RequestParam(required = false, defaultValue = "전국") String sido) {

        String search = "";
        try {
            String baseDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
            log.info(baseDate);

            search  = String.valueOf(covidVaccineStatRepository.findByBaseDateAndSido(baseDate, sido));
            log.info("success");

        }
        catch (Exception e) {
            log.info("error");
            log.info(e.toString());
        }

        return search;
}
```
**HOST**  
localhost:9091   

**PATH(GET)**  
/searchCovidVaccineStat

**PARAMETERS**  
1. dateTime  
	- in : query  
	- description : 날짜
	- type : LocalDate
	- default : todayDate  

2. sido
	- in : query  
	- description : 지역명칭
	- type : string
	- default : 전국  

**EXAMPLE**
1. Basic - localhost:9091/searchCovidVaccineStat
2. Parameter - localhost:9091/searchCovidVaccineStat?nowDate=20210405&sido=서울특별시

---
Search Service에서 요청한 StartDate, EndDate, sido를 검색해 지정한 기간과 지역을 return 한다.
```
 @GetMapping("/searchPeriodDataCovidVaccineStat")
    public String searchPeriodDataCovidVaccineStat(String startDate, String endDate, String sido) {

        log.info("startDate = {}", startDate);
        log.info("endDate = {}", endDate);
        log.info("sido = {}",sido);

        String jsonInString = "";
        try {

            List<String> sidoList = Arrays.asList(sido.split(","));
            log.info("sidoList = {}", sidoList);

            List<CovidVaccineStatDAO> list = new ArrayList<>();

            list = covidVaccineStatRepository.findAllByBaseDateBetweenAndSidoIn(startDate, endDate, sidoList);
            log.info("list = {}", list);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            String jsonList = gson.toJson(list);
            JsonElement element = jsonParser.parse(jsonList);
            jsonInString = String.valueOf(element);

            log.info("data = {} ", jsonInString);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.toString());

        } catch (Exception e) {
            log.error(e.toString());
        }

        return jsonInString;
    }
```
**HOST**  
localhost:9091   

**PATH(GET)**  
/searchPeriodDataCovidVaccineStat

**PARAMETERS**  
1. startDate  
	- in : query  
	- description : 지정한 시작 날짜
	- type : string  

2. endDate
	- in : query  
	- description : 지정한 끝 날짜
	- type : string

3. sido
	- in : query  
	- description : 지역명칭
	- type : string

## 4-4. Controller - Save
Collector Service에서 Push 한 데이터를 List로 받아 Insert 해준다.
단, DB 안에 데이터가 존재한다면 데이터는 Insert 하지 않는다.
```
@PostMapping (value = "/saveCovidVaccineStat", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveCovidVaccineStat(@RequestBody List<CovidVaccineStatDAO> data) {

        log.info("data = {}",data);
        for (CovidVaccineStatDAO vo: data) {
            List<CovidVaccineStatDAO> covidDaoList = covidVaccineStatRepository.findByBaseDateAndSido(vo.getBaseDate(), vo.getSido());

            if(covidDaoList.isEmpty()) {
                covidVaccineStatRepository.insert(vo);
                log.info("insert data success!");
            }
        }

        log.info("It already in the data!");
    }
```
**HOST**  
localhost:9091   

**PATH(POST)**  
/saveCovidVaccineStat

**PARAMETERS**  
1. data  
	- in : query  
	- description : Collector Service에서 Push 한 데이터
	- type : List  
