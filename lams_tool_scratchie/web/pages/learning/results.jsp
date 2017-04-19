<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
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
	
	<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
	<link rel="stylesheet" href="<lams:LAMSURL />/includes/font-awesome/css/font-awesome.min.css">
	<style media="screen,projection" type="text/css">
		#reflections-div {
			padding: 10px 0 20px;
		}
		.burning-question-dto {
			padding-bottom: 5px; 
		}
		.ui-jqgrid tr.jqgrow td {
		    white-space: normal !important;
		    height:auto;
		    vertical-align:text-top;
		    padding-top:2px;
		}
		.ui-jqgrid tr.jqgrow td {
			vertical-align:middle !important
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
	<script type="text/javascript">
		function likeEntry(scratchieItemUid, rowid, burningQuestionUid) {
			
			var jqGrid = $("#burningQuestions" + scratchieItemUid);
			var likeCell = jqGrid.jqGrid('getCell', rowid, 'like');
			var isLike = likeCell.includes( 'fa-thumbs-o-up' );

			if (isLike) {
				$.ajax({
				    url: '<c:url value="/learning/like.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {	       		
		    		if ( response.added ) {
		    			//update 'count' column
		  				var currentCount = eval(jqGrid.jqGrid('getCell', rowid, 'count'));
		  				jqGrid.jqGrid('setCell', rowid, 'count', currentCount + 1);
		  				
		  				//update 'like' column
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-thumbs-o-up", "fa-thumbs-up"));
		  				
					} else {
						alert("Error");
					}
				});
				
			} else {
				$.ajax({
				    url: '<c:url value="/learning/removeLike.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {
			    	if ( response.added ) {
			    		//update 'count' column
		  				var currentCount = eval(jqGrid.jqGrid('getCell', rowid, 'count'));
		  				jqGrid.jqGrid('setCell', rowid, 'count', currentCount - 1);
		  				
		  				//update 'like' column
		  				jqGrid.jqGrid('setCell',rowid,'like', likeCell.replace("fa-thumbs-up", "fa-thumbs-o-up"));
			       		
		  			} else {
						alert("Error");
					}
				});
			}

		}
	
		$(document).ready(function(){
			
			<!-- Display burningQuestionItemDtos -->
			<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
				<c:set var="scratchieItem" value="${burningQuestionItemDto.scratchieItem}"/>
			
				jQuery("#burningQuestions${scratchieItem.uid}").jqGrid({
					datatype: "local",
					rowNum: 10000,
					height: 'auto',
					autowidth: true,
					shrinkToFit: false,
				   	colNames:[
						'#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
						"<fmt:message key='label.burning.questions' />",
						"<fmt:message key='label.like' />",
						"<fmt:message key='label.count' />"
					],
				   	colModel:[
				   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
				   		{name:'groupName', index:'groupName', width:200},
				   		{name:'feedback', index:'feedback', width:401},
				   		{name:'like', index:'like', width:60, align: "center"},
				   		{name:'count', index:'count', width:50, align:"right"}
				   	],
				   	caption: "${scratchieItem.title}"
				});
				
			    <c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}" varStatus="i">			    
			    	jQuery("#burningQuestions${scratchieItem.uid}").addRowData(${i.index + 1}, {
			   			id:"${i.index + 1}",
			   	     	groupName:"${burningQuestionDto.sessionName}",
				   	    feedback:"${burningQuestionDto.escapedBurningQuestion}",
				   	 	<c:choose>
				   			<c:when test="${!isUserLeader && burningQuestionDto.userLiked}">
				   				like:'<span class="fa fa-thumbs-up fa-2x"></span>',
				   			</c:when>
					   		<c:when test="${!isUserLeader}">
				   				like:'',
				   			</c:when>
							<c:when test="${burningQuestionDto.userLiked}">
								like:'<span class="fa fa-thumbs-up fa-2x" title="<fmt:message key="label.unlike"/>"' +
										'onclick="javascript:likeEntry(${scratchieItem.uid}, ${i.index + 1}, ${burningQuestionDto.burningQuestion.uid});" />',
							</c:when>
							<c:otherwise>
								like:'<span class="fa fa-thumbs-o-up fa-2x" title="<fmt:message key="label.like"/>"' +
										'onclick="javascript:likeEntry(${scratchieItem.uid}, ${i.index + 1}, ${burningQuestionDto.burningQuestion.uid});" />',
							</c:otherwise>
						</c:choose>
				   	 	count:'${burningQuestionDto.likeCount}'
			   	   	});
		        </c:forEach>

		        jQuery("#burningQuestions${scratchieItem.uid}").jqGrid('sortGrid','groupName', false, 'asc');
	        </c:forEach>
			
			<!-- Display reflection entries -->
			jQuery("#reflections").jqGrid({
				datatype: "local",
				rowNum: 10000,
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
			
			// trigger the resize when the window first opens so that the grid uses all the space available.
			setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
		    
		})
	
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function editBurningQuestions() {
			document.location.href='<c:url value="/learning/showBurningQuestions.do?sessionMapID=${sessionMapID}"/>';
		}
		function refresh() {
			location.reload();
			return false;
		}
    </script>
</lams:head>

<body class="stripes">

	<c:set var="title">
		${scratchie.title} / <fmt:message key='label.score' />
	</c:set>
	<lams:Page type="learner" title="${title}">

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert id="submissionDeadline">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<lams:Alert id="score" type="info" close="false">
			<fmt:message key="label.you.ve.got">
				<strong><fmt:param>${score}%</fmt:param></strong>
			</fmt:message>
		</lams:Alert>
	
		<div class="row voffset5" >
				<html:button property="refreshButton" onclick="return refresh();" styleClass="btn btn-default voffset5 roffset5 pull-right">
					<fmt:message key="label.refresh" />
				</html:button>
		</div>

		<!-- Display burningQuestionItemDtos -->

		<c:if test="${sessionMap.isBurningQuestionsEnabled}">
			<div class="voffset5">
				<div class="lead">
					<fmt:message key="label.burning.questions" />
				</div>

				<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
					<div class="burning-question-dto">
						<table id="burningQuestions${burningQuestionItemDto.scratchieItem.uid}" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
				</c:forEach>

				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return editBurningQuestions()" styleClass="btn btn-sm btn-default">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${sessionMap.reflectOn}">
			<div class="voffset10">
				<div class="panel panel-default">
					<div class="panel-heading-sm  bg-success">
						<fmt:message key="monitor.summary.td.notebookInstructions" />
					</div>
					<div class="panel-body-sm">
						<div class="panel">
							<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
						</div>
						<c:choose>
							<c:when test="${empty sessionMap.reflectEntry}">
								<p>
									<fmt:message key="message.no.reflection.available" />
								</p>
							</c:when>
							<c:otherwise>
								<div class="panel-body-sm bg-warning">
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</div>
							</c:otherwise>
						</c:choose>
						<c:if test="${(mode != 'teacher') && isUserLeader}">
							<div class="voffset5">
								<html:button property="finishButton" onclick="return continueReflect()" styleClass="btn btn-sm btn-default">
									<fmt:message key="label.edit" />
								</html:button>
							</div>
						</c:if>

					</div>
				</div>

				<c:if test="${fn:length(reflections) > 0}">
					<div id="reflections-div">
						<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="voffset10 pull-right">
				<html:link href="#nogo" property="finishButton" styleId="finishButton" onclick="return finishSession()"
					styleClass="btn btn-primary na">
					<c:choose>
						<c:when test="${sessionMap.activityPosition.last}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</html:link>
			</div>
		</c:if>

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>
