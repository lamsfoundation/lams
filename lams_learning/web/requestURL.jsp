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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<c:if test="${empty url}">
			<c:set var="url" value="${param.url}" />
		</c:if>
		
		<script type="text/javascript">
			function doRedirect() {
				var myParent = parent;
				var url = decodeURIComponent("<c:out value='${url}' escapeXml='false' />");
				if ( myParent )
					myParent.location.href = url;
				else 
					window.location.href = url;
			}
			window.onload = doRedirect;
		</script>
	</lams:head>
</lams:html>