<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="fck-editor" prefix="FCK"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
  	    <%@ include file="basic.jsp"%>
  	   	<table>
		 	<tr>
		 		<!--<td>
					<html:button property="cancel" onclick="javascript:history.back()">
						<bean:message key="label.monitoring.edit.activity.cancel"/>
					</html:button>
					</td>-->
					<td>
					<html:link href="javascript:doSubmit('updateActivity');" property="Update" styleClass="button">
						<bean:message key="label.monitoring.edit.activity.update"/>
					</html:link>
				</td>
			</tr>
		</table>