<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

		<table>
						<tr> 
						<td NOWRAP valign=top>
				  				<b> <fmt:message key="count.total.user" /> </b>
				  			</td>
							<td NOWRAP valign=top>
							  	  <c:out value="${mcGeneralMonitoringDTO.countAllUsers}"/>
							</td> 
						</tr>
						
						<tr> 
							<td NOWRAP valign=top>
				  				<b>  <fmt:message key="count.finished.session" /> </b>
				  			</td>
							<td NOWRAP valign=top>
							  	 <c:out value="${mcGeneralMonitoringDTO.countSessionComplete}"/>
							</td> 
						</tr>
		</table>


