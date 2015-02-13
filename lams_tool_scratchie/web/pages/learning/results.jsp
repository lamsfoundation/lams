<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
	<style media="screen,projection" type="text/css">
		#reflections-div {
			padding: 80px 0 20px;
		}
		.ui-jqgrid tr.jqgrow td {
		    white-space: normal !important;
		    height:auto;
		    vertical-align:text-top;
		    padding-top:2px;
		}
	</style>

 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			<!-- Display reflection entries -->
			jQuery("#reflections").jqGrid({
				datatype: "local",
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:['#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
					    "<fmt:message key='label.learners.feedback' />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:140},
			   		{name:'feedback', index:'feedback', width:568}
			   	],
			   	caption: "<fmt:message key='label.other.groups' />"
			});
		    <c:forEach var="reflectDTO" items="${reflections}" varStatus="i">
		    	jQuery("#reflections").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${reflectDTO.groupName}",
			   	    feedback:"<lams:out value='${reflectDTO.reflection}' escapeHtml='true' />"
		   	   	});
		    </c:forEach>
		    
			//jqgrid autowidth (http://stackoverflow.com/a/1610197)
			$(window).bind('resize', function() {
				var grid;
			    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
			    	grid.each(function(index) {
			        	var gridId = $(this).attr('id');
			        	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
			        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
			    	});
			    }
			});
		    
		})
	
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function editBurningQuestions(){
			document.location.href='<c:url value="/learning/showBurningQuestions.do?sessionMapID=${sessionMapID}"/>';
		}
    </script>
</lams:head>

<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${scratchie.title}"/>
		</h1>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<h3>
			<fmt:message key="label.score" />
		</h3>
		
		<h3>
			<fmt:message key="label.you.ve.got" >
				<fmt:param>${score}%</fmt:param>
			</fmt:message>
		</h3>
		
		<c:if test="${sessionMap.isBurningQuestionsEnabled}">
			<div class="small-space-top">
				<h3><fmt:message key="label.burning.questions" />:</h3>
				
				<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
					<table>
						<tr>
							<td width="30%">
								<c:out value="${item.title}" escapeXml="true"/>
							</td>						
							<td>
								<c:choose>
									<c:when test="${empty item.burningQuestion}">
										-
									</c:when>
									<c:otherwise>
										<c:out value="${item.burningQuestion}" escapeXml="true"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</table>
				</c:forEach>

				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return editBurningQuestions()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${sessionMap.reflectOn}">
			<div class="small-space-top">
				<h3><fmt:message key="monitor.summary.td.notebookInstructions" />:</h3>
				<p>
					<strong><lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/></strong>
				</p>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message key="label.your.answer" />
						</p>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
				
				<c:if test="${fn:length(reflections) > 0}">
					<div id="reflections-div">
						<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
		        </c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<html:link href="#nogo" property="finishButton" styleId="finishButton" onclick="return finishSession()" styleClass="button">
					<span class="nextActivity">
						<c:choose>
							<c:when test="${sessionMap.activityPosition.last}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</span>
				</html:link>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
