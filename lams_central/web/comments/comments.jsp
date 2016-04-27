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
		<link rel="stylesheet" href="<lams:LAMSURL />/includes/font-awesome/css/font-awesome.min.css">
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jscroll.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/comments.js"></script>		

		<script type="text/javascript">
			$(document).ready(function(){
			    scrollDoneCallback();
			    
   				$('#sortMenu').change(function(){
   					var url = "<lams:LAMSURL />comments/viewTopic.do?sessionMapID=${sessionMapID}&sticky=true&sortBy="+$(this).find("option:selected").attr('value');
   				 	reloadDivs(url);
			    });
			});
			
			function refreshComments(){
				var reqIDVar = new Date();
				reloadDivs('<lams:LAMSURL />comments/viewTopic.do?sessionMapID=${sessionMapID}&sticky=true&reqUid='+reqIDVar.getTime());
			}

			function reloadDivs(url) {
				$('#newcomments').children().remove();
				$('.scroll').data('jscroll', null); 
				$('#commentDiv').load(url);
			}
			
			function scrollDoneCallback() {
			}
		</script>		
		
	</lams:head>
	<body>
			<h2><fmt:message key="heading.comments"/></h2>
		
			<c:if test='${not sessionMap.readOnly}'>
			<%@ include file="new.jsp"%>
			</c:if>

			
			<select id="sortMenu" name="sortMenu">
    			<option value='0' <c:if test='${sessionMap.sortBy == 0}'>selected</c:if>><fmt:message key="label.newest.first"/></option>
    			<option value='1' <c:if test='${sessionMap.sortBy == 1}'>selected</c:if>><fmt:message key="label.top.comments"/></option>
			</select>
			
			<div id="newcomments"></div>			

			<div id="commentDiv">
			<%@ include file="allview.jsp"%>
			</div>  

	</body>
</lams:html>
