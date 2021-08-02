<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
		
<lams:html>
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		
		<lams:css/>
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		<style>
			#mindmap-container {
				height: calc(100vh - 40px);
				border: none;
			}
		</style>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
		<script src="${lams}includes/javascript/fullscreen.js"></script>
		<script src="${tool}includes/javascript/jquery.timer.js"></script>
		<script src="${tool}includes/javascript/mapjs/main.js"></script>
		<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
	</lams:head>

	<body>	
		<%-- MindMap ---------------------------------------%>
		<%@ include file="/common/mapjs.jsp"%>
		<%-- End MindMap -----------------------------------%>
   </body>
</lams:html>