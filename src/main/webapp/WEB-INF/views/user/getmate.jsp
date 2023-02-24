<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>	
<%@ include file="../layout/header.jsp" %>
<main class="main">
<%@ include file="../sidemenu/sidemenu.jsp" %>
	<!--전체 리스트 시작-->
	<div class="map_wrap">
		<div id="map" style="height:700px;"></div>
		<div id="menu_wrap" class="bg_white">
        <div class="option">
            <div>
                <form onsubmit="searchPlaces(); return false;">
                    키워드 : <input type="text" value="성수동 카페거리" id="keyword" size="13"> 
                    <button type="submit">검색하기</button> 
                </form>
            </div>
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
	</div>
</main>
<!--  <script src="/js/story.js"></script>-->
<!-- services와 clusterer, drawing 라이브러리 불러오기 -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=88c4127abe68950c2cb72f1f60ee7f6c&libraries=services,clusterer,drawing"></script>
<script src="/js/map.js"></script>
<script src="/js/upload.js" ></script>