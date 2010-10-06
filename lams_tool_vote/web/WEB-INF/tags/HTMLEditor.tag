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
	 * HTMLEditor.tag
	 *	Author: Mitchell Seaton
	 *	Description: Creates a single instance of FCK Editor used over multiple fields.
	 * Wiki: 
	 */
%>
<%@ tag body-content="empty"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<div id="wyswygEditorScreen" style="visibility: hidden">
	<!-- position: absolute; z-index: 1000; top: 16px; left: 230px; -->
	<!--  Testing by Anthony, please delete this comment -->
	<div id="wyswygEditor">
		<div>
			<c:set var="language">
				<lams:user property="localeLanguage" />
			</c:set>
			<c:set var="basePath"><lams:LAMSURL/>ckeditor/</c:set>
			
			<textarea id="FCKeditor1" name="FCKeditor1" cols="8" rows="40"></textarea>
			
			<script type="text/javascript" src="${basePath}ckeditor.js"></script>
			<script type="text/javascript">
			
				CKEDITOR.basePath = "${basePath}";
				
				CKEDITOR.replace( "FCKeditor1", {
						language                      : "${language}",
						defaultLangugage              : "en",
						filebrowserBrowseUrl          : "${basePath}filemanager/browser/default/browser.html?Type=File&Connector=connectors/jsp/connector",
						filebrowserUploadUrl          : "${basePath}filemanager/upload/simpleuploader?Type=File",
						filebrowserImageBrowseUrl     : "${basePath}filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector",
						filebrowserImageUploadUrl     : "${basePath}filemanager/upload/simpleuploader?Type=Image",
						filebrowserImageBrowseLinkUrl : "${basePath}filemanager/browser/default/browser.html?Connector=connectors/jsp/connector",
						filebrowserFlashBrowseUrl     : "${basePath}filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector",
						filebrowserFlashUploadUrl     : "${basePath}filemanager/upload/simpleuploader?Type=Flash"
				});
			</script>
		</div>
		<div style="text-align: center">
			<a href="#"
				onClick="saveWYSWYGEdittedText(activeEditorIndex); doPreview(activeEditorIndex)"><img
					src="${lams}images/tick.gif" border="0"
					alt="<fmt:message key="label.save"/>" />
			</a>
			<a href="#" onClick="doPreview(activeEditorIndex)"><img
					src="${lams}images/cross.gif" border="0"
					alt="<fmt:message key="label.cancel"/>" />
			</a>
		</div>
	</div>
</div>
