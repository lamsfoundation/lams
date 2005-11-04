/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.util;

import org.apache.struts.validator.Resources;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Field;
import org.apache.struts.action.ActionMessages;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.upload.FormFile;

/**
 * This class is used by commons validator. To validate various properties 
 * related to any uploaded files
 * 
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class FileValidatorUtil {

    /** 
     * To enable this validator copy the XML entry below to validator-rules.xml
     *     <validator name="maxFileSize"
     *           classname="org.lamsfoundation.lams.util.FileValidator"
     *              method="validateFileSize"
     *        methodParams="java.lang.Object,
     *                      org.apache.commons.validator.ValidatorAction,
     *                      org.apache.commons.validator.Field,
     *                      org.apache.struts.action.ActionMessages,
     *                      org.apache.commons.validator.Validator,
     *                      javax.servlet.http.HttpServletRequest"
     *                 msg="errors.maxfilesize"/>
     * 
     * Then to validate your uploaded file, by placing it under the corresponding <form> tag
     *     <field property="myuploadfile.fileSize" depends="maxFileSize" /> 
     *     
     * NOTE: You need to add an errors.maxfilesize entry to your resource bundle or 
     *       you can change the "msg" attribute in the "validator" tag above   
     */
    public static boolean validateFileSize(Object bean, ValidatorAction va,
            Field field, ActionMessages errors, Validator validator,
            HttpServletRequest request) {
       
        String fileSizeStr = ValidatorUtils.getValueAsString(bean, field.getProperty());
        int fileSize = Integer.parseInt(fileSizeStr);
              
        if(fileSize > 1000000){       
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));

            return false;
        }

        return true;
    }
}
