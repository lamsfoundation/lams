<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.CommonConstants"%>
<script>
	$(document).ready(function(){
		//insert total learners number taken from the parent tblmonitor.jsp
		$("#total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);
	});
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-12 col-lg-12 col-xl-8">
		<h3>
			<fmt:message key="label.forum"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Notifications -->  
<div class="row no-gutter">
	<div class="col-6 col-lg-4 col-xl-4 ">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<i class="fa fa-users" style="color:gray" ></i> 
					<fmt:message key="label.attendance"/>: ${attemptedLearnersNumber}/<span id="total-learners-number"></span>
				</h4> 
			</div>
		</div>
	</div>                       
</div>
<!-- End notifications -->

<!-- Tables -->
<div class="row no-gutter">
<div class="col-12 col-lg-12 col-xl-12">

<h4><fmt:message key="label.grouping.group.heading"/></h4>

<c:forEach var="toolSession" items="${toolSessions}" varStatus="i">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<c:out value="${toolSession.toolSessionName}" escapeXml="false"/>
			</h4>
		</div>
		
		<div class="panel-body">
			<div class="table-responsive">
				<a href="/lams/tool/<%= CommonConstants.TOOL_SIGNATURE_FORUM %>/learning/viewForum.do?toolSessionID=${toolSession.toolSessionId}&mode=teacher&hideReflection=true" target="_blank" class="btn btn-default btn-sm">
					<i class="fa fa-comments"></i> <fmt:message key="label.view.forum" />
				</a>
			</div>
		</div>
	</div>
</c:forEach>

</div>
</div>
<!-- End tables -->

