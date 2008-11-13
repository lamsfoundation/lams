<%@ include file="/common/taglibs.jsp"%>

	<script type="text/javascript">
		$(document).ready(function(){

			alert("ccccc");
		});
	</script>

	
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="image" value="${sessionMap.currentImage}" />

<c:set var="currentRating" value="${sessionMap.currentRating}" />







