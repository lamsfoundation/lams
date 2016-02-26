<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
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
	<c:when test="${empty sessionMap.defineCompleted}">
		<c:set var="defineCompleted" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="defineCompleted" value="${sessionMap.defineCompleted}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty sessionMap.completed}">
		<c:set var="completed" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="defineCompleted" value="${sessionMap.defineCompleted}" />
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
		<title><c:out value="${instructions.title}" escapeXml="true"/></title>

		<%@ include file="/common/header.jsp"%>
		
		<%-- param has higher level for request attribute --%>
		<c:if test="${not empty param.mode}">
			<c:set var="mode" value="${param.mode}" />
		</c:if>
		<script language="JavaScript" type="text/JavaScript">
		<!--
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
					        window.parent.location.href='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}&toolContentID=${toolContentID}"/>';
					   }else{
						    var url="<c:url value="/learning/completeItem"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolContentID=${toolContentID}&itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
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
				
			
				function nextIns(currIns){
					document.location.href="<c:url value='/nextInstruction.do'/>?mode=${mode}&insIdx=" + currIns + "&sessionMapID=${sessionMapID}&itemUid=${param.itemUid}&itemIndex=${param.itemIndex}";
				}
			
			function hideButton(){
				button = document.getElementById('FinishInstruction');
				button.style.visibility = 'hidden';
			}
			
			function showButton(){
				button = document.getElementById('FinishInstruction');
				button.style.visibility = 'visible';
			}
	
		//-->
		</script>
	</lams:head>
	<body>

		<div id="instructions">
			
			
		<span class="right-buttons">
		
		<c:choose>
				<c:when test="${reflectOn && runAuto}">
					<input type="button" id="FinishInstruction" name="FinishInstruction"
						onClick="javascript:continueReflect()" class="button" value="<fmt:message
							key='label.continue' />" />
				</c:when>
				<c:otherwise>
					<input type="button" id="FinishInstruction" name="FinishInstruction" class="button" value="<fmt:message key='label.finish' />" />
				</c:otherwise>
			</c:choose>
		
			</span>
<c:choose>
		<c:when test="${defineCompleted && !completed}">
			<script type="text/javascript" language="JavaScript">
       			<!--
       			  hideButton();
       			  //-->
     	 	</script>
		</c:when>
</c:choose>
			<p>

				<c:choose>
					<c:when test="${instructions.instruction == null}">
						<fmt:message key="msg.no.instruction" />
					</c:when>
					<c:otherwise>
					<c:out value="${instructions.instruction}" escapeXml="false"/>
				</c:otherwise>
				</c:choose>
			</p>
		</div>
	</body>
</lams:html>
