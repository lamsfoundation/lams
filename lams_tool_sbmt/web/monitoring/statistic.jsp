<%@include file="../sharing/share.jsp" %>
	<div class="datatablecontainer">
		<c:forEach var="element" items="${statisticList}">
			<c:set var="sessionName" value="${element.key.sessionName}"/>
			<c:set var="statistic" value="${element.value}"/>
			<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		    <tr>
		        <td style="border-bottom:1px #000 solid;" colspan="2"><b>SESSION NAME: <c:out value="${sessionName}" /></td>
		    </tr>
		    <tr>
			    <th scope="col">Title</th>
			    <th scope="col">Count</th>
			  </tr>
			  <tr>
			    <td><bean:message key="monitoring.statistic.marked"/></td>
			    <td><c:out value="${statistic.markedCount}"/></td>
			  </tr>
			  <tr>
			    <td><bean:message key="monitoring.statistic.not.marked"/></td>
			    <td><c:out value="${statistic.notMarkedCount}"/></td>
			  </tr>
			  <tr>
			    <td><bean:message key="monitoring.statistic.total.uploaded.file"/></td>
			    <td><c:out value="${statistic.totalUploadedFiles}"/></td>
			  </tr>
			</table>
		</c:forEach>
	</div>

