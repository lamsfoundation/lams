<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body class="stripes">
 		<script type="text/javascript">
			var reqIDVar = new Date();
			window.location.href  = "${tool}/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
		</script>
		
		<lams:Page type="learner">
		<div style="align:center">
			<!--  this should never be seen! -->
			<a href="${tool}/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime()" type="button" class="btn btn-primary">
					<i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.check.for.new" />
			</a>
		</div>
		</lams:Page>
	</body>
</lams:html>
