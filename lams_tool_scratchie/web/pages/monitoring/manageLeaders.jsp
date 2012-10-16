<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
		<style media="screen,projection" type="text/css">
			.refresh-button {float:right; margin-right:12px; padding-top:5px;}
		</style>	
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>

  	    <script>
  	    	<!-- 
  	    	
    		function save()  {
	    		$('#leaders').ajaxSubmit({
    		   		success: function (responseText, statusText)  {
    	    			self.parent.tb_remove();
    	    		}
	    		});
    		}
	  		
    		function closeThickboxPage()  {
        		self.parent.tb_remove();
    		}
  			-->
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.plese.select.leaders" />
			</h1>
			<br/>
			
			<form action="<c:url value='/monitoring/saveLeaders.do'/>?sessionMapID=${sessionMapID}" id="leaders">
				<c:forEach var="summary" items="${summaryList}" varStatus="status">
					<h1></h1>
					<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
						<c:if test="${sessionMap.isGroupedActivity}">
							<div style="padding-bottom: 5px; font-size: small;">
								<B><fmt:message key="monitoring.label.group" /></B> ${summary.sessionName}
							</div>
						</c:if>
						
						<c:forEach var="user" items="${summary.users}">
							<div>
								<input type="radio" name="sessionId${summary.sessionId}" value="${user.userId}" <c:if test="${user.leader}">checked="checked"</c:if>/>
								${user.firstName} ${user.lastName}
							</div>
						</c:forEach>
					</div>
				</c:forEach>
			</form>


			<lams:ImgButtonWrapper>
				<a href="#" onclick="closeThickboxPage();" class="button refresh-button">
					<fmt:message key="label.authoring.cancel.button" /> 
				</a>
				<a href="#" onclick="save();" class="button refresh-button">
					<fmt:message key="label.authoring.save.button" /> 
				</a>

			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
