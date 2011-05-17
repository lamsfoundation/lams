<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link rel="stylesheet" href="${lams}/css/thickbox.css" type="text/css" media="screen">
<link type="text/css" href="${lams}/css/jquery-ui-1.8.11.flick-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui-timepicker-addon.css" rel="stylesheet">

<script type="text/javascript">
<!--
	var tb_pathToImage = "${lams}/images/loadingAnimation.gif";
//-->	
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-1.8.11.custom.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/thickbox-compressed.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
<!--
	var evalcomixWindow = null;
	
	function openEvalcomixWindow(url)
	{
    	evalcomixWindow=window.open(url,'evalcomixWindow','width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}


	$(function(){
		$("#datetime").datetimepicker();

		var submissionDeadline = '${submissionDeadline}';
		if (submissionDeadline != "") {
			var date = new Date(eval(submissionDeadline));

			$("#dateInfo").html( formatDate(date) );
			
			//open up date restriction area
			toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'),'${lams}');
		
		}
		
	});

	function formatDate(date) {
		var currHour = "" + date.getHours();
		if (currHour.length == 1) {
			currHour = "0" + currHour;
		}  
		var currMin = "" + date.getMinutes();
		if (currMin.length == 1) {
			currMin = "0" + currMin;
		}
		return $.datepicker.formatDate( 'mm/dd/yy', date ) + " " + currHour + ":" + currMin;
	}

	function setSubmissionDeadline() {
		//get the timestamp in milliseconds since midnight Jan 1, 1970
		var date = $("#datetime").datetimepicker('getDate');
		if (date == null) {
			return;
		}

		var reqIDVar = new Date();
		 var url = "<c:url value="/monitoring.do"/>?dispatch=setSubmissionDeadline&toolContentID=${param.toolContentID}&submissionDeadline="
					+ date.getTime() + "&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI('<fmt:message key="monitor.summary.notification" />', '<fmt:message key="monitor.summary.date.restriction.set" />');
				$("#datetimeDiv").hide();
				$("#dateInfo").html(formatDate(date) );
				$("#dateInfoDiv").show();
			}
		});
	}
	function removeSubmissionDeadline() {
		var reqIDVar = new Date();
        var url = "<c:url value="/monitoring.do"/>?dispatch=setSubmissionDeadline&toolContentID=${param.toolContentID}&submissionDeadline="
        			+ "&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI('<fmt:message key="monitor.summary.notification" />', '<fmt:message key="monitor.summary.date.restriction.removed" />');
				$("#dateInfoDiv").hide();
				
				$("#datetimeDiv").show();
				$("#datetime").val("");
			}
		});
	}	
	
//-->
</script>

<c:set var="dto" value="${notebookDTO}" />

<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.lockOnFinish}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowRichEditor" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowRichEditor}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</div>

<%@include file="daterestriction.jsp"%>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<h2>
			${session.sessionName}
		</h2>
	</c:if>

	<table cellpadding="0">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalLearners" />
			</td>
			<td width="70%">
				${session.numberOfLearners}
			</td>
		</tr>
	</table>

	<table cellpadding="0">

		<tr>
			<th>
				<fmt:message key="heading.learner" />
			</th>
			<th>
				<fmt:message key="heading.notebookEntry" />
			</th>
		</tr>


		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td width="30%">
					${user.firstName} ${user.lastName}
				</td>
				<td width="70%">
					<c:choose>
						<c:when test="${user.entryUID == null}">
							<fmt:message key="label.notAvailable" />
						</c:when>

						<c:otherwise>
							<a href="./monitoring.do?dispatch=showNotebook&amp;userUID=${user.uid}&keepThis=true&TB_iframe=true&height=260&width=800" class="thickbox" title="<fmt:message key='heading.notebookEntry' />">
								<fmt:message key="label.view" /> 
							</a>
						</c:otherwise>
					</c:choose>

				</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
