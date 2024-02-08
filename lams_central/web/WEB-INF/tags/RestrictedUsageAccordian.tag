<%
	 /****************************************************************
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
	 * AdvancedAccordian.tag
	 *	Author: Fiona Malikoff
	 *	Description: Creates the show/hide entry for the Restricted Usage Settings in Monitoring.
	 * Wiki: 
	 */
%>
<%@ attribute name="title" required="true" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="accordion" id="accordionRestrictUsageDiv"> 
    <div class="accordion-item">
    	<h2 class="accordion-header" id="headingRestrictedUsageDiv">
        	<button class="accordion-button collapsed text-bg-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#restrictUsageDiv" aria-expanded="false" aria-controls="restrictUsageDiv">
	          	${title}
    		</button>
        </h2>

		<div id="restrictUsageDiv" class="accordion-collapse collapse p-3" aria-labelledby="headingRestrictedUsageDiv">
      		<div>
      			<jsp:doBody />
      		</div>
      	</div>
	</div>
</div>
