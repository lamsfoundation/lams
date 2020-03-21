<!DOCTYPE html>      
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${not empty param.mode}">
	<c:set var="mode" value="${param.mode}" />
</c:if>
<%-- runAuto and reflectOn does not set when  authoring preview mode then set false as default value --%>
<c:choose>
	<c:when test="${empty sessionMap.runAuto}">
		<c:set var="runAuto" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="runAuto" value="${sessionMap.runAuto}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty sessionMap.reflectOn}">
		<c:set var="reflectOn" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="reflectOn" value="${sessionMap.reflectOn}" />
	</c:otherwise>
</c:choose>

<lams:html>
	<lams:head>
		<title><c:out value="${instructions.title}"/></title>

		<%@ include file="/common/header.jsp"%>
		
		<script type="text/JavaScript">
			jQuery.noConflict();
		
		  	jQuery(document).ready(function() {

				jQuery("input#FinishInstruction").bind('click', function() {
					finishIns();
				});	

		  	});
		
			function finishIns(){
					//learner and author(preview mode) will mark the finish
					if(${mode == "learner"} || ${mode == "author"}){
					   var reqIDVar = new Date();
					   if(${runAuto}){
					  		//set complete flag and finish this activity as well.
					        window.parent.location.href='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}"/>';
					   }else{
						    var url="<c:url value="/learning/completeItem"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
							jQuery.ajax({
								type:   'GET',
								dateType: 	'script',
								url:    url,
								timeout: 5000,

								beforeSend:  function() {
									// disable button
									jQuery("input#FinishInstruction").attr("disabled", true);
									jQuery("input#FinishInstruction").removeClass("button");
									jQuery("input#FinishInstruction").addClass("disabled");
								},

								error: function() {
									alert('server timeout');
								},

								success:  function(data) {
									eval(data);
								},

								complete:  function() {
									//enable button
									jQuery("input#FinishInstruction").attr("disabled", false);
									jQuery("input#FinishInstruction").removeClass("disabled");
									jQuery("input#FinishInstruction").addClass("button");
								}
							});
							
					   }
					} else {
						window.parent.close();
					}
			}
				
			function continueReflect(){
				 window.parent.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
			}
		</script>
	</lams:head>
	<body>

		<div class="container-fluid" id="instructions">
		  <div class="row">
			<div class="col-12 ">
				<c:choose>
					<c:when test="${reflectOn && runAuto}">
						<input type="button" id="FinishInstruction" name="FinishInstruction"
							onClick="javascript:continueReflect()" class="btn btn-primary pull-right roffset5" value="<fmt:message key='label.continue' />" />
					</c:when>
					<c:otherwise>
						<input type="button" id="FinishInstruction" name="FinishInstruction" class="btn btn-primary pull-right roffset5" value="<fmt:message key='label.finish' />" />
					</c:otherwise>
				</c:choose>
			</div>
		  </div>
		</div>
	
	</body>
</lams:html>
