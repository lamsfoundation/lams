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
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateCondition" method="post" styleId="chatConditionForm" focus="displayName" >
			<html:hidden property="orderId" />

			<div class="form-group">
            	<label for="displayName"><fmt:message key="label.authoring.conditions.condition.name" /></label>
         		<html:text property="displayName" size="51" styleClass="form-control" />
			</div>
			<%-- Text search form fields are being included --%>
			<lams:TextSearch wrapInFormTag="false" sessionMapID="${sessionMapID}"  />
		</html:form>

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
