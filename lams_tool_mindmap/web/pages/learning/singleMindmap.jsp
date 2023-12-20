<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
		
<lams:html>
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		<c:if test="${printMode}">
			<title>
				<fmt:message key='activity.title'/>
				<c:if test="${not empty sessionName}">
					&nbsp;<fmt:message key='monitoring.label.group'/>&nbsp;"<c:out value="${sessionName}" />"
				</c:if>
				<c:if test="${not empty currentMindmapUser}">
					&nbsp;<fmt:message key='node.learner.label'/>&nbsp;"<c:out value="${currentMindmapUser}" />"
				</c:if>
			</title>
		</c:if>

        <link rel="stylesheet" href="<lams:LAMSURL/>learning/css/components-learner.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
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

        <script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
		<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
		<script src="${lams}includes/javascript/fullscreen.js"></script>
		<script src="${tool}includes/javascript/jquery.timer.js"></script>
		<script src="${tool}includes/javascript/mapjs/main.js"></script>
		<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
	</lams:head>

	<body>	
		<c:if test="${printMode}">
			<button type="button" class="btn btn-primary" id="print-button" onClick="javascript:window.print()">
				<fmt:message key='button.print' />
			</button>
		</c:if>
		<%-- MindMap ---------------------------------------%>
		<%@ include file="/common/mapjs.jsp"%>
		<%-- End MindMap -----------------------------------%>
   </body>
</lams:html>