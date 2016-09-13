<!DOCTYPE html>
		
<%@ include file="/common/taglibs.jsp"%>

<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.authoring.conditions.add.condition" /></div>
</div>

<div class="panel-body">	

	<%@ include file="/common/messages.jsp"%>
	<html:form action="/authoring/saveOrUpdateCondition" method="post" styleId="surveyConditionForm" focus="displayName" >
		<html:hidden property="orderId" />
		
		<div class="form-group">
		    <label for="displayName"><fmt:message key="label.authoring.conditions.condition.name" /></label>
       		<html:text property="displayName" size="51"  styleClass="form-control"/>
		</div>

		<%-- Text search form fields are being included --%>
		<lams:TextSearch wrapInFormTag="false" sessionMapID="${sessionMapID}"  />
		<h4><fmt:message key="textsearch.questions" /></h4>
		<logic:iterate name="surveyConditionForm" id="itemE" property="possibleItems">
			<div class="checkbox">
			<label>
		  	<html:multibox property="selectedItems">
		    	<bean:write name="itemE" property="value" />
		  	</html:multibox>
		    <bean:write name="itemE" property="label" />
		    </label>
		    </div>
		</logic:iterate>
	
	</html:form>

	<div class="voffset5 pull-right">
	    <a href="#" onclick="hideConditionMessage();" class="btn btn-default btn-xs"><fmt:message key="button.cancel" /> </a>
	    <a href="#" onclick="submitCondition();" class="btn btn-default btn-xs"><fmt:message key="label.save" /> </a>
	</div>

</div>
</div>