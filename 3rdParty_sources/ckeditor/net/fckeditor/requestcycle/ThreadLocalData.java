/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2004-2010 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package net.fckeditor.requestcycle;

import javax.servlet.http.HttpServletRequest;

/**
 * Maintains current {@link Context context} and {@link HttpServletRequest
 * request} instances. This container relies on {@link ThreadLocal} and provides
 * static access to the aforementioned objects for the current File Browse
 * request. This means that this class is thread-safe.
 * 
 *
 */
public class ThreadLocalData {
	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<Context> context = new ThreadLocal<Context>();
	
	/**
	 * Initializes the current request cycle.
	 * 
	 * @param request
	 *            current user request instance
	 */
	public static void beginRequest(final HttpServletRequest request) {
		if (request == null)
			throw new NullPointerException("the request cannot be null");
		ThreadLocalData.request.set(request);
		ThreadLocalData.context.set(new Context(request));
	}
	
	/**
	 * Returns the current user request instance.
	 * 
	 * @return the current user request instance
	 */
	public static HttpServletRequest getRequest() {
		return request.get();
	}

	/**
	 * Returns the current context instance.
	 * 
	 * @return the current context instance
	 */
	public static Context getContext() {
		return context.get();
	}
	
	/**
	 * Terminates the current request cycle. <br />
	 * <strong>Important: To prevent memory leaks, make sure that this
	 * method is called at the end of the current request cycle!</strong>
	 */
	public static void endRequest() {
		// ThreadLocal#remove is not available in Java 1.4
		request.set(null);
		context.set(null);
	}
}
