<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/taglibs.jsp"%>

		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>
		<title> 
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<lams:headItems />
		<lams:css/>
	</lams:head>
	
	<body>
	
	<div class="panel panel-default">
		<div class="panel-heading">
		<div class="panel-title">
			<fmt:message key="label.authoring.conditions.add.condition" />
		</div>
	</div>

	<div class="panel-body">
	
		<!-- Basic Info Form-->
		<lams:errors/>
		<form:form action="../authoringCondition/saveOrUpdateCondition.do" method="post" modelAttribute="chatConditionForm" id="chatConditionForm" focus="displayName" >
			<form:hidden path="orderId" />

			<div class="form-group">
            	<label for="displayName"><fmt:message key="label.authoring.conditions.condition.name" /></label>
         		<input type="text" value="${chatConditionForm.displayName}" name="displayName" size="51" class="form-control" />
			</div>
			<%-- Text search form fields are being included --%>
			<lams:TextSearch sessionMapID="${sessionMapID}"  />
		</form:form>

		<div class="voffset5 pull-right">
		<a href="javascript:;" onclick="hideConditionMessage()" class="btn btn-default btn-xs">
				<fmt:message key="label.cancel" /> </a>
			<a href="javascript:;" onclick="submitCondition();" class="btn btn-default btn-xs">
				<fmt:message key="label.save" /></a>
			
		</div>

	</div>
</div>

	</body>
</lams:html>
