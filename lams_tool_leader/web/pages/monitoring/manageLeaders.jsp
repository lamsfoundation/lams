<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<c:set var="title"><fmt:message key="label.plese.select.leaders" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet"/>
		
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
	
	<h1 class="fs-3 mb-4">
		${title}
	</h1>

	<form action="<c:url value='/monitoring/saveLeaders.do'/>" method="post" id="leaders">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
        <input name="sessionMapID" type="hidden" value="${sessionMapID}"/>
        
		<c:forEach var="session" items="${sessionMap.leaderselectionDT0.sessionDTOs}" varStatus="status">
			<div class="lcard">
				<c:if test="${sessionMap.isGroupedActivity}">
					<div class="card-header">
						<B><fmt:message key="monitoring.label.group" /></B> 
						${session.sessionName}
					</div>
				</c:if>
						
				<c:forEach var="user" items="${session.userDTOs}">
					<div class="card-body align-items-center d-flex p-2">
						<input type="radio" name="sessionId${session.sessionID}" id="sessionId${session.sessionID}-${user.uid}" 
								value="${user.uid}" class="form-check-input me-2"
								<c:if test="${session.groupLeader.uid == user.uid}">checked="checked"</c:if>/>
								
						<label for="sessionId${session.sessionID}-${user.uid}" class="form-check-label">
							<lams:Portrait userId="${user.userId}"/>&nbsp;
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
						</label>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</form>

	<div class="activity-bottom-buttons">
		<button type="button"onclick="save();" class="btn btn-primary ms-2">
			<i class="fa fa-check fa-lg me-1"></i>
			<fmt:message key="button.save" /> 
		</button>
		<button type="button" onclick="closeThickboxPage();" class="btn btn-secondary">
			<i class="fa fa-xmark fa-lg me-1"></i>
			<fmt:message key="button.cancel" /> 
		</button>
	</div>
</lams:PageMonitor>
