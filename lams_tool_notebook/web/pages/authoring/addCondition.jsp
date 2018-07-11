<%@ include file="/common/taglibs.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<fmt:message key="label.authoring.conditions.add.condition" />
		</div>
	</div>

	<div class="panel-body">

		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<form:form action="saveOrUpdateCondition.do" method="post"
			modelAttribute="notebookConditionForm" id="notebookConditionForm" focus="displayName">
			<form:hidden path="orderId" />

			<div class="form-group">
				<label for="displayName"><fmt:message
						key="label.authoring.conditions.condition.name" /> *</label>
				<form:input tabindex="1" property="displayName" size="51"
					cssClass="form-control" />
			</div>

			<%-- Text search form fields are being included --%>
			<lams:TextSearch wrapInFormTag="false" sessionMapID="${sessionMapID}" />
		</form:form>

		<div class="voffset5 pull-right">
		    <a href="#" onclick="hideConditionMessage()"
				class="btn btn-default btn-xs"><fmt:message key="button.cancel" />
			</a>
			<a href="#" onclick="submitCondition()"
				class="btn btn-default btn-xs"><fmt:message key="label.save" />
			</a> 
		</div>
	</div>
</div>