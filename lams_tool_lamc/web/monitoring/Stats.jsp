<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


		<table class="forms" align="left">
			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="count.total.user" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	 <c:out value="${sessionScope.countAllUsers}"/>
				</td> 
			</tr>
			
			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="count.finished.user" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	 <c:out value="${sessionScope.countSessionComplete}"/>
				</td> 
			</tr>
		
			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="label.topMark" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	 <c:out value="${sessionScope.topMark}"/>
				</td> 
			</tr>
			
			<tr>
				<td colspan=2> &nbsp&nbsp&nbsp </td>
			</tr>
	
			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="label.avMark" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	<c:out value="${sessionScope.averageMark}"/>
				</td> 
			</tr>
			
			<tr>
				<td colspan=2> &nbsp&nbsp&nbsp </td>
			</tr>
	
			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="label.loMark" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	<c:out value="${sessionScope.lowestMark}"/>
				</td> 
			</tr>

			<tr> 
				<td NOWRAP class="formlabel" align="right" valign=top>
	  				<b> <bean:message key="count.max.attempt" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
				  	<c:out value="${sessionScope.countMaxAttempt}"/>
				</td> 
			</tr>
			
		</table>


