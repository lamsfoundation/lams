<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<link type="text/css" href="${lams}css/jquery-ui-1.8.11.redmont-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid-4.1.2.css" rel="stylesheet" />
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery-1.7.1.min.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.min.js"></script>

  	    <script>
  	    	<!-- 
	  	  	$(document).ready(function(){
	  			<c:forEach var="summary" items="${summaryList}" varStatus="status">
		  			
	  				jQuery("#session${summary.sessionId}").jqGrid({
	  					datatype: "local",
	  					height: 'auto',
	  					width: 500,
	  					shrinkToFit: true,
	  					
	  				   	colNames:["<fmt:message key="label.monitoring.summary.user.name" />",
	  							"<fmt:message key="label.monitoring.summary.attempt.number" />"],
	  						    
	  				   	colModel:[
							{name:'userName',index:'userName', width:100},
	  				   		{name:'attemptNumber', index:'attemptNumber', width:200, sorttype:"int"}		
	  				   	],
	  				   	
	  				   	multiselect: false,
	  				   	caption: "${summary.sessionName}"
  						/*  resetSelection() doesn't work in this version
						    hope it'll be fixed in the next one
						    
	  					,
	  					onSelectRow: function (rowid){
	  						$("[id^='user']").resetSelection();
	  					},
	  					onCellSelect: function (rowid, iCol, cellcontent){
	  						jQuery("#session${session.sessionId}}").resetSelection();
	  					}*/ 	  				  	
	  				});
	  				
	  	   	        <c:forEach var="user" items="${summary.users}" varStatus="i">
			   	   	  	<c:choose>
			   	   			<c:when test="${user.attemptNumber == -1}">
			   	   				var attemptNumber = "-";
			   	   			</c:when>
			   	   			<c:otherwise>
			  	   	        	var attemptNumber = ${user.attemptNumber};
			   	   			</c:otherwise>
		   	   			</c:choose>		  	   	        

	  	   	     		jQuery("#session${summary.sessionId}").addRowData(${i.index + 1}, {
	  	   	     			userName:"${user.lastName}, ${user.firstName}",
	  	   	     			attemptNumber:attemptNumber
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>
	  		});  	    	
	  		
    		function refreshSummaryPage()  {
        		self.parent.tb_remove();
    		}
  			-->
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.monitoring.summary.report.for" >
					<fmt:param>${item.description}</fmt:param>
				</fmt:message>
			</h1>
			<br><br>		
			<%@ include file="/common/messages.jsp"%>
			
			<h3>
				${item.description}
				
				<c:if test='${item.correct}'> 
					<div class="float-right">
						<img src='<html:rewrite page='/includes/images/tick.gif'/>' border='0'>
						<fmt:message key="label.monitoring.item.summary.correct" />
					</div>
				</c:if>
			</h3>
			
			${item.description}
			<br/>
			<br/>
			
			<c:forEach var="summary" items="${summaryList}" varStatus="status">
				<div style="padding-left: 0px; padding-bottom: 30px;">
					<div style="font-size: small; padding-bottom: 5px;">
						<fmt:message key="label.monitoring.item.summary.group" /> ${summary.sessionName}
					</div>
					
					<table id="session${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0" ></table>
				</div>	
			</c:forEach>	


			<lams:ImgButtonWrapper>
				<a href="#" onclick="refreshSummaryPage();" class="button space-left" style="float:right; margin-right:40px; padding-top:5px;">
					<fmt:message key="button.close" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
