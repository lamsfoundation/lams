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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.thoughtworks.xstream.XStream;
/**
 * Export tool content service bean.
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportToolContentService implements IExportToolContentService, ApplicationContextAware {
	public static final String LEARNING_DESIGN_SERVICE_BEAN_NAME = "learningDesignService";
	//export tool content zip file prefix
	public static final String EXPORT_TOOLCONTNET_ZIP_PREFIX = "lams_toolcontent_";
	public static final String EXPORT_TOOLCONTNET_CONTENT_FOLDER = "content";
	public static final String EXPORT_TOOLCONTNET_FOLDER_SUFFIX = "export_toolcontent";
	public static final String EXPORT_TOOLCONTNET_ZIP_SUFFIX = ".zip";
	public static final String LEARNING_DESIGN_FILE_NAME = "learning_design.xml";
	
	private Logger log = Logger.getLogger(ExportToolContentService.class);
	
	private ApplicationContext applicationContext;
	
	//spring injection properties
	private ActivityDAO activityDAO;
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.exportToolContent(Long)
	 */
	public String exportToolContent(Long learningDesignId) throws ExportToolContentException{
		try {
			//root temp directory, put target zip file
			String rootDir = FileUtil.createTempDirectory(EXPORT_TOOLCONTNET_FOLDER_SUFFIX);
			//content directory to put all toolContent, root file is learning design xml.
			String contentDir = getFullPath(rootDir,EXPORT_TOOLCONTNET_CONTENT_FOLDER);
			if(!(new File(contentDir).mkdir()))
				throw new ExportToolContentException("Unable to create content directory.");
			
			String targetZipFileName = getFullPath(rootDir, EXPORT_TOOLCONTNET_ZIP_PREFIX
					+ learningDesignId + EXPORT_TOOLCONTNET_ZIP_SUFFIX);
			
			String ldFileName = getFullPath(contentDir,LEARNING_DESIGN_FILE_NAME);
			Writer ldFile = new FileWriter(new File(ldFileName));
			
			ILearningDesignService service =  getLearningDesignService();
			LearningDesignDTO ldDto = service.getLearningDesignDTO(learningDesignId);
			XStream xstream = new XStream();
			xstream.toXML(ldDto,ldFile);
			
			return ZipFileUtil.createZipFile(targetZipFileName, rootDir);
		} catch (FileUtilException e) {
			log.error("FileUtilExcpetion:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (ZipFileUtilException e) {
			log.error("ZipFileUtilException:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (IOException e) {
			log.error("IOException:" + e.toString());
			throw new ExportToolContentException(e);
		}
	}
	
	//******************************************************************
	// ApplicationContextAware method implementation
	//******************************************************************
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
	}
	//******************************************************************
	// Private methods
	//******************************************************************
	private ILearningDesignService getLearningDesignService(){
		return (ILearningDesignService) applicationContext.getBean(LEARNING_DESIGN_SERVICE_BEAN_NAME);		
	}
	
	private String getFullPath(String path, String file){
	    return path + File.separator + file;
	}
	
	//******************************************************************
	// Spring injection properties set/get
	//******************************************************************
	public ActivityDAO getActivityDAO() {
		return activityDAO;
	}
	public void setActivityDAO(ActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}
}
