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

package org.lamsfoundation.lams.util;

import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.springframework.validation.Errors;

/**
 * This class is used by commons validator. To validate various properties
 * related to any uploaded files
 *
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class FileValidatorUtil {

    public static final String LARGE_FILE = "largeFile";
    private static final String MSG_KEY = "errors.maxfilesize";

    /**
     * To enable this validator copy the XML entry below to validator-rules.xml
     * <validator name="maxFileSize"
     * classname="org.lamsfoundation.lams.util.FileValidatorUtil"
     * method="validateFileSize"
     * methodParams="java.lang.Object,
     * org.apache.commons.validator.ValidatorAction,
     * org.apache.commons.validator.Field,
     * org.apache.struts.action.ActionMessages,
     * org.apache.commons.validator.Validator,
     * javax.servlet.http.HttpServletRequest"
     * msg="errors.maxfilesize"/>
     *
     * Then to validate your uploaded file size, by placing it under the corresponding <form> tag
     * <field property="myuploadfile.fileSize" depends="maxFileSize" />
     * We could also use maxFileSize to validated large file sizes by setting the largeFile flag
     * to true
     * <field property="myuploadfile.fileSize" depends="maxFileSize">
     * <var>
     * <var-name>largeFile</var-name>
     * <var-value>true</var-value>
     * </var>
     * </field>
     *
     * You need to add an errors.maxfilesize entry to your resource bundle or you can change
     * the "msg" attribute in the "validator" tag above. The maximum file size is pass to
     * the message as arg0. So the following message or something similar could be used.
     * errors.maxfilesize=The uploaded file has exceeded the maximum file size limit of {0} bytes
     */
    public static boolean validateFileSize(Object bean, ValidatorAction va, Field field, ActionMessages errors,
	    Validator validator, HttpServletRequest request) {

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
	//so far put message into GLOABLE_MESSAGE rather than special key
	return FileValidatorUtil.validateFileSize(fileSize, largeFile, ActionMessages.GLOBAL_MESSAGE, errors);
    }

    /**
     *
     * @param file
     * @param largeFile
     * @param errors
     * @return Be careful, if the file size is under maximum size, return TRUE. Otherwise, return false.
     */
    public static boolean validateFileSize(FormFile file, boolean largeFile, Errors errors) {
	return FileValidatorUtil.validateFileSize(file, largeFile, errors);

    }

    /**
     *
     * @param file
     * @param largeFile
     * @param errorKey
     *            the key in ActionMessages(errorKey,ActionMessage());
     * @param errors
     * @return Be careful, if the file size is under maximum size, return TRUE. Otherwise, return false.
     */
    public static boolean validateFileSize(FormFile file, boolean largeFile, String errorKey, ActionMessages errors) {
	int fileSize = 0;
	try {
	    fileSize = file.getFileSize();
	} catch (Exception e) {
	    //skip, do nothing
	    return true;
	}

	return FileValidatorUtil.validateFileSize(fileSize, largeFile, errorKey, errors);
    }
    
    /**
    *
    * @param file
    * @param largeFile
    * @return return null if file size is below max filesize, otherwise, return error message
    */
   public static ActionMessage validateFileSize(FileItem file, boolean largeFile) {
	int fileSize = (int) file.getSize();
	ActionMessages errors = new ActionMessages();
	
	ActionMessage errorMessage = null;
	boolean isMaxFilesizeExceeded = !FileValidatorUtil.validateFileSize(fileSize, largeFile,
		ActionMessages.GLOBAL_MESSAGE, errors);
	if (isMaxFilesizeExceeded) {
	    errorMessage = (ActionMessage)errors.get().next();
	}
	
	return errorMessage;
   }

    private static boolean validateFileSize(int fileSize, boolean largeFile, String errorKey, ActionMessages errors) {
	float maxFileSize = largeFile ? Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)
		: Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);

	if (fileSize > maxFileSize) {
	    String maxSize = FileValidatorUtil.formatSize(maxFileSize);

	    // set error message
	    errors.add(errorKey, new ActionMessage(MSG_KEY, maxSize));
	    return false;
	}
	return true;
    }

    public static String formatSize(double size) {
	String unit = null;
	if (size >= 1024) {
	    size = size / 1024;
	    if (size >= 1024) {
		size = size / 1024;
		unit = " MB";
	    } else {
		unit = " KB";
	    }
	} else {
	    unit = " B";
	}

	NumberFormat format = NumberFormat.getInstance();
	format.setMaximumFractionDigits(1);
	return format.format(size) + unit;
    }
}
