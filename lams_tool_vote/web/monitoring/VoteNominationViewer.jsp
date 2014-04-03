<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
	<lams:head>
	<title> <fmt:message key="label.learnersVoted"/> </title>

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	
</lams:head>
<body class="stripes">
<div id="content">

    <table class="alternative-color">
	    <tr> <th NOWRAP colspan=2 align=center>  <fmt:message key="label.learnersVoted"/> </th> </tr>
	
		<tr> 
	       <td NOWRAP align=center valign=top><b> <fmt:message key="label.user"/> </b></td>
		   <td NOWRAP align=center valign=top><b> <fmt:message key="label.attemptTime"/> </b></td>
	   </tr>
	    
		<c:forEach var="userData" items="${voteGeneralMonitoringDTO.mapStudentsVoted}">
			<tr>		 
					 <td NOWRAP valign=top align=center>    <c:out value="${userData.userName}"/>   </td>  
					 <td NOWRAP valign=top align=center>    <lams:Date value="${userData.attemptTime}"/>  </td>					 
			</tr>				 
		</c:forEach>		  	
    </table>

</div>
<div id="footer"></div>
</body>
</lams:html>
