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

public class MessageService {
    private MessageSourceAccessor messageAccessor;
    
    private MessageSource messageSource;
    /**
     * @return
     */
    public MessageSource getMessageSource() {
		return messageSource;
	}
    public void setMessageSource(MessageSource messageSource){
    	this.messageSource = messageSource;
    }
    public String getMessage(String key){
    	String message;
    	try {
    		messageAccessor = new MessageSourceAccessor(messageSource);
    		message = messageAccessor.getMessage(key,LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			message = "??" + key + "??";
		}
		return message;
    }
}
