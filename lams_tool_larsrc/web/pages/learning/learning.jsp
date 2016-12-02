<!DOCTYPE html>
        

<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="resource" value="${sessionMap.resource}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
	
	<script type="text/javascript">
	
		$(document).ready(function(){
			cancel();
		});

 		function submitResourceForm() {
 			debugger;
			if ( $(this).valid() ) {
				var formData = new FormData(this);
			    $.ajax({ // create an AJAX call...
			        data: formData, 
			        processData: false, // tell jQuery not to process the data
			        contentType: false, // tell jQuery not to set contentType
			        type: $(this).attr('method'), // GET or POST
			        url: $(this).attr('action'), // the file to call
			        success: function (response) {
						$('#addresource').html(response);
						if ( itemType == 1)
							setFormURL();
						else if ( itemType == 2)
							setFormFile();
			    	},
			    	error: function (jqXHR, textStatus, errorThrown ) {
			    		alert(textStatus+": "+errorThrown);
			    	},
			    });
			}
			return false;
		}
		
	    function gotoURL(){
	    	var reqIDVar = new Date();
	   		var gurl = "<c:url value="/learning/addurl.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	   		$.ajaxSetup({ cache: true });
	        $("#addresource").load(gurl, function() {
	        	setFormURL();
	        });
		}
	    function setFormURL() {
			$("#itemType").val("1");
			$("#mode").val("${mode}");
			$("#sessionMapID").val("${sessionMapID}");
	    }
	    
		function gotoFile(){
		    var reqIDVar = new Date();
		    var gurl = "<c:url value="/learning/addfile.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
		    $.ajaxSetup({ cache: true });
	        $("#addresource").load(gurl, function() {
	        	setFormFile();
	        });
		}		
		function setFormFile() {
			$("#itemType").val("2");
			$("#mode").val("${mode}");
			$("#sessionMapID").val("${sessionMapID}");
		}
		function cancel(){
			$("#addresource").html('');
		}		
		
		function checkNew(){
 		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();				
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		    </script>
</lams:head>
<body class="stripes">


	<lams:Page type="learner" title="${resource.title}">
	
		<!--  Warnings -->
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher' and (resource.allowAddFiles or resource.allowAddUrls) }">
			<lams:Alert type="danger" id="warn-lock" close="false">
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

		<%@ include file="/common/messages.jsp"%>

		<!--  Instructions -->
		<div class="panel">
			<c:out value="${resource.instructions}" escapeXml="false"/>
		</div>

		<!-- Resources to View -->
		<div class="panel panel-default">
			<div class="panel-heading panel-title">
				<fmt:message key="label.resoruce.to.review" />

				<!--  Panel button bar controlling refresh and adding items -->
				<div class="btn-group pull-right">
					<c:if test="${mode != 'teacher'}">
						<a href="#" onclick="javascript:return checkNew()" type="button" class="btn btn-xs btn-default">
						<i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.check.for.new" /></a>
					</c:if>
					<c:if test="${not finishedLock}">
						<c:choose>
							<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoURL()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.url.input" /></a>
								<a href="#" onclick="javascript:gotoFile()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.file.input" /></a>
							</c:when>
	
							<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoFile()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.file.input" /></a>
							</c:when>
	
							<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
								<a href="#" onclick="javascript:gotoURL()" type="button" class="btn btn-xs btn-default">
								<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.authoring.basic.resource.url.input" /></a>
							</c:when>
						</c:choose>
					</c:if>
				</div>
				<!--  End panel button bar -->
			</div> 

			<table class="table table-hover table-striped table-condensed">
			<tr>
				<th width="70%">
					<fmt:message key="export.label.resource" />
				</th>
				<th class="text-center">
					<fmt:message key="label.completed" />
				</th>
			</tr>
			<c:forEach var="item" items="${sessionMap.resourceList}">
				<tr>
					<td>
						<a href="#" onclick="viewItem(${item.uid})">
							<c:out value="${item.title}" escapeXml="true"/></a>
						
						<c:if test="${!item.createByAuthor && item.createBy != null}">
								(<c:out value="${item.createBy.firstName} ${item.createBy.lastName}" escapeXml="true"/>)
						</c:if>
					</td>
					<td class="text-center">
						<c:choose>
							<c:when test="${item.complete}">
								<i class="fa fa-check"></i>
							</c:when>
							<c:otherwise>
								<i class="fa fa-minus"></i>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>

			<c:if test="${resource.miniViewResourceNumber > 0}">
				<tr>
					<td colspan="3" align="left">
						<b>${resource.miniViewNumberStr}</b>
					</td>
				</tr>
			</c:if>
			</table>

		</div>
		<!--  End Resources to View -->

		<!-- Add a URL/File Form-->
		<div id="addresource">
		</div>

		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="reflectionInstructions">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

					<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> 
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
					</c:choose>

					<c:if test="${mode != 'teacher'}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-sm btn-default voffset5">
						<fmt:message key="label.edit" />
						</html:button>
					</c:if>
				</div>
			</div>
		</c:if>
		<!-- End Reflection -->

		<c:if test="${mode != 'teacher'}">
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton"
							onclick="return continueReflect()" styleClass="btn btn-default voffset5 pull-right">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
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
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>
</body>
</lams:html>
