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
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/authoring/saveOrUpdateCondition" method="post" styleId="taskListConditionForm" focus="name" >
				<html:hidden property="sessionMapID" />
				<html:hidden property="sequenceId" />
	
				<div class="form-group">
	            	<label for="name"><fmt:message key="label.authoring.conditions.condition.name" /></label>
	         		<html:text property="name" styleClass="form-control"/>
				</div>
	
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />					
	        	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
		    	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		    
		    	<div class="form-group form-inline">
			    <logic:iterate name="taskListConditionForm" id="itemE" property="possibleItems">
				  	<html:multibox property="selectedItems" styleClass="form-control">
				    	<bean:write name="itemE" property="value" />
				  	</html:multibox>
				    &nbsp;<bean:write name="itemE" property="label" />
				    <br />
				</logic:iterate>
				</div>
				
			<div class="voffset5 pull-right">
			    <a href="#" onclick="hideConditionMessage()"
					class="btn btn-default btn-xs"><fmt:message key="label.cancel" />
				</a>
				<a href="#" onclick="submitCondition()"
					class="btn btn-default btn-xs"><fmt:message key="button.add" />
				</a> 
			</div>
			
			</html:form>
	
			
		</div>
		</div>
		
	</body>
</lams:html>
