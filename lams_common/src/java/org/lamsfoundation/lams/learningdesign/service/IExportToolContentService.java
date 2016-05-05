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

import java.io.File;
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
     * <P>
     * For IMS format learning design, it uses XSLT and some JDom code to transform Lams format learing_design.xml to
     * imsmanifest.xml.
     * <BR>
     * It genereate 2 temporary files: resource.mxl and transitions.xml.<BR>
     * <ul>
     * <li>resource.mxl. It is used under resources tag, which contains all attachment files from tools</li>
     * <li>transtions.xml. It is used under &lt;activity-structure identifier="A-sequence"&gt; tag. It contains sorted
     * &lt;learning-activity-ref&gt; tags.</li>
     * </ul>
     * </P>
     * <P>
     * During IMS exporting, it also copy all Lams format tool.xml to a temporary folder,"xslttemp", and makes below
     * modifications from original tool.xml:
     * <ul>
     * <li>Change root tag from xxx.xxx.xxx.DTO to toolSignatur+toolContnetId. This make looks better</li>
     * <li>Add &lt;mainObject&gt; tag to hold original root tag, ie, the DTO class name. This is just for transfrom back
     * to Lams format if some day need this function.</li>
     * <li>Add lams tool's namespace to root tag</li>
     * </ul>
     * </P>
     * <P>
     * During IMS exporting, it will copy all schema files to "content" folder for pacakge usage. It also copy XSLT to
     * "xslttemp" folder for XSLT transform usage. It means all necessary
     * files for XSLT will be in same folder to avoid abosulate/relative folder assign in XSLT.
     * </P>
     * 
     * @param learningDesignId
     * @param toolsErrorMsgs
     * @param format
     *            LAMS format or IMS format
     * @return the full file path of exported learning design zip file.
     * @throws ExportToolContentException
     */
    String exportLearningDesign(Long learningDesignId, List<String> toolsErrorMsgs, int format, File xslt)
	    throws ExportToolContentException;

    /**
     * Export tool content.
     * 
     * @param toolContentId
     *            the tool content ID.
     * @param toolContentObj
     *            The POJO object to descript the tool content which need to export
     * @param toolContentHandler
     *            need be used when this tool has any items in LAMS repository to be exported.
     * @param toPath
     *            the target local file system directory to put tool.xml and its attached files or package.
     * 
     * @throws ExportToolContentException
     */
    void exportToolContent(Long toolContentId, Object toolContentObj, IToolContentHandler toolContentHandler,
	    String toPath) throws ExportToolContentException;

    /**
     * Register access class and relative method for item in LAMS repository. For example, there is POJO to refer to
     * a tool offline instruction files:
     * 
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
     * @param fileNodeClassName
     *            The POJO class name for repository item reference
     * @param fileUuidFieldName
     *            The POJO properties name for get fileUuid. There must be a get method to access this property.
     * @param fileVersionFieldName
     *            The POJO properties name for get fileVersion. There must be a get method to access this property.
     */
    void registerFileClassForExport(String fileNodeClassName, String fileUuidFieldName, String fileVersionFieldName);

    void registerFileClassForImport(String fileNodeClassName, String fileUuidFieldName, String fileVersionFieldName,
	    String fileNameFieldName, String filePropertyFieldName, String mimeTypeFieldName,
	    String initialItemFieldName);

    void registerImportVersionFilterClass(Class filterClass);

    /**
     * Import the learning design from the given path. Set the importer as the creator. If the workspaceFolderUid is
     * null then saves the design in the user's own workspace folder.
     * 
     * @param designFile
     * @param importer
     * @param workspaceFolderUid
     * @param customCSV
     *            the customCSV to be used by tool adapters to create new instances on the external LMS
     * @return An object array where:
     *         <ul>
     *         <li>Object[0] = ldID (Long)</li>
     *         <li>Object[1] = ldErrors when importing (List<String>)</li>
     *         <li>Object[2] = toolErrors when importing (List<String>)</li>
     *         </ul>
     * 
     * @throws ImportToolContentException
     */
    Object[] importLearningDesign(File designFile, User importer, Integer workspaceFolderUid,
	    List<String> toolsErrorMsgs, String customCSV) throws ImportToolContentException;

    /**
     * Import the learning design from the given path. Set the importer as the creator. If the workspaceFolderUid is
     * null then saves the design in the user's own workspace folder.
     * 
     * @param learningDesignPath
     * @param importer
     * @param workspaceFolderUid
     * @param customCSV
     *            the customCSV to be used by tool adapters to create new instances on the external LMS
     * @return learningDesignID
     * @throws ImportToolContentException
     */
//	Long importLearningDesignv2(String learningDesignPath,User importer, Integer workspaceFolderUid, List<String> toolsErrorMsgs) throws ImportToolContentException;

    /**
     * Import the tool content. This is called by tools to do the actual content import, once the tool
     * has set up whatever it needs to do.
     */
    Object importToolContent(String toolContentPath, IToolContentHandler toolContentHandler, String fromVersion,
	    String toVersion) throws ImportToolContentException;

    /**
     * Import 1.0.2 learning design
     * 
     * @return learningDesingID
     * @throws ExportToolContentException
     * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.importLearningDesign102(String, User,
     *      WorkspaceFolder)
     */
//	public Long importLearningDesignV102(String ldWddxPacket, User importer, Integer workspaceFolderUid
//			, List<String> toolsErrorMsgs) throws ImportToolContentException;

}
