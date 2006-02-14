/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
/**
 * Service class to help Service bean get i18n value quickly. The locale information will get from <code>LAMS_USER</code>
 * table. For more detail see <code>org.lamsfoundation.lams.web.filter.LocaleFilter</code>.
 * 
 * @author Steve.Ni
 * @version $Revision$
 * @see org.springframework.context.support.MessageSourceAccessor
 */
public class MessageService {
    private MessageSourceAccessor messageAccessor;
    
    /**
     * Set <code>MessageSource</code> from spring IoC. 
     * @param messageSource
     */
    public void setMessageSource(MessageSource messageSource){
    	messageAccessor = new MessageSourceAccessor(messageSource);
    }
    
    /**
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String)
     * @param key
     * @return
     */
    public String getMessage(String key){
    	String message;
    	try {
    		message = messageAccessor.getMessage(key,LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			message = "??" + key + "??";
		}
		return message;
    }
    /** 
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String, java.lang.String)
     * @param key
     * @param defaultMessage
     * @return
     */
    public String getMessage(String key, String defaultMessage){
    	String message = defaultMessage;
    	try {
    		message = messageAccessor.getMessage(key,defaultMessage,LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			message = defaultMessage;
		}
		return message;
    }
    /**
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String, java.lang.Object[])
     * @param key
     * @param args
     * @return
     */
    public String getMessage(String key, Object[] args){
    	String message;
    	try {
    		message = messageAccessor.getMessage(key,args,LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			message = "??" + key + "??";
		}
		return message;
    }
    /**
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String, java.lang.Object[], java.lang.String)
     * @param key
     * @param args
     * @param defaultMessage
     * @return
     */
    public String getMessage(String key, Object[] args, String defaultMessage){
    	String message = defaultMessage;
    	try {
    		message = messageAccessor.getMessage(key,args,defaultMessage,LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			message = defaultMessage;
		}
		return message;
    }
}
