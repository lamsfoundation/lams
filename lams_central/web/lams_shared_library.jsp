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
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" import="org.lamsfoundation.lams.web.util.AttributeNames" %>
<%
String library = request.getParameter(AttributeNames.PARAM_LIB);

String authoringClientVersion = Configuration.get(ConfigurationKeys.AUTHORING_CLIENT_VERSION);
String monitoringClientVersion = Configuration.get(ConfigurationKeys.MONITOR_CLIENT_VERSION);

String buildVersion = (library.equals("lams_authoring_library.swf")) ? authoringClientVersion : monitoringClientVersion;

String redirectURL = library + "?build=" + buildVersion;
response.setContentType("application/x-shockwave-flash");
response.sendRedirect(redirectURL);

%>