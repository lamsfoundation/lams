<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
	<lams:head>
		<lams:css />
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet"/>
		
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
	
	<body class="stripes">

	<c:set var="title"><fmt:message key="label.plese.select.leaders" /></c:set>
	<lams:Page type="learner" title="${title}">
		<form action="<c:url value='/monitoring/saveLeaders.do'/>" method="post" id="leaders">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
                <input name="sessionMapID" type="hidden" value="${sessionMapID}"/>
				<c:forEach var="session" items="${sessionMap.leaderselectionDT0.sessionDTOs}" varStatus="status">
					<h1></h1>
					<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
						<c:if test="${sessionMap.isGroupedActivity}">
							<div style="padding-bottom: 5px; font-size: small;">
								<B><fmt:message key="monitoring.label.group" /></B> ${session.sessionName}
							</div>
						</c:if>
						
						<c:forEach var="user" items="${session.userDTOs}">
							<div class="voffset5">
								<input type="radio" name="sessionId${session.sessionID}" value="${user.uid}" <c:if test="${session.groupLeader.uid == user.uid}">checked="checked"</c:if>/>
								&nbsp;<lams:Portrait userId="${user.userId}"/>&nbsp;
								<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
							</div>
						</c:forEach>
					</div>
				</c:forEach>
			</form>

			<div class="pull-right">
				<a href="#" onclick="closeThickboxPage();" class="btn btn-default">
					<fmt:message key="button.cancel" /> 
				</a>
				<a href="#" onclick="save();" class="btn btn-primary">
					<fmt:message key="button.save" /> 
				</a>
			</div>

		</div>
		<!--closes content-->
	
		<div id="footer"></div>

	</lams:Page>		
	</body>
</lams:html>
