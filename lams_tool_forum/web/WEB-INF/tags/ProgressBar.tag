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
  * ProgressBar.tag
  *	Author: Marcin Cieslak
  *	Description: Creates a the progress bar widget.
  */
 
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>

<script type="text/javascript">
	$('head').append('<link rel="stylesheet" href=""<lams:LAMSURL />css/progressBar.css" type="text/css" />');
	
	var LAMS_URL = '<lams:LAMSURL/>',
	APP_URL = LAMS_URL + 'learning/',
	
	LABELS = {
		<fmt:message key="label.learner.progress.activity.current.tooltip" var="CURRENT_ACTIVITY_VAR"/>
		CURRENT_ACTIVITY : '<c:out value="${CURRENT_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.completed.tooltip" var="COMPLETED_ACTIVITY_VAR"/>
		COMPLETED_ACTIVITY : '<c:out value="${COMPLETED_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.attempted.tooltip" var="ATTEMPTED_ACTIVITY_VAR"/>
		ATTEMPTED_ACTIVITY : '<c:out value="${ATTEMPTED_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.tostart.tooltip" var="TOSTART_ACTIVITY_VAR"/>
		TOSTART_ACTIVITY : '<c:out value="${TOSTART_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.support.tooltip" var="SUPPORT_ACTIVITY_VAR"/>
		SUPPORT_ACTIVITY : '<c:out value="${SUPPORT_ACTIVITY_VAR}" />'
	},
	
	parentURL = "${notifyCloseURL}",
	lessonId = '1',
	progressPanelEnabled = true,
	
	// settings for progress bar
	presenceEnabled = false,
	isHorizontalBar = false,
	hasContentFrame = false,
	hasDialog = false,
	
	bars = {
		'learnerMainBar' : {
			'containerId' : 'progressBarDiv'
		}
	};

	$(document).ready(function() {
		fillProgressBar('learnerMainBar');
		
		$('#progressBarButton').click(function(){
			var bar = $('#progressBarDiv');
			if (bar.is(':visible')){
				bar.hide(1000);
			} else {
				bar.show(1000);
			}
		});
	});
</script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/progressBar.js"></script>

<div id="progressBarDiv" style="float: left; border: thin black solid; display: none; z-index:1000; position: absolute; background-color: rgb(219,230,252); top: 65px"></div>
<i id="progressBarButton" class="fa fa-bars"></i>&nbsp;