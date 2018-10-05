<!DOCTYPE html>
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css/>

	</lams:head>
	<body class="tabpart">
	
	<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="label.authoring.conditions.add.condition" /></div>
	</div>

	<div class="panel-body">	
	
		<!-- Basic Info Form-->
		<lams:errors/>
		<form:form action="../authoringCondition/saveOrUpdateCondition.do" method="post" modelAttribute="forumConditionForm" id="forumConditionForm" focus="displayName" >
			<form:hidden path="orderId" />
			
			<div class="form-group">
			    <label for="displayName"><fmt:message key="label.authoring.conditions.condition.name" /> *</label>
			    <input type="text" tabindex="1" name="displayName" value="${forumConditionForm.displayName}" size="51" class="form-control"/>
			</div>

			<%-- Text search form fields are being included --%>
			<lams:TextSearch sessionMapID="${sessionMapID}"  />
						
			<h4><fmt:message key="textsearch.topics" /></h4>
			<c:forEach var="itemE" items="${forumConditionForm.possibleItems}">
		    	<div class="checkbox">
		    	<label>
		    	<form:checkbox path="selectedItems" value="${itemE.value}"/>
		    		<c:out value="${itemE.key}" />
		    	</label>
				</div>
		    	</c:forEach>
			
		</form:form>

		<div class="voffset5 pull-right">
		    <a href="#" onclick="hideConditionMessage();" class="btn btn-default btn-xs"><fmt:message key="button.cancel" /> </a>
			<a href="#" onclick="submitCondition();" class="btn btn-default btn-xs"><fmt:message key="label.save" /> </a>
			
		</div>
		
	</div>
	</div>
	</body>
</lams:html>
