/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

// (0) 현재 로긴한 사용자 아이디
let principalId = $("#principalId").val();


let modelUserId = $("#modelUserId").val();

// (1) 스토리 로드하기
let page = 0;

function storyLoad() {
	$.ajax({
		url : `/api/maplist/${modelUserId}?page=${page}`,
		dataType:"json"
	}).done(res=>{
		console.log(res)	;
		res.data.content.forEach((map)=>{
			let storyItem = getMapItem(map);
			$("#mapList").append(storyItem);
		});
	}).fail(error=>{
		console.log("오류",error)	;
	});
}

storyLoad();

function getMapItem(map) {
	let item = `<div class="story-list__item" style="border:none;">
	<div class="sl__item__header" style="height: 30px;">
		<div style="font-family: 'NotoSansCJKkr',Helvetica,sans-serif;">${map.user.username}</div>
	</div>
	
	<div class="map-inner">`;
		
		if(principalId == map.user.id){
	item += `<a href="#" onclick="deleteMap(${map.id})">x</a>`;
	}
	item += `
		<div class="info-img">
			<img class="profile-image" src="/upload/${map.user.profileImageUrl}" 
				onerror="this.src='/images/person.jpeg'" />
		</div>
		<div class="map-info">
			<div class="tit">
				${map.title}
			</div>
			<div class="cont">
				${map.detail}
			</div>
			<div class="period">
				<dl>
					<dt>일정날짜</dt>
					<dd>${map.date}</dd>
				</dl>
				<dl>
					<dt>모집인원</dt>
					<dd>${map.member}</dd>명
				</dl>
				<dl>
					<dt>들를곳</dt>
					<dd>
						<span class="color-point-4" >${map.course}</sapn>
					</dd>
				</dl>
			</div>
		</div>
		
		
	</div>

	<div class="sl__item__contents">
		<div class="sl__item__contents__icon" style="height: 30px; padding: 8px 0;">

			<button>`;
			
			if(map.likeState){
				item += `<i class="fas fa-heart active" id="storyLikeIcon-${map.id}" onclick="toggleLike(${map.id})"></i>`;
				}else{
				item += `<i class="far fa-heart" id="storyLikeIcon-${map.id}" onclick="toggleLike(${map.id})"></i>`;
				}
				item += `
				</button>
		<span class="like"><b id="storyLikeCount-${map.id}">${map.likeCount}</b>likes</span>
</div>

<div class="sl__item__contents__content">
</div>

<div id="storyCommentList-${map.id}"> `;

	map.comments.forEach((comment) =>{
		item += `
		<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
			<p>
				<b>${comment.user.username} :</b> ${comment.content}
			</p>`;
		
		if(principalId == comment.user.id){
		item += `<button onclick="deleteComment(${comment.id})">
				<i class="fas fa-times"></i>
			</button>`;
		}		
		item += `
		</div> `;
	});
	
	item += `

</div>

<div class="sl__item__input" style="border-bottom: 1px solid #dbdbdb;">
	<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${map.id}" />
	<button type="button" onClick="addComment(${map.id})">게시</button>
		</div>

	</div>
</div>` ;
return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	//console.log("윈도우 scrollTop" , $(window).scrollTop());
	//console.log("문서의 높이" ,$(document).height());
	//console.log("윈도우 height",  $(window).height());
	
	let checkNum = $(window).scrollTop() - ($(document).height()-$(window).height());
	console.log(checkNum);
	
	if(checkNum <1 && checkNum > -1){
		page++;
		storyLoad();
	}

});


// (3) 좋아요, 안좋아요
function toggleLike(mapId) {
	let likeIcon = $(`#storyLikeIcon-${mapId}`);
	
	if (likeIcon.hasClass("far")) { //좋아요 하겠다
		$.ajax({
			type : "post",
			url: `/api/map/${mapId}/likes`,
			dataType : "json"
		}).done(res=>{
			
			let likeCountStr = $(`#storyLikeCount-${mapId}`).text();
			let likeCount = Number(likeCountStr) +1;
			$(`#storyLikeCount-${mapId}`).text(likeCount);
			
			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(error=>{
			console.log("좋아요 호출 오류", error);
		})

	} else { //좋아요 취소하겠다
		$.ajax({
			type : "delete",
			url: `/api/map/${mapId}/likes`,
			dataType : "json"
		}).done(res=>{
			
			let likeCountStr = $(`#storyLikeCount-${mapId}`).text();
			let likeCount = Number(likeCountStr) -1;
			$(`#storyLikeCount-${mapId}`).text(likeCount);
			
			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(error=>{
			console.log("좋아요 취소 오류", error);
		})

	}
}

// (4) 댓글쓰기
function addComment(mapId) {

	let commentInput = $(`#storyCommentInput-${mapId}`);
	let commentList = $(`#storyCommentList-${mapId}`);

	let data = {
		mapId : mapId,
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
		type : "post",
		url: `/api/comment/map`,
		data : JSON.stringify(data),
		contentType : "application/json; charset=utf-8",
		dataType : "json"
	}).done(res=>{
		console.log("성공", res.data);
		
			let comment = res.data;
			let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
			    <p>
			      <b>${comment.user.username} :</b>
			      ${comment.content}
			    </p>
			    <button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
			  </div>
			`;
		commentList.prepend(content); //prepend는 미리 앞에다 넣는 것임 append와는 달라
	}).fail(error=>{
		alert(error.responseJSON.data.content);
		console.log("댓글 쓰기 오류", error);
	});

	commentInput.val(""); //인풋필드를 깨끗하게 비워준다
}

// (5) 댓글 삭제
function deleteComment(commentId) {
$.ajax({
		type : "delete",
		url: `/api/comment/${commentId}`,
		dataType : "json"
	}).done(res=>{
		console.log("댓글 삭제 성공", res);
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error=>{
		console.log("댓글 삭제 오류", error);
	});
}


// (6) 일정 삭제
function deleteMap(mapId) {
$.ajax({
		type : "delete",
		url: `/map/delete/${mapId}`,
		dataType : "json"
	}).done(res=>{
		console.log("삭제 성공", res);
		location.reload();
	}).fail(error=>{
		console.log("삭제 오류", error);
	});
}




