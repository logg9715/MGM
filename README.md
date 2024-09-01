MySql 연동 상태. 다만 이 점을 지켜줄 것.
http://localhost:8081/


<h3> 1. application.properties 파일에서 </h3><br>

<p>spring.datasource.username=root <-------------- !!!</p>
<p>spring.datasource.password=drkim4926^ <-------- !!!!</p><br>

<p>이 두 부분을 본인 컴퓨터의 MySql에 맞는 걸로 변환시키기.</p><br><br>

<h3>MySQL에서 아래 코드를 한 번만 기동시키기</h3>
<p>drop schema if exists flower;</p>
<p>create schema flower;</p>
<p>use flower;</p>
<br><br>

<h3> 7월 21일 업데이트된 목록</h3>
<h4>entity 폴더</h4>
<p>User</p>
<p>UserRole</p>
<br>

<h4>dto 폴더</h4>
<p>UserForm</p>
<br>
<h4>controller 폴더</h4>
<p>SessionCheckController <- 로그인 한 아이디 체크, 로그인 한 아이디가 관리자 여부인가를 체크</p>
<p>SessionLoginController <- 세션 로그인 및 로그아웃 처리</p>
<p>UserController <- 회원가입, 관리자 페이지의 사용자 생성/수정/삭제</p>
<br>
<h4>api 폴더</h4>
<p>UserApiController <- REST API 관련으로 회원가입, 관리자 페이지 사용자 생성/수정/삭제 사용자 아이디 중복체크, 사용자 닉네임 중복체크</p>
<br>
<h4>service 폴더</h4>
<p>UserService</p>
<br>
<h4>resource 폴더의 sessionLogin 폴더와 users 폴더</h4>
<h4>sessionLogin 폴더의</h4>
<p>join.mustache <- 회원가입 코드. UserApiController의</p> 
    <p>@PostMapping("/join") public User joinUser(@RequestBody UserForm form) {...} 과 UserService의 @Transactional public User saveUser(User user) {...} 가 각각 연관됨.</p>
<h4>users 폴더의</h4>
<p>edit, index 또한 UserService와 UserApiController과 연관되어 있음.</p>



[졸업프로젝트 테이블 목록.xlsx](https://github.com/user-attachments/files/16740113/default.xlsx)

