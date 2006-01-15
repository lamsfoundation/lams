<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


		<table class="forms">
			<tr>
				<td>
				<table align="center">
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2> <bean:message key="count.total.user" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	 <font size=2> <c:out value="${sessionScope.countAllUsers}"/>
							</td> 
						</tr>
						
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2> <bean:message key="count.finished.user" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	 <font size=2> <c:out value="${sessionScope.countSessionComplete}"/></font>
							</td> 
						</tr>
					
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2> <bean:message key="label.topMark" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	<font size=2> <c:out value="${sessionScope.topMark}"/></font>
							</td> 
						</tr>
						
						<tr>
							<td colspan=2> &nbsp&nbsp&nbsp </td>
						</tr>
				
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2><bean:message key="label.avMark" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	<font size=2> <c:out value="${sessionScope.averageMark}"/></font>
							</td> 
						</tr>
						
						<tr>
							<td colspan=2> &nbsp&nbsp&nbsp </td>
						</tr>
				
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2> <bean:message key="label.loMark" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	<font size=2> <c:out value="${sessionScope.lowestMark}"/></font>
							</td> 
						</tr>
			
						<tr> 
							<td NOWRAP align="right" valign=top>
				  				<b> <font size=2> <bean:message key="count.max.attempt" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
							  	<font size=2> <c:out value="${sessionScope.countMaxAttempt}"/></font>
							</td> 
						</tr>
				</table>
				</td>
			</tr>
		</table>


