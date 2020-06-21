# 소켓, Rest API 알람 정의서 
  
## 화면 레이아웃 
- `[쪽지](2)[게시판](3)[회원정보](3)`

## 1. 쪽지알람 (쪽지글을 안읽었을때) 
### 이벤트 흐름도
```
=> 클릭! => 쪽지함 이동 => 목록 
=> 클릭 => 상세 => 서버로 쪽지 id를 보낸다. 
=> 쪽지 개수 수정 => 클라이언트 수정된 개수 보낸다.
=> 마우스오버 => 목록출력
{
	url: '/socket/comment/notread',
	body: {
		notReadTotalCount: 2
	}	
}  
```

## 2. 게시글 알람 (게시글을 썼는데 댓글/좋아요/싫어요을 달았다.) 
### 이벤트 흐름도
```
=> 클릭! => 목록 보기 팝업 => 클릭! => 서버 게시판ID 3건 => 2건 업데이트 
=> 클라이언트 화면으로 개수 줄꺼고 => 상세화면 이동
=> 마우스오버 => 목록출력
- socket 
{
	url: '/socket/board/comment/list'
	count: 3,
	boardId: [1,3,4] // board(title) + comment(iconKind) + user(userid, username)
}
- rest api 
{
	url: '/api/board/comment/list',
	body: [
		{
			boardType: '자유게시판',
			boardId: 1,
			iconKind: 'COMMENT_1', // 1:댓글, 2:좋아요, 3:싫어요 
			userName: '태연',
			userId: 1 // 프로필 이동 
			title: '게시글 댓글이 달린 글입니다.
		},
		{
			boardType: '자유게시판',
			boardId: 3,
			iconKind: 'COMMENT_2', // 1:댓글, 2:좋아요, 3:싫어요 
			userName: '태연',
			userId: 1 // 프로필 이동
			title: '게시글 좋아요가 달린 글입니다.
		},
		{
			boardType: '자유게시판',
			boardId: 4,
			iconKind: 'COMMENT_3', // 1:댓글, 2:좋아요, 3:싫어요 
			userName: '선미',
			userId: 2 // 프로필 이동
			title: '게시글 싫어요가 달린 글입니다.
		},
	]
}
```
  
## 3. 팔로우 알람 
### 이벤트 흐름도
```
=> 클릭! => 서버로 유저정보 전송 
=> 서버에서 수정된 개수 응답 => 프로필 이동
- socket 
{
	url: '/socket/user/follow'
	count: 3,
	fromUserIds: [1,2,3],
	toUserIds: [2,2,3]
}
- rest api 
{
	url: '/api/user/follow',
	list: [
		userId: '1',
		followType: 'F1' // F1: 팔로우 F2: 언팔로우
	]	
}
```

### 클라우드 웹 아키텍처
```
모노리틱 / soa / msa ...
cloud 환경 => ASG (Auto scale out - 서버, redis, mongod / Auto scale up - DB)
                so + su => master(insert,update,delet) + slave(select) 9대...
``` 

# 참고
## 프론트 앤드에서 백앤드로 진입 흐름도
```
front => ㄴ api g/w - auth or resource server  
          ㄴ resource source - rest api 
		    ㄴ auth server - 인증/권한 
```

## * http 통신 정의
```		 
# rquest header
status code : 200, 300, 400, 500 ...
content-type : application/json
token: 23ejrhgeuiht3jh4t348th434kj53klj54kl345 => sessionStoage

# request body
{
	code: A001, 
	msg: '로그인 정상 처리 되었습니다.',
	body: {
		userId: 'user01',
		addr: '주소'
		email: 'user@gmail.com',
		menus : [게시판1, 게시판2, 댓글]
	}
}
```

## * http status code - 에러코드
```
2XX : 정상
3XX : 리다이렉트 (http://domain/login (round robin / 스티키) - 로그인  
				=> nginx (웹서버/cdn) = static file / 1차 cache  
				=> nodejs(도메인같음, 도메인X) / 2차 캐시 => get => redis 
				=> was(도메인같음) 3차 캐시(DB) redis => cors => DB 서버		
				
4XX : 클라이언트 실수 

5XX : 서버 실수 
 ㄴ 500 : DB 에러  
 ㄴ 502 : 통신 장애 
    ㄴ front => map api => google api => 응답안줌 => timeout 3초 => fail
 ㄴ 501 : MSA => Side car(서킷브레이크) - 스프링 클라우드  
          ㄴ 타임아웃
		  ㄴ 실패율 전체 / 50% 
		  ㄴ 실패 회수 2회 이상 
		  ㄴ hystrix (히스트릭스) => fallback => exception => controller advice => 501, 실패, ''		  
```