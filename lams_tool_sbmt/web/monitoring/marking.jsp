<%@ include file="/common/taglibs.jsp" %>
				<table border="0" cellpadding="2" cellspacing="2">					
					<tr>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('viewAllMarks', 5);" property="viewAllMarks" styleClass="button">
							<bean:message key="label.monitoring.viewAllMarks.button" />
						</html:link>
			  			</td>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('releaseMarks');" property="releaseMarks" styleClass="button">
							<bean:message key="label.monitoring.releaseMarks.button" />
						</html:link>
			  			</td>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('downloadMarks');" property="downloadMarks" styleClass="button">
							<bean:message key="label.monitoring.downloadMarks.button" />
						</html:link>
			  			</td>
	  				</tr>
				</table>