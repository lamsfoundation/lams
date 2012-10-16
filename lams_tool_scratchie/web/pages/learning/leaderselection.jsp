<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet" />
	<style type="text/css">
    	.dialog{display: none;}
    	.ui-widget-overlay{opacity:0.7;}
    </style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">
		<!--
		$(function() {
	
			$("#leaderSelectionDialog").dialog({
				bgiframe: true,
				autoOpen: ${!nodialog},
				height: 350,
				width: 350,
				modal: true,
				buttons: {
					'<fmt:message key="label.yes.become.leader" />': function() {
				        $.ajax({
				        	async: false,
				            url: '<c:url value="/learning/becomeLeader.do"/>',
				            data: 'toolSessionID=${toolSessionID}',
				            type: 'post',
				            success: function (json) {
				            	refresh();
				            }
				       	});
					},
					
					'<fmt:message key="label.no" />': function() {
						setTimeout("refresh();",30000);// Auto-Refresh every 30 seconds
						$(this).dialog('close');
					}
				}
			});
		});
		
		function refresh() {
			var newHref = (location.href.indexOf("nodialog=true") == -1) ? location.href + "&nodialog=true" : location.href;
			location.href = newHref;
		}
		
		//refresh page if not showing dialog
		if (${nodialog}) {
			setTimeout("refresh();",30000);
		}
		
		-->        
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			${scratchie.title}
		</h1>
		
		<h2>
			<fmt:message key="label.waiting.for.leader" />
		</h2>

		<div>
			<fmt:message key="label.users.from.group" />
		</div>
		
		<div>
			<c:forEach var="user" items="${groupUsers}" varStatus="status">
				<div>
					${user.firstName} ${user.lastName}
				</div>
			</c:forEach>
		</div>
		
		<div class="space-bottom-top align-right">
			<html:button property="refreshButton" onclick="refresh();" styleClass="button">
				<fmt:message key="label.refresh" />
			</html:button>
		</div>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->
	
	<div id="leaderSelectionDialog" title="<fmt:message key='label.leader.selection' />" class="dialog">
	
		<div style="font-weight:bold; margin: 10px 0 20px;">
			<fmt:message key="label.are.you.going.to.be.leader" />
		</div>
		
		<div>
			<fmt:message key="label.users.from.group" />
		</div>
		
		<div style="text-align: right;">
			<c:forEach var="user" items="${groupUsers}" varStatus="status">
				<div>
					${user.firstName} ${user.lastName}
				</div>
			</c:forEach>
		</div>
	</div>
	

</body>
</lams:html>
