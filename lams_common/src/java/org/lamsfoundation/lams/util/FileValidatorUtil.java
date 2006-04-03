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
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import org.apache.struts.validator.Resources;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Field;
import org.apache.struts.action.ActionMessages;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.util.ValidatorUtils;
import javax.servlet.http.HttpServletRequest;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionForm;

/**
 * This class is used by commons validator. To validate various properties 
 * related to any uploaded files
 * 
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class FileValidatorUtil {
    
    public static final String LARGE_FILE = "largeFile";

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

        int maxFileSize;
        
        //whether we are using large file or not?
        if(Boolean.valueOf(field.getVarValue(LARGE_FILE)).booleanValue())
            maxFileSize = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE);
        else
            maxFileSize = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
        
        String fileSizeStr = ValidatorUtils.getValueAsString(bean, field.getProperty());
        int fileSize = Integer.parseInt(fileSizeStr);
              
        if(fileSize >  maxFileSize){
            //Set arg0 in message bundle
            Arg arg = new Arg();
            arg.setPosition(0);
            arg.setResource(false); //dont treat this as a locale key
            arg.setKey(String.valueOf(maxFileSize));
            field.addArg(arg);
            
            //set error message
            errors.add(field.getKey(), Resources.getActionMessage(request, va,
                    field));
            return false;
        }
        return true;
    }
}
