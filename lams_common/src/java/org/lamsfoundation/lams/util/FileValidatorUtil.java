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
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

/**
 * This class is used by commons validator. To validate various properties 
 * related to any uploaded files
 * 
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class FileValidatorUtil {
    
    public static final String LARGE_FILE = "largeFile";
	private static final String MSG_KEY = "errors.maxfilesize";

    /** 
     * To enable this validator copy the XML entry below to validator-rules.xml
     *     <validator name="maxFileSize"
     *           classname="org.lamsfoundation.lams.util.FileValidatorUtil"
     *              method="validateFileSize"
     *        methodParams="java.lang.Object,
     *                      org.apache.commons.validator.ValidatorAction,
     *                      org.apache.commons.validator.Field,
     *                      org.apache.struts.action.ActionMessages,
     *                      org.apache.commons.validator.Validator,
     *                      javax.servlet.http.HttpServletRequest"
     *                 msg="errors.maxfilesize"/>
     * 
     * Then to validate your uploaded file size, by placing it under the corresponding <form> tag
     *     <field property="myuploadfile.fileSize" depends="maxFileSize" />
     * We could also use maxFileSize to validated large file sizes by setting the largeFile flag
     * to true
     *     <field property="myuploadfile.fileSize" depends="maxFileSize">
     *         <var>
     *             <var-name>largeFile</var-name>
     *             <var-value>true</var-value>
     *         </var>
     *     </field>
     *
     * You need to add an errors.maxfilesize entry to your resource bundle or you can change 
     * the "msg" attribute in the "validator" tag above.  The maximum file size is pass to 
     * the message as arg0. So the following message or something similar could be used.
     *     errors.maxfilesize=The uploaded file has exceeded the maximum file size limit of {0} bytes
     */
    public static boolean validateFileSize(Object bean, ValidatorAction va,
            Field field, ActionMessages errors, Validator validator,
            HttpServletRequest request) {
        
        int fileSize = 0;
        try {
        	String fileSizeStr = ValidatorUtils.getValueAsString(bean, field.getProperty());
        	fileSize = Integer.parseInt(fileSizeStr);
		} catch (Exception e) {
			//catch null point exception: e.g., offlineFile.fileSize, if offlineFile is null, 
			//ValidatorUtils.getValueAsString() will throw null.
			//skip, do nothing
			return true;
		}
		
		boolean largeFile = Boolean.valueOf(field.getVarValue(LARGE_FILE)).booleanValue();
		
		return validateFileSize(fileSize, largeFile, errors);
    }
    /**
     * 
     * @param file
     * @param largeFile
     * @param errors
     * @return Be careful, if the file size is under maximum size, return TRUE. Otherwise, return false.   
     */
    public static boolean validateFileSize(FormFile file, boolean largeFile, ActionMessages errors){
        int fileSize = 0;
        try {
        	fileSize = file.getFileSize();
		} catch (Exception e) {
			//skip, do nothing
			return true;
		}
		
		return validateFileSize(fileSize, largeFile, errors);

    }
    
    private static boolean validateFileSize(int fileSize, boolean largeFile, ActionMessages errors){
    	float maxFileSize;
        
        //whether we are using large file or not?
        if(largeFile)
            maxFileSize = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE);
        else
            maxFileSize = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
        
        if(fileSize >  maxFileSize){
            //Set arg0 in message bundle
            String unit= "";
            if(maxFileSize >= 1024){
            	maxFileSize = maxFileSize/1024;
            	unit = "K";
            }
            if(maxFileSize >= 1024){
            	maxFileSize = maxFileSize/1024;
            	unit = "M";
            }
            NumberFormat format = NumberFormat.getInstance();
			format.setMaximumFractionDigits(1);
			String maxSize = format.format(maxFileSize) + unit;
            
            //set error message
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_KEY,maxSize));
            return false;
        }
        return true;
    }
}
