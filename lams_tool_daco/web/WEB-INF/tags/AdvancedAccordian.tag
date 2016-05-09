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
	 *	Description: Creates the show/hide entry for the Advanced Settings in Montoring.
	 * Wiki: 
	 */
%>
<%@ attribute name="title" required="true" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="panel-group" id="accordionAdvanced" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="headingAdvanced">
        	<span class="panel-title">
	    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseAdvanced" aria-expanded="false" aria-controls="collapseAdvanced" >
          	${title}
        	</a>
      		</span>
        </div>

        <div id="collapseAdvanced" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingAdvanced">
		<jsp:doBody />
		</div>
	</div>
</div>
		