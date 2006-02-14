package org.lamsfoundation.lams.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HttpRequestWrapper overriding methods getLocale(), getLocales() to include
 * the user's preferred locale.
 */
public class LocaleRequestWrapper extends HttpServletRequestWrapper {
	
    private final transient Log log = LogFactory.getLog(LocaleRequestWrapper.class);
    private final Locale preferredLocale;

    public LocaleRequestWrapper(HttpServletRequest decorated, Locale userLocale) {
        super(decorated);
        preferredLocale = userLocale;
        if (null == preferredLocale) {
            log.error("preferred locale = null, it is an unexpected value!");
        }
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocale()
     */
    public Locale getLocale() {
        if (null != preferredLocale) {
            return preferredLocale;
        } else {
            return super.getLocale();
        }
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocales()
     */
    public Enumeration getLocales() {
        if (null != preferredLocale) {
            List list = Collections.list(super.getLocales());
            if(list.contains(preferredLocale))
            {
                list.remove(preferredLocale);
            }
            list.add(0, preferredLocale);
            return Collections.enumeration(list);
        } else {
            return super.getLocales();
        }
    }

}
