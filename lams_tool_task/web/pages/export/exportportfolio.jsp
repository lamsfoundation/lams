<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="taskSummaryList" value="${sessionMap.taskSummaryList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="export.title" /></title>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<script type="text/javascript">
		function launchPopup(url,title) {
			var wd = null;
			if(wd && wd.open && !wd.closed){
				wd.close();
			}
			wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
			wd.window.focus();
		}
	</script>
	<lams:css localLinkPath="../"/>
</lams:head>
<body class="stripes">


	<div id="content">

	<h1>${title} </h1>

		<table border="0" cellspacing="3" width="98%">
			<c:forEach var="taskSummary" items="${taskSummaryList}">
				<tr>
					<td>
						<%@ include file="summaryTask.jsp"%>
					</td>
				</tr>
			</c:forEach>
		</table>

	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->

</body>
</lams:html>
