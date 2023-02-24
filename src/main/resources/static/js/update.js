// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); //폼태그 액션을 막기
	
	let data = $("#profileUpdate").serialize();
	
	$.ajax({
		type: "put",
		url : `/api/user/${userId}`,
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json"
	}).done(res=>{ //HttpStatus 상태코드 200번대
		console.log("성공", res);
		location.href = `/user/${userId}`;
	}).fail(error=>{//HttpStatus 상태코드 200번대가 아닐 때
		if(error.data == null){
			alert(error.responseJSON.message);			
		}else{
		alert(JSON.stringify(error.responseJSON.data)); //json 응답 형태의 값을 가져오는 함수
		}
		console.log("실패", error.responseJSON.data.name);
	});
	
	
}