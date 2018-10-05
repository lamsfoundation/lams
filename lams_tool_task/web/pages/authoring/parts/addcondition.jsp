<!DOCTYPE html>
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
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
			<form:form action="../authoringCondition/saveOrUpdateCondition.do" method="post" modelAttribute="taskListConditionForm" id="taskListConditionForm" focus="name"
			 onSubmit="javascript:return false;" >
				<lams:errors/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="sequenceId" />
	
				<div class="form-group">
	            	<label for="name"><fmt:message key="label.authoring.conditions.condition.name" /></label>
	         		<form:input path="name" cssClass="form-control"/>
				</div>
				
	        	<c:set var="sessionMapID" value="${taskListConditionForm.sessionMapID}" />				
		    	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		    
		    	<c:forEach var="itemE" items="${taskListConditionForm.possibleItems}">
		    	<div class="checkbox">
		    	<label>
		    	<form:checkbox path="selectedItems" value="${itemE.key}"/>
		    		<c:out value="${itemE.value}" />
		    	</label>
				</div>
		    	</c:forEach>
		    	
			</form:form>	
		
			<div class="voffset5 pull-right">
			    <a href="#" onclick="hideConditionMessage()"
					class="btn btn-default btn-xs"><fmt:message key="label.cancel" />
				</a>
				<a href="#" onclick="submitCondition()"
					class="btn btn-default btn-xs"><fmt:message key="button.add" />
				</a> 
			</div>
			
			
	
			
		</div>
		</div>
		
	</body>
</lams:html>
