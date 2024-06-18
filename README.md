# 🚀 BackOffice Project: Polo::Ject

이번 팀 프로젝트의 미션은 “백오피스 개발 프로젝트”입니다.

## 📋 Polo::Ject 개요

온라인 의류 스토어 기능을 구현한 프로젝트로, 쇼핑몰의 기본적인 CRUD + 쇼핑몰 관리자 기능을 함께 구성해보았습니다.
관리자와 사용자의 역할을 명확하게 구분하고, 적용해 볼 수 있는 다양한 기능들을 고민해 볼 수 있어 다음 주제로 프로젝트를 진행하게 되었습니다.

스토어 내 역할을 단순화하기 위해 ‘일반 사용자’(USER) / ‘관리자’(ADMIN) 두 개의 ROLE로 분리하여 설계를 진행하였으며,
여러 브랜드가 모여있는 편집샵이 아닌, 스마트스토어와 유사한 구조를 가집니다.

## ✨ ERD
![image](https://github.com/JinkownHong/newsfeed-team-project/assets/161419351/86b47397-5dbf-48a4-8c4f-9cbb692ab5f9)

## ✨ Wireframe, API
아래의 Notion Link 를 참고해주세요.
- https://teamsparta.notion.site/7-bfeda8f71e2b4f8eb049cc17962dafb5

## ✨ 주요 기능 소개

#### 1. PRODUCT
* Create, Update, Delete Product Admin만 접근하고 사용할 수 있도록 설계
* DeletedAt 을 통해 SoftDelete 기능 구현 (DeletedAt이 Null인 상품만 일반 사용자가 조회할 수 있도록 설정)
* 상품명을 검색하거나 카테고리, 상품 할인 여부, 상품 등록 순을 기준으로 상품 조회가 가능하도록 페이징 방식 구현


#### 2. REVIEW
* 주문한 상품만 리뷰를 남길 수 있도록 설정
* DeleteReview 관련 일반 User의 경우 작성자만 삭제할 수 있도록 기능 설정
* 일반 유저가 댓글을 삭제하는 화면에서 관리자도 댓글을 삭제할 수 있도록 기능 설정 (preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER, MemberRole.ADMIN))
* 관리자는 Product와 상관없이 모든 리뷰를 조회할 수 있으며, 문제의 소지가 있다고 판단되는 특정 사용자의 리뷰를 모두 확인하여 삭제할 수 있도록 설정


#### 3. USER, ADMIN
* 프로젝트 주제가 BackOffice인 만큼 Admin, User 부분을 각각 관리할 수 있도록 도메인 분리
* ID는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
* password는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성
* 최근 3번 안에 사용한 비밀번호는 사용할 수 없도록 제한 설정 (PasswordHistoryService)


#### 4. ORDER
* Order, OrderProduct 두 개의 Entity로 구성
* OrderStatus의 경우 관리자와 일반 사용자가 접근할 수 있는 범위의 차이로 API를 분리 (관리자 - PLACED, SHIPPING, CONFIRMED, CANCELLED / 일반 사용자 - CANCELLED)
* Product Stock Order 내 Quantity를 통해 관리 진행


#### 5. LOGIN /LOGOUT
* JWT(JSON Web Token) 활용 인증 인가 구현
* Spring AOP를 사용하지 않고, 트레일링 람다(Trailing Lambdas)를 통한 AOP 구현
* 기존 AOP 문자열로 처리되어 컴파일 시점에 오류 확인이 어려워, customPreAuthorize를 통한 기능 구현
* 로그아웃을 DB를 사용하여 구현 시 Redis 비교 입출력 속도가 느리고 일정 시간마다 삭제해줘야하기 때문에 기본 만료시간을 설정할 수 있는 Redis를 도입
* 블랙리스트 방식으로 로그인을 구현, 로그아웃 된 사용자의 토큰만 블랙리스트에 저장하도록 설정


## ✨ About Code

#### QueryDSL
* 서비스 비즈니스 로직에 더 집중하고, 동적 쿼리 활용을 위해 QueryDSL 적용
- findByPageableAndDeleted Method 활용 기존 JPA Paging 또는 검색을 구현하면 서비스가 무거워지는 문제를 해소
- getReviews Method Requestparam 내 UserId를 받아올 경우를 포함하여 하나의 Query로 코드 단순화 작업 진행

#### TEST CODE
* 일부 Controller, Service Test Code 작성
* ZonedDateTimeDeserializer
- responseDto에서 ZonedDateTime Type을 갖는 필드들이 String으로 변환되었다가, 다시 객체로 Deserialize될 때 ZoneId에 대한 정보를 잃어버리면서 UTC 시간으로 변경되어 테스트 실패
- objectMapper가 deserialize할 때 ZoneId를 "Asia/Seoul"로 설정하는 커스텀 Deserializer을 구현(ZonedDateTImeDeserializer)하고 objectMapper에 Module 설정

#### IMAGE UPLOAD
* PresignedUrl 활용
- 처음 이미지 업로드 시, 멀티파트 파일로 받아 S3 서버에 직접 업로드 진행 (그러나 이는 서버에 큰 부담을 주어 미리 서명된 URL을 발급 받아 Front에서 직접 업로드 할 수 있도록 설정 변경)
- 이미지 업로드 과정에서 문제가 생길 경우 대비 서버에서 이미지를 삭제하는 기능 함께 추가

* 흐름 정리
- S3Config에서 AmazonS3에 접근하기 위한 설정이 담긴 객체 생성 -> service에서 버킷 정보, 파일 이름, 만료 시간을 설정 후 Method PUT으로 설정 -> S3Config 객체를 사용 미리 서명된 URL 생성 -> controller에서 요청 시 URL을 문자열로 반환


## 📰 API 문서 확인
Swagger를 사용하여 API 문서를 조회할 수 있습니다. 브라우저에서 `http://localhost:8080/swagger-ui.html`로 접속하거나, 아래의 Notion 링크를 확인해주세요.
- https://teamsparta.notion.site/7-bfeda8f71e2b4f8eb049cc17962dafb5

## 📜 라이센스

이 프로젝트는 MIT 라이센스를 따릅니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 🔨 빌드 환경

* **Language:** Kotlin 1.9.24
* **IDE:** Intellij
* **JDK:** 17.0.11
* **SDK:** Eclipse Temurin 22.0.1
