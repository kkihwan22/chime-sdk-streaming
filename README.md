# 탈잉 라이브 시스템
온라인 라이브 내재화 서버 프로젝트 저장소입니다.

## 사용 기술

* Java 11
* Spring Boot 2.5.3
* Maven
* FeignClient
* Amazon SQS
* Amazon ElasticCache

## 프로젝트 관련 문서

* [기획서](https://www.notion.so/talingme/1533bb0a8cc9404c803ea98818878e66)
* [API 명세서](https://live.dev.taling.me/swagger-ui/)

## 배포

### 브랜치 전략

~~~
ASIS : DEV, PROD
TOBE : DEV, QA(STAGE), PROD

- DEV : 개발자 테스트 환경
- QA (STAGE) : QA 환경
- PROD : 운영환경

1. Jira 티켓에 기반하여 feature 브랜치를 마스터 브랜치에서 checkout한다.
2. DEV 브랜치에 feature 브랜치를 머지하여 dev 환경에서 개발자 테스트를 진행한다.
3. QA를 위한 release 브랜치를 checkout 한 후 테스트할 feature들을 머지한다. 
4. 해당 release 브랜치의 검수가 완료되면 마스터로 머지한다. 

~~~

## 서버현황
https://www.notion.so/talingme/97f749ee19fe47d98754273a034286d2

### 배포
- Github Actions 탭에서 진행
  - 개발 서버
    - Workflows 중 'Deploy dev' 선택
  - QA 서버
    - Workflows 중 'Deploy qa' 선택
  - 운영 서버
    - Workflows 중 'Deploy prod' 선택
  - Run workflow 버튼 클릭
  - 배포할 브랜치 선택 후 Run workflow 실행

## 개발환경

### 로컬 환경변수 설정

* IntelliJ 사용 시
  * Run/Debug Configurations 화면에 진입
  * Configuration 탭
  * Environment variables에 아래와 같이 환경변수 추가
    * 변수에 해당하는 값은 live-server IAM의 키 정보 (별도문의 바람)
  ~~~
  *** AWS_ACCESS_KEY *** 
  *** AWS_SECRET_KEY ***
  ~~~

### Docker 이용 redis 설치

- redis image 다운로드
  ~~~shell
  # docker image pull redis
  ~~~
- redis run
  ~~~shell
  # docker run -d --name redis-container -p 6379:6379 -dit --restart unless-stopped redis
  ~~~
- Mac용 redis-cli 설치
  ~~~shell
  # brew install redis
  ~~~
- 접속
  ~~~shell
  # redis-cli -h localhost
  ~~~
## 타임존
* 'Asia/Seoul'

  ~~~java
  // Application.java 
  
  @PostConstruct
      public void started() {
          // TODO : timezone Asia/Seoul 로 지정 (향후 글로벌 서비스 시 해당 부분 수정이 필요)
          TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
      }
  ~~~
