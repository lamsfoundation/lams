<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="language" value="${pageContext.response.locale.language}" />
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>

<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
        	saveUserSpreadsheet("finishSession");
			return false;
		}
		
		function continueReflect(){
			saveUserSpreadsheet("continueReflect");
		}
		
		//save changes made in spreadsheet
		function saveUserSpreadsheet(typeOfAction){
			var code = window.frames["externalSpreadsheet"].cellsToJS();
			code = encodeURIComponent(code);
		    var url = '<c:url value="/learning/saveUserSpreadsheet.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&typeOfAction=' + typeOfAction + '&code=' + code;
		    location.href = url;
		}  
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${spreadsheet.title}">
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert type="danger" id="lockWhenFinished" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		<c:if test="${userIsMarked && !(sessionMap.lockOnFinish and mode != 'teacher' && sessionMap.userFinished)}">
			<lams:Alert type="info" id="lockWhenFinished" close="false">
				<fmt:message key="message.spreadsheet.marked" />
			</lams:Alert>
		</c:if>
		

		<lams:errors/>
		
		<p>
			<c:out value="${spreadsheet.instructions}" escapeXml="false"/>
		</p>

		<input type=hidden name="spreadsheetCode" id="spreadsheet-code" value="${sessionMap.code}"/>
		<br>
		
		<c:if test="${spreadsheet.markingEnabled}">
		<div class="row no-gutter">
			<div class="col-12">
			<div class="panel panel-default">
				<div class="panel-body">
				<div class="row no-gutter">
					<div class="col-md-2"><fmt:message key="label.learning.comments" /></div>
					<div class="col-md-10">
						<c:choose>
							<c:when test="${mark == null}">
								<fmt:message key="label.learning.not.available" />
							</c:when>
							<c:otherwise>
								<c:out value="${mark.comments}" escapeXml="false" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row no-gutter">
				<div class="col-md-2"><fmt:message key="label.learning.marks" /></div>
					<div class="col-md-10">
						<c:choose>
							<c:when test="${mark == null}">
								<fmt:message key="label.learning.not.available" />
							</c:when>
							<c:otherwise>
								<fmt:formatNumber type="number" maxFractionDigits="<%= SpreadsheetConstants.MARK_NUM_DEC_PLACES %>" value="${mark.marks}"/>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				</div>
			</div>
			</div>
		</div>
		</c:if>		

		
		<div class="row no-gutter">
			<div class="col-12">
				<div class="panel panel-default">
					<div class="panel-body">
					<iframe
						id="externalSpreadsheet" name="externalSpreadsheet" src="<lams:WebAppURL/>includes/javascript/simple_spreadsheet/spreadsheet_offline.html?lang=${language}"
						style="width:99%;" frameborder="no" height="385px"
						scrolling="no">
						</iframe>
				
					<c:if test="${!userIsMarked && (mode != 'teacher') && (spreadsheet.learnerAllowedToSave) && !(sessionMap.lockOnFinish && sessionMap.userFinished)}">
						<div class="space-bottom-top align-right">
							<button name="SaveButton" onclick="return saveUserSpreadsheet('saveUserSpreadsheet')" class="btn btn-primary voffset10 pull-right">
								<fmt:message key="label.save" />
							</button>
						</div>
					</c:if>		
					</div>
				</div>
			</div>
		</div>


		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="row no-gutter">
				<div class="col-12">
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<fmt:message key="title.reflection" />
						</div>
						<div class="panel-body">
							<div class="reflectionInstructions">
								<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
							</div>
							<div class="panel">
								<lams:out value="${QaLearningForm.entryText}" escapeHtml="true" />
							</div>

							<c:if test="${hasEditRight}">
								<button name="forwardtoReflection" class="btn btn-default pull-left"
									onclick="submitMethod('forwardtoReflection');">
									<fmt:message key="label.edit" />
								</button>
							</c:if>
							
							<c:choose>
								<c:when test="${empty sessionMap.reflectEntry}">
									<p><em> <fmt:message key="message.no.reflection.available" /></em>	</p>
								</c:when>
								<c:otherwise>
									<p>	<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}"/> </p>
								</c:otherwise>
							</c:choose>

							<c:if test="${mode != 'teacher'}">
								<button name="FinishButton" onclick="return continueReflect()" class="btn btn-default voffset10 pull-left">
								<fmt:message key="label.edit" />
								</button>
							</c:if>
							
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">				
						<button name="FinishButton" onclick="return continueReflect()" class="btn btn-primary pull-right">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<a href="#nogo" class="btn btn-primary pull-right na" id="finishButton" onclick="return finishSession()">
							<c:choose>
			 					<c:when test="${sessionMap.isLastActivity}">
			 						<fmt:message key="label.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="label.finished" />
			 					</c:otherwise>
			 				</c:choose>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
