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

<lams:html>
	<lams:head>
		<%-- param has higher level for request attribute --%>
		<c:if test="${not empty param.mode}">
			<c:set var="mode" value="${param.mode}" />
		</c:if>
		<script type="text/JavaScript">
 				function finishIns(){
				//learner and author(preview mode) will mark the finish
					if(${mode == "learner"} || ${mode == "author"}){
					   var reqIDVar = new Date();
					   if(${runAuto}) {
					  		//set complete flag and finish this activity as well.
					        location.href='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}"/>';
					   }else{
						    var url="<c:url value="/learning/completeItem"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
							$.ajax({
								type:   'GET',
								dataType: 	'script',
								url:    url,
								timeout: 5000,

								beforeSend:  function() {
									// disable button
									$("input#FinishInstruction").attr("disabled", true);
									$("input#FinishInstruction").removeClass("button");
									$("input#FinishInstruction").addClass("disabled");
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
									$("input#FinishInstruction").attr("disabled", false);
									$("input#FinishInstruction").removeClass("disabled");
									$("input#FinishInstruction").addClass("button");
								}
							});
							
					   }
					} else {
						window.close();
					}
				}
				
				function continueReflect(){
					 location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
				}
		</script>
	</lams:head>
	<body>

		<div class="container-fluid" id="instructions">
		
		<div class="row">
			<div class="col-xs-12 ">
				<c:out value="${instructions}" escapeXml="false"/>
			
				<c:choose>
					<c:when test="${reflectOn && runAuto}">
						<input type="button" id="Reflect" name="Reflect"
							onClick="javascript:continueReflect()" class="btn btn-sm btn-default pull-right" value="<fmt:message
								key='label.continue' />" />
					</c:when>
					<c:when test="${! reflectOn && runAuto}">
						<a href="#nogo" name="FinishInstruction" id="FinishInstruction"
							onclick="javascript:finishIns()" class="btn btn-sm btn-primary voffset5 pull-right na">
							<span class="nextActivity">
								<fmt:message key='label.finish' />
							</span>
						</a>
					</c:when>
					<c:otherwise>
						<input type="button" onClick="javascript:finishIns()" id="FinishInstruction" name="FinishInstruction" 
							class="btn btn-sm btn-default pull-right" 
							value="<fmt:message key='label.finish' />" />	
					</c:otherwise>
				</c:choose>
			</div>
		</div> 
	</div>
	
	</body>
</lams:html>
