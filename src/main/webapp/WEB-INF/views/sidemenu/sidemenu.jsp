<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<script src="/js/sidemenu.js"></script>
<script src="/js/map.js"></script>
<body>
<aside class="sidemenu">
<h2 style=" margin: 10px 10px;">Create and share your schedule!</h2>
	<form class="getmate__input"  name= "listOrd" action="/user/getmate" method="post" id="insertMapForm" enctype="multipart/form-data">
		<p><span class="spnCl">일정제목</span><input type="text" name="title" placeholder="일정제목" maxlength="30" class="inPt" style="width:250px !important;"/></p>
		<p><span class="spnCl" style="position: relative; top: -40px;">일정설명</span><textarea name="detail" placeholder="내용을 입력해주세요" style="margin-left:20px; resize: none; height: 100px; width:250px; margin-top: 5px;"></textarea></p>
		<p><span class="spnCl">기간</span><input type="date" name="date" placeholder="날짜" class="inPt"/></p>
	    <p><span class="spnCl">희망인원</span><input type="number" name="member" placeholder="희망인원" class="inPt" /> 명</p>
	    
		<div style="overflow-y: auto; height:380px; border: 1px solid gray; margin: 15px 0px;">
		<ul id="placesListInfo"></ul>
		<textarea id="infoText" style="display:none;" name="course"></textarea>
		</div>
	    <button type="button" class="canCl" onclick="javascript:canBtn(); return false;">취소하기</button>
	    <button class="regCl">등록하기</button>
	</form>
	
</aside>
</body>
</html>