<%/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/**
 * Tabs.tag
 *	Author: Fiona Malikoff
 *	Description: Create a hybrid panel header that contains a nav bar that acts like tabs.
 */

%>
<%@ attribute name="control" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="refreshOnClickAction" required="false" rtexprvalue="true"%>
<%@ attribute name="helpPage" required="false" rtexprvalue="true"%>
<%@ attribute name="helpToolSignature" required="false" rtexprvalue="true"%>
<%@ attribute name="helpModule" required="false" rtexprvalue="true"%>
<%@ attribute name="extraControl" required="false" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="useActions" value="false" scope="request" />
<c:if test="${not empty helpToolSignature or not empty helpModule or not empty helpPage or not empty refreshOnClickAction or not empty extraControl}">
	<c:set var="useActions" value="true" scope="request" />
</c:if>

<c:set var="dControl" value="false" scope="request" />
<c:if test="${control}">
	<c:set var="dControl" value="${control}" scope="request" />
</c:if>

<!-- navbar combined with tabs -->
<div class="panel panel-default panel-monitor-page">
<div class="panel-heading navbar-heading">

 	<nav class="navbar navbar-default navbar-heading">
	<div class="container-fluid">
    	<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
	      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <c:if test="${not empty title}">
	      	<span class="navbar-brand">${title}</span>
	      </c:if>
		</div>
		<div class="collapse navbar-collapse" id="navbar-collapse-1" role="navigation">
        	<ul class="nav navbar-nav" id="page-tabs">
		     	<c:if test="${not empty title}">
			    	<li role="separator" class="divider"></li>
			    </c:if>
	          	<jsp:doBody />
         	</ul>
         	<c:if test="${useActions}">
		         <ul class="nav navbar-nav navbar-right" id="page-actions">
				     <li role="separator" class="divider"></li>
				     <c:if test="${not empty refreshOnClickAction}">
		             <li class="navbar-text" ><span onclick="${refreshOnClickAction}"><i class="fa fa-refresh"></i></span></li>
		             </c:if>
		             <c:if test="${not empty helpToolSignature or not empty helpModule}">
		             <li class="navbar-text" ><lams:help toolSignature="${helpToolSignature}" module="${helpModule}" style="small"/></li>
		             </c:if>
		             <c:if test="${not empty helpPage}">
		             <li class="navbar-text" ><lams:help page="${helpPage}" style="small"/></li>
		             </c:if>
		             <c:if test="${not empty extraControl}">
		             <li class="navbar-text" >${extraControl}</li>
		             </c:if>
		         </ul>
         	</c:if>
 	    
 		</div>
	 </div>
     </nav>
     <!-- /top nav -->
</div>
<!-- panel div closed by TabBodyArea -->