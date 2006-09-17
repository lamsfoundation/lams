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

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.util.List;

import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * Export tool content service provides ability to export learning design and its relative activities' tool content.
 *   
 * @author Steve.Ni
 * @version $Revision$
 */
public interface IExportToolContentService {
	/**
	 * Export given learning design tool content. It includes all tools content
	 * in this learning design.
	 * 
	 * @param learningDesignId
	 * @return the full file path of exported learning design zip file.
	 * @throws ExportToolContentException
	 */
	String exportLearningDesign(Long learningDesignId, List<String> toolsErrorMsgs) throws ExportToolContentException;
	/**
	 * Export tool content. 
	 * 
	 * @param toolContentId the tool content ID.
	 * @param toolContentObj The POJO object to descript the tool content which need to export 
	 * @param toolContentHandler need be used when this tool has any items in LAMS repository to be exported.
	 * @param toPath the target local file system directory to put tool.xml and its attached files or package.
	 * 
	 * @throws ExportToolContentException
	 */
	void exportToolContent(Long toolContentId, Object toolContentObj, IToolContentHandler toolContentHandler, String toPath)
		throws ExportToolContentException;
	
	/**
	 * Register access class and relative method for item in LAMS repository. For example, there is POJO to refer to 
	 * a tool offline instruction files:
	 * <pre>
	 * <code>
	 * public class ShareResourceToolFile{
	 * 	private Long fileUuid;
	 * 	private Long fileVersionId;
	 *  ...
	 *  //set&get methods
	 * }
	 * </code>
	 * </pre>
	 *   
	 * Tool must call this method to tell export service to get item by this fileUuid and fileVersionId properties.
	 *  
	 * @param fileNodeClassName The POJO class name for repository item reference  
	 * @param fileUuidFieldName The POJO properties name for get fileUuid. There must be a get method to access this property.
	 * @param fileVersionFieldName The POJO properties name for get fileVersion. There must be a get method to access this property.
	 */
	void registerFileClassForExport(String fileNodeClassName,String fileUuidFieldName, String fileVersionFieldName);
	void registerFileClassForImport(String fileNodeClassName, String fileUuidFieldName,
			String fileVersionFieldName, String fileNameFieldName, String filePropertyFieldName, String mimeTypeFieldName,
			String initialItemFieldName);
	
	/** Import the learning design from the given path. Set the importer as the creator. If the workspaceFolderUid is
	 * null then saves the design in the user's own workspace folder.
	 * 
	 * @param learningDesignPath
	 * @param importer
	 * @param workspaceFolderUid
	 * @return learningDesignID
	 * @throws ImportToolContentException
	 */
	Long importLearningDesign(String learningDesignPath,User importer, Integer workspaceFolderUid, List<String> toolsErrorMsgs) throws ImportToolContentException;
	
	/** 
	 * Import the tool content. This is called by tools to do the actual content import, once the tool
	 * has set up whatever it needs to do. 
	 */
	Object importToolContent(String toolContentPath, IToolContentHandler toolContentHandler)
		throws ImportToolContentException;
	
	/**
	 * Import 1.0.2 learning design 
	 * @return learningDesingID
	 * @throws ExportToolContentException 
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.importLearningDesign102(String, User, WorkspaceFolder)
	 */
	public Long importLearningDesign102(String ldWddxPacket, User importer, Integer workspaceFolderUid
			, List<String> toolsErrorMsgs) throws ImportToolContentException;

}
