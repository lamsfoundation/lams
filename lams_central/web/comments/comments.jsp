<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>


<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="loading_animation" value="${lams}images/ajax-loader.gif"/>
<c:set var="loading_words"><fmt:message key="msg.loading" /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
	<lams:head>
		
		<!-- ********************  CSS ********************** -->
		<c:choose>
			<c:when test="${not empty localLinkPath}">
				<lams:css localLinkPath="${localLinkPath}" />
			</c:when>
			<c:otherwise>
				<lams:css />
			</c:otherwise>
		</c:choose>
		<link type="text/css" href="<lams:LAMSURL />css/jquery.treetable.css" rel="stylesheet"/>
		<link type="text/css" href="<lams:LAMSURL />css/jquery.treetable.lams.css" rel="stylesheet"/>
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jscroll.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/comments.js"></script>		

		<script type="text/javascript">
			$(document).ready(function(){
				scrollDoneCallback();
			});
			
			function scrollDoneCallback() {
				resizeIframe();
				
			}
		</script>		
		
	</lams:head>
	<body>
			<h2><fmt:message key="heading.comments"/></h2>
		
			<c:if test='${sessionMap.mode == "learner"}'>
			<%@ include file="new.jsp"%>
			</c:if>
			
			<div id="newcomments"></div>			

			<c:if test='${(empty commentThread) && (sessionMap.mode == "teacher")}'>
				<fmt:message key="label.no.comments"/>
			</c:if>
					
			<c:if test="${! empty commentThread}">
			<div class="space-bottom">
			<div class="scroll" >
			<%@ include file="topicview.jsp"%>
			</div>
			</div>
			<script>
				$('.scroll').jscroll({loadingHtml: '<img src="${loading_animation}" alt="${loading_words}" />${loading_words}',padding:30,autoTrigger:true,callback:scrollDoneCallback});
			</script>
			</c:if>
					
	</body>
</lams:html>
