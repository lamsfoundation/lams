<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<link type="text/css" href="${lams}css/thickbox.css" rel="stylesheet"  media="screen">
<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">		
		
<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
<lams:JSImport src="includes/javascript/portrait5.js" />
<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
</script>

<div class="instructions">
	<div class="fs-4">
	    <c:out value="${leaderselectionDTO.title}" escapeXml="true"/>
	</div>
	<div class="mt-2">
	    <c:out value="${leaderselectionDTO.instructions}" escapeXml="false"/>
	</div>
</div>

<c:forEach var="session" items="${leaderselectionDTO.sessionDTOs}">
	<c:if test="${isGroupedActivity}">
		<div class="lcard" >
        	<div class="card-header" id="heading${session.sessionID}">
   	    		<span class="card-title collapsable-icon-left">
       				<button type="button" class="btn btn-secondary-darker no-shadow" data-bs-toggle="collapse" data-bs-target="#collapse${session.sessionID}" 
							aria-expanded="false" aria-controls="collapse${session.sessionID}" >
						${session.sessionName}
					</button>
				</span>
       		</div>

        	<div id="collapse${session.sessionID}" class="card-body collapse show">
	</c:if>

	<div class="ltable table-hover">
		<div class="row">
			<div class="col">
				<fmt:message key="heading.learner" />
			</div>
			<div class="col text-center">
				<fmt:message key="heading.leader" />
			</div>
		</div>
		
		<c:forEach var="user" items="${session.userDTOs}">
			<div class="row">
				<div class="col">
					<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
				</div>
				<div class="col text-center">
					<c:choose>
						<c:when test="${session.groupLeader != null && session.groupLeader.uid == user.uid}">
							<i class="fa fa-check"></i>
						</c:when>
		
						<c:otherwise>
							-
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:forEach>
	</div>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
</c:forEach>

<a href="<c:url value='/monitoring/manageLeaders.do?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true'/>" class="btn btn-secondary float-end mb-3 thickbox">
	<i class="fa-solid fa-user-pen me-1"></i>
	<fmt:message key="label.manage.leaders" />
</a>
