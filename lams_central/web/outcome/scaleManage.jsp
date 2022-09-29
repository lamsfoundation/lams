<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/outcome.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/outcome.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.cookie.js"></script>
	<lams:JSImport src="includes/javascript/dialog5.js" />
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="scale.manage.add" var="ADD_SCALE_TITLE_VAR"/>
				ADD_SCALE_TITLE : '<c:out value="${ADD_SCALE_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.edit" var="EDIT_SCALE_TITLE_VAR"/>
				EDIT_SCALE_TITLE : '<c:out value="${EDIT_SCALEE_TITLE_VAR}" />',
				<fmt:message key="scale.manage.remove.confirm" var="REMOVE_SCALE_CONFIRM_LABEL_VAR"/>
				REMOVE_SCALE_CONFIRM_LABEL : decoderDiv.html('<c:out value="${REMOVE_SCALE_CONFIRM_LABEL_VAR}" />').text()
			};
	</script>
</lams:head>
<body class="component bg-white p-3">
<lams:Page type="admin" >

	<lams:errors/>	

	<div class="outcomeContainer">
		<div class="row">
			<div class="col-7">
				<fmt:message key='outcome.manage.add.name' />
			</div>
			<div class="col-3">
				<fmt:message key='outcome.manage.add.code' />
			</div>
		</div>
		<c:forEach var="scale" items="${scales}">
			<div class="row">
				<div class="col-7">
					<c:out value="${scale.name}" />
				</div>
				<div class="col-3">
					<c:out value="${scale.code}" />
				</div>
				<div class="col-2">
					<c:choose>
						<c:when test="${scale.scaleId != 1}">
							<button class="btn btn-secondary" onClick="javascript:openEditScaleDialog(${scale.scaleId})"
								 	title="<fmt:message key='scale.manage.edit' />">
								<i class="fa fa-pencil"></i>
							</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-secondary" onClick="javascript:openEditScaleDialog(${scale.scaleId})"
								 	title="<fmt:message key='scale.manage.view' />">
								<i class="fa fa-eye"></i>
							</button>
						</c:otherwise>
					</c:choose>
					<c:if test="${scale.scaleId != 1}">
						<button type="button" onClick="javascript:removeScale(${scale.scaleId})" class="btn btn-danger">
							<i class="fa fa-trash" title="<fmt:message key='scale.manage.remove' />"></i>
						</button>
					</c:if>
				</div>
			</div>
		</c:forEach>
		<div id="exportButton" class="btn btn-secondary float-start" onClick="javascript:exportOutcome(true)"
			 data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="outcome.export" /></span>">
			<i class="fa fa-download"></i>
			<span class="hidden-xs">
				<fmt:message key="outcome.export" />
			</span>
		</div> 
		<div id="importButton" class="btn btn-secondary float-start" onClick="javascript:$('#importInput').click()">
			<i class="fa fa-upload"></i> <fmt:message key="outcome.import" />
		</div>
		<form id="importForm" action="scaleImport.do" method="post" enctype="multipart/form-data">
			<input type="file" id="importInput" name="file"/>
		</form>
		<div id="addButton" class="btn btn-primary" onClick="javascript:openEditScaleDialog()">
			<i class="fa fa-plus"></i>
			<span><fmt:message key='scale.manage.add' /></span>
		</div>
	</div>
</lams:Page>
</body>
</lams:html>