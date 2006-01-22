<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

		<!-- tab holder table -->
		<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
		  	<td> &nbsp&nbsp</td>
		  </tr>
			
		  <tr>
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_su" width="8" height="25" border="0" id="tab_left_su"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_su" ><label for="su" accesskey="su">
						<a href="<c:out value='${monitoringURL}'/>?method=getSummary" id="su" >
							<bean:message key="label.summary"/>
						</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getSummary">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_su" width="8" height="25" border="0" id="tab_right_su"/></a></td>
					  </tr>
					</table>
				</td>

			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_i" width="8" height="25" border="0" id="tab_left_i"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_i" ><label for="i" accesskey="i"><a
							href="<c:out value='${monitoringURL}'/>?method=getInstructions" id="i" >
								<bean:message key="label.instructions"/>
							</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getInstructions">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_i" width="8" height="25" border="0" id="tab_right_i"/></a></td>
					  </tr>
					</table>
				</td>

				
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_e" width="8" height="25" border="0" id="tab_left_e"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_e" ><label for="e" accesskey="e"><a
							href="<c:out value='${monitoringURL}'/>?method=editActivity" id="e" >
								<bean:message key="label.editActivity"/>								
							</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=editActivity">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_e" width="8" height="25" border="0" id="tab_right_e"/></a></td>
					  </tr>
					</table>
				</td>

							
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_s" width="8" height="25" border="0" id="tab_left_s"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_s" ><label for="s" accesskey="s"><a
							href="<c:out value='${monitoringURL}'/>?method=getStats" id="e" >
								<bean:message key="label.stats"/>								
							</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getStats">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_s" width="8" height="25" border="0" id="tab_right_s"/></a></td>
					  </tr>
					</table>
				</td>
		  </tr>
		</table>
