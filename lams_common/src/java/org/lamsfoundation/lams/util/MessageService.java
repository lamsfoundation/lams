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

import java.util.Locale;

import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Service class to help Service bean get i18n value quickly. The locale information will get from
 * <code>LAMS_USER</code>
 * table. For more detail see <code>org.lamsfoundation.lams.web.filter.LocaleFilter</code>.
 *
 * @author Steve.Ni
 * @version $Revision$
 * @see org.springframework.context.support.MessageSource
 */
public class MessageService {
    private MessageSource messageSource;

    /**
     * Set <code>MessageSource</code> from spring IoC.
     * 
     * @param messageSource
     */
    public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
    }

    /**
     * Set <code>MessageSource</code> from spring IoC.
     * 
     * @param messageSource
     */
    public MessageSource getMessageSource() {
	return this.messageSource;
    }

    /**
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String)
     * @param key
     * @return
     */
    public String getMessage(String key) {
	String message;
	try {
	    message = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	} catch (NoSuchMessageException e) {
	    message = "??" + key + "??";
	}
	return message;
    }
    
    /**
     * Wrapper method for getMessage(String key) that allows specifying locale in which to do a loockup.
     * 
     * @param key
     * @param supportedLocale
     * @return
     */
    public String getMessage(String key, SupportedLocale supportedLocale) {
	Locale locale = new Locale(supportedLocale.getLanguageIsoCode(), supportedLocale.getCountryIsoCode());

	String message;
	try {
	    message = messageSource.getMessage(key, null, locale);
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
    public String getMessage(String key, String defaultMessage) {
	String message = defaultMessage;
	try {
	    message = messageSource.getMessage(key, null, defaultMessage, LocaleContextHolder.getLocale());
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
    public String getMessage(String key, Object[] args) {
	String message;
	try {
	    message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	} catch (NoSuchMessageException e) {
	    message = "??" + key + "??";
	}
	return message;
    }
    
    /**
     * Wrapper method for getMessage(String key, Object[] args) that allows specifying locale in which to do a loockup.
     * 
     * @param key
     * @param supportedLocale
     * @return
     */
    public String getMessage(String key, Object[] args, SupportedLocale supportedLocale) {
	Locale locale = new Locale(supportedLocale.getLanguageIsoCode(), supportedLocale.getCountryIsoCode());
	
	String message;
	try {
	    message = messageSource.getMessage(key, args, locale);
	} catch (NoSuchMessageException e) {
	    message = "??" + key + "??";
	}
	return message;
    }

    /**
     * @see org.springframework.context.support.MessageSourceAccessor#getMessage(java.lang.String, java.lang.Object[],
     *      java.lang.String)
     * @param key
     * @param args
     * @param defaultMessage
     * @return
     */
    public String getMessage(String key, Object[] args, String defaultMessage) {
	String message = defaultMessage;
	try {
	    message = messageSource.getMessage(key, args, defaultMessage, LocaleContextHolder.getLocale());
	} catch (NoSuchMessageException e) {
	    message = defaultMessage;
	}
	return message;
    }
}
