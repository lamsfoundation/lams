<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="index.outcome.manage" /></title>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/outcome.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/outcome.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.cookie.js"></script>
	<lams:JSImport src="includes/javascript/dialog.js" />
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="outcome.manage.add" var="ADD_OUTCOME_TITLE_VAR"/>
				ADD_OUTCOME_TITLE : '<c:out value="${ADD_OUTCOME_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.edit" var="EDIT_OUTCOME_TITLE_VAR"/>
				EDIT_OUTCOME_TITLE : '<c:out value="${EDIT_OUTCOME_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.remove.confirm" var="REMOVE_OUTCOME_CONFIRM_LABEL_VAR"/>
				REMOVE_OUTCOME_CONFIRM_LABEL : decoderDiv.html('<c:out value="${REMOVE_OUTCOME_CONFIRM_LABEL_VAR}" />').text()
			};
	</script>
</lams:head>
<body class="stripes">
<lams:Page type="admin" >
	<lams:errors path="*"/>
	
	<div class="outcomeContainer">
		<div class="row">
			<div class="col-xs-5">
				<fmt:message key='outcome.manage.add.name' />
			</div>
			<div class="col-xs-3">
				<fmt:message key='outcome.manage.add.code' />
			</div>
			<div class="col-xs-1">
			</div>
			<div class="col-xs-1">
			</div>
		</div>
		<c:forEach var="outcome" items="${outcomes}">
			<div class="row">
				<div class="col-xs-5">
					<c:out value="${outcome.name}" />
				</div>
				<div class="col-xs-3">
					<c:out value="${outcome.code}" />
				</div>
				<div class="col-xs-1">
					<i class="manageButton fa fa-pencil" title="<fmt:message key='outcome.manage.edit' />"
				   	   onClick="javascript:openEditOutcomeDialog(${outcome.outcomeId})" >
					</i>
				</div>
				<div class="col-xs-1">
					<csrf:form style="display: inline-block;" id="remove_${outcome.outcomeId}" method="post" action="outcomeRemove.do"><input type="hidden" name="outcomeId" value="${outcome.outcomeId}"/><button type="button" onClick="javascript:removeOutcome('remove_${outcome.outcomeId}')" class="btn btn-danger btn-xs"><i class="fa fa-trash" title="<fmt:message key='outcome.manage.remove' />"></i></button></csrf:form>
				</div>
			</div>
		</c:forEach>
		<c:if test="${not empty outcomes}">
			<div id="exportButton" class="btn btn-default pull-left" onClick="javascript:exportOutcome()"
				 data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="outcome.export" /></span>">
				<i class="fa fa-download"></i>
				<span class="hidden-xs">
					<fmt:message key="outcome.export" />
				</span>
			</div>
		</c:if>
		
		<div id="importButton" class="btn btn-default pull-left" onClick="javascript:$('#importInput').click()">
			<i class="fa fa-upload"></i> <fmt:message key="outcome.import" />
		</div>
		<form id="importForm" action="outcomeImport.do" method="post" enctype="multipart/form-data">
			<input type="file" id="importInput" name="file"/>
		</form>
		<div id="addButton" class="btn btn-primary" onClick="javascript:openEditOutcomeDialog()">
			<i class="fa fa-plus"></i>
			<span><fmt:message key='outcome.manage.add' /></span>
		</div>
	</div>
</lams:Page>
</body>
</lams:html>
