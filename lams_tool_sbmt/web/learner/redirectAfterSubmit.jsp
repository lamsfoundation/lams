<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<!-- Triggers redirection to the learner page after a file is submitted, to stop Refresh reloading the file. -->

<lams:html>

<lams:head>
<%@ include file="/common/header.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	$(document).ready(function(){
		window.location.href = '<lams:WebAppURL />/learner.do?method=refresh&sessionMapID=${sessionMapID}';
	});
</script>
</lams:head>

<body class="stripes">
<lams:Page type="admin">
	<div class="text-center" style="margin-top: 10px; margin-bottom: 15px;">
		<i class="fa fa-2x fa-refresh fa-spin text-primary"></i>
	</div>
</lams:Page>	 
</body>

</lams:html>
