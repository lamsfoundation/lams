<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
</script>

<div class="panel">
	<h4>
	    <c:out value="${leaderselectionDTO.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${leaderselectionDTO.instructions}" escapeXml="false"/>
	</div>
</div>

<c:if test="${isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="session" items="${leaderselectionDTO.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<div class="panel panel-default" >
        	<div class="panel-heading" id="heading${session.sessionID}">
   	    		<span class="panel-title collapsable-icon-left">
       				<a role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
							aria-expanded="false" aria-controls="collapse${session.sessionID}" >
						${session.sessionName}
					</a>
				</span>
       		</div>
       
        	<div id="collapse${session.sessionID}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading${session.sessionID}">
	</c:if>

	<table class="table table-condensed table-striped">
	
		<tr>
			<th>
				<fmt:message key="heading.learner" />
			</th>
			<th style="text-align: center;">
				<fmt:message key="heading.leader" />
			</th>
		</tr>
		
		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td width="30%">
					<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
				</td>
				<td width="70%" align="center">
					<c:choose>
						<c:when test="${session.groupLeader != null && session.groupLeader.uid == user.uid}">
							<i class="fa fa-check"></i>
						</c:when>
		
						<c:otherwise>
							-
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		
	</table>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
</c:forEach>

<c:if test="${isGroupedActivity}">
	</div> 
</c:if>

<a href="<c:url value='/monitoring/manageLeaders.do?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true'/>" class="btn btn-default pull-right thickbox">
	<fmt:message key="label.manage.leaders" />
</a>
