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
	<c:when test="${empty sessionMap.reflectOn}">
		<c:set var="reflectOn" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="reflectOn" value="${sessionMap.reflectOn}" />
	</c:otherwise>
</c:choose>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.mode}">
	<c:set var="mode" value="${param.mode}" />
</c:if>

<lams:html>
	<lams:head>
		<title><c:out value="${instructions.title}" escapeXml="true"/></title>

		<%@ include file="/common/mobileheader.jsp"%>
		<style media="screen,projection" type="text/css">
			.ui-mobile .ui-page {min-height: 10px !important;}
			.float-right {padding-top: 15px;}
			h2, p {text-align:left !important; margin-left: 5px !important;}
			p {min-height: 26px; font-size: 13px}
		</style>
		
		<script language="JavaScript" type="text/JavaScript">
		
				$(document).bind('pageinit', function(){
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
								dataType: 	'script',
								url:    url,
								timeout: 5000,

								beforeSend:  function() {
									// disable button
									jQuery("input#FinishInstruction").attr("disabled", true);
									jQuery("input#FinishInstruction").removeClass("button");
									jQuery("input#FinishInstruction").addClass("disabled");
								},

								error: function(jqXHR, textStatus, errorThrown) {
									alert('Error while marking item as complete.\nStatus: ' + textStatus + '\nError: ' + errorThrown);
								},

								success:  function() {
									var winParent = window.parent;
									if (!winParent.opener.checkNew) {
										// there can be an extra iframe in the hierarchy
										winParent = winParent.parent;
									}
									winParent.opener.checkNew();
									winParent.opener=null;
									winParent.close();
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
		</script>
	</lams:head>
	<body>
	
		<div data-role="page" data-cache="false">
		
			<div data-role="header" data-theme="b" data-nobackbtn="true">
			
				<span class="float-right">
					<c:choose>
						<c:when test="${instructions.current < instructions.total}">
							<input type="button" id="NextInstruction" name="NextInstruction"
								onClick="javascript:nextIns(${instructions.current})"
								value="<fmt:message key='label.next.instruction' />" data-inline="true" data-icon="arrow-r"/>
						</c:when>
						<c:when test="${reflectOn && runAuto}">
							<input type="button" id="FinishInstruction" name="FinishInstruction"
								onClick="javascript:continueReflect()" value="<fmt:message
									key='label.continue' />" data-inline="true" data-icon="arrow-r"/>
						</c:when>
						<c:otherwise>
							<input type="button" id="FinishInstruction" name="FinishInstruction" value="<fmt:message key='label.finish' />" data-inline="true" data-icon="arrow-r"/>
						</c:otherwise>
					</c:choose>
				</span>
			
				<h2>
					<c:if test="${instructions.total > 0}" >
						<fmt:message key="message.step.of">
							<fmt:param value="${instructions.current}" />
							<fmt:param value="${instructions.total}" />
						</fmt:message>
					</c:if>
					<c:if test="${instructions.total <= 0}" >
						&nbsp;
					</c:if>
				</h2>
				
				<p>
					<c:choose>
						<c:when test="${instructions.instruction == null}">
							<fmt:message key="msg.no.instruction" />
						</c:when>
						<c:otherwise>
						<c:out value="${instructions.instruction.description}" escapeXml="false"/>
					</c:otherwise>
					</c:choose>
				</p>
			</div>
			
				<div data-role="content">
				</div>

		</div>
	</body>
</lams:html>
