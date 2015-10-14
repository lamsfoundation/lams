<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>

  	    <script>
	  	  	$(document).ready(function(){
	  			<c:forEach var="summary" items="${summaryList}" varStatus="status">
		  			
	  				jQuery("#session${summary.sessionId}").jqGrid({
	  					datatype: "local",
	  					rowNum: 10000,
	  					height: 'auto',
	  					width: 500,
	  					shrinkToFit: true,
	  					
	  				   	colNames:["<fmt:message key="label.monitoring.summary.answer" />"
		  			  	   	        <c:forEach var="answer" items="${summary.answers}" varStatus="i">
		  			  	   	 			,"${i.index + 1} <fmt:message key="label.monitoring.summary.choice" />"
					  		        </c:forEach>
	  							],
	  						    
	  				   	colModel:[
							{name:'answer',index:'userName', width:100}
  			  	   	        <c:forEach var="answer" items="${summary.answers}" varStatus="i">
			  	   	 			,{name:'choice${i.index}', index:'${i.index}choice', width:20, sorttype:"int"}
		  		        	</c:forEach>		
	  				   	],
	  				   	
	  				   	multiselect: false,
	  				   	caption: "<fmt:message key="label.monitoring.item.summary.group" /> ${summary.sessionName}"
	  				});
	  				
	  	   	        <c:forEach var="answer" items="${summary.answers}" varStatus="i">
	  	   	     		jQuery("#session${summary.sessionId}").addRowData(${i.index + 1}, {
	  	   	     			answer:"${answer.description}<c:if test='${answer.correct}'> (<fmt:message key='label.monitoring.item.summary.correct' />)</c:if>"
	  	   	     			
	  	   	     			<c:forEach var="j" begin="0" end="${fn:length(summary.answers)}">
	  	   	     				,choice${j}:"${answer.attempts[j]}"
	  	   	     			</c:forEach>
	  	   	     			
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>
	  		});  	    	
	  		
    		function refreshSummaryPage()  {
        		self.parent.tb_remove();
    		}
  		</script>
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.monitoring.summary.report.for" >
					<fmt:param>${fn:escapeXml(item.title)}</fmt:param>
				</fmt:message>
			</h1>
			<h3>
				<c:out value="${item.description}" escapeXml="false"/>
			</h3>	
			<%@ include file="/common/messages.jsp"%>
			<br/>
			<br/>
			
			<c:forEach var="summary" items="${summaryList}" varStatus="status">
				<div style="padding-left: 0px; padding-bottom: 30px;">
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
