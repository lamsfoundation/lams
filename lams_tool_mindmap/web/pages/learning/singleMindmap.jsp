<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
		
<lams:html>
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		<c:if test="${printMode}">
			<title><fmt:message key='activity.title'/>&nbsp;<fmt:message key='monitoring.label.group'/>&nbsp;"<c:out value="${sessionName}" />"</title>
		</c:if>
		<lams:css/>
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		<c:choose>
			<c:when test="${printMode}">
				<style>
					#mindmap-container {
						height: 750px;
						width: 1150px;
						border: none !important;
					}
					
					#mindmap-controls {
						display: none;
					}
					
					#print-button {
						margin: 20px 0 0 20px;
					}
										
					#print-header {
						text-align: center;
					}
					
					@media print {
		               #print-button {
		                  display: none;
		               }
		            }
				</style>
			</c:when>
			<c:otherwise>
				<style>
					#mindmap-container {
						height: calc(100vh - 40px);
						border: none;
					}
				</style>
			</c:otherwise>
		</c:choose>

		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
		<script src="${lams}includes/javascript/fullscreen.js"></script>
		<script src="${tool}includes/javascript/jquery.timer.js"></script>
		<script src="${tool}includes/javascript/mapjs/main.js"></script>
		<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
	</lams:head>

	<body>	
		<c:if test="${printMode}">
			<button class="btn btn-primary" id="print-button" onClick="javascript:window.print()"><fmt:message key='button.print' /></button>
		</c:if>
		<%-- MindMap ---------------------------------------%>
		<%@ include file="/common/mapjs.jsp"%>
		<%-- End MindMap -----------------------------------%>
   </body>
</lams:html>