<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
	"http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
	<lams:head>
		<lams:css />
		<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
		<style media="screen,projection" type="text/css">
			.refresh-button {float:right; margin-right:12px; padding-top:5px;}
		</style>	
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>

  	    <script>
    		function save()  {
	    		$('#leaders').ajaxSubmit({
    		   		success: function (responseText, statusText)  {
    		   			self.parent.location.reload();
    	    		}
	    		});
    		}
	  		
    		function closeThickboxPage()  {
        		self.parent.tb_remove();
    		}
  		</script>
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.plese.select.leaders" />
			</h1>
			<br/>
			
			<form action="<c:url value='/monitoring.do'/>?dispatch=saveLeaders&sessionMapID=${sessionMapID}" id="leaders">
				<c:forEach var="session" items="${sessionMap.leaderselectionDT0.sessionDTOs}" varStatus="status">
					<h1></h1>
					<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
						<c:if test="${sessionMap.isGroupedActivity}">
							<div style="padding-bottom: 5px; font-size: small;">
								<B><fmt:message key="monitoring.label.group" /></B> ${session.sessionName}
							</div>
						</c:if>
						
						<c:forEach var="user" items="${session.userDTOs}">
							<div>
								<input type="radio" name="sessionId${session.sessionID}" value="${user.uid}" <c:if test="${session.groupLeader.uid == user.uid}">checked="checked"</c:if>/>
								<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
							</div>
						</c:forEach>
					</div>
				</c:forEach>
			</form>

			<lams:ImgButtonWrapper>
				<a href="#" onclick="closeThickboxPage();" class="button refresh-button">
					<fmt:message key="button.cancel" /> 
				</a>
				<a href="#" onclick="save();" class="button refresh-button">
					<fmt:message key="button.save" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
