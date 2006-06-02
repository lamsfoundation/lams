<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<div align="center">

	<h1><fmt:message key="label.view.groups.title"/></h1>
	<table border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
	<tr>
		<td><fmt:message key="message.view.groups.wait"/></td>
	</tr>
	<tr>
		<td align="right">
			<html:form action="/grouping.do?method=performGrouping" target="_self">
				<html:submit styleClass="button"><fmt:message key="label.gate.next.button"/></html:submit>
			</html:form>
		</td>
	</tr>
	</table>


</div>