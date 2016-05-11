package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions 
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import javax.servlet.ServletContext;
import java.util.*;
import java.util.Hashtable;

class ServletUnitContext {

    private SessionListenerDispatcher _listenerDispatcher;
    private ServletContext _servletContext;


    ServletUnitContext( String contextPath, ServletContext servletContext, SessionListenerDispatcher dispatcher ) {
        _servletContext = servletContext;
        _contextPath = (contextPath != null ? contextPath : "");
        _listenerDispatcher = dispatcher;
    }


	Set getSessionIDs() {
		return _sessions.keySet();
	}


    /**
     * Returns an appropriate session for a request. If no cached session is
     * @param sessionId
     * @param session the session cached by previous requests. May be null.
     * @param create
     * @return
     */
    ServletUnitHttpSession getValidSession( String sessionId, ServletUnitHttpSession session, boolean create ) {
        if (session == null && sessionId != null) {
            session = getSession( sessionId );
        }

        if (session != null && session.isInvalid()) {
            session = null;
        }

        if (session == null && create) {
            session = newSession();
        }
        return session;
    }


    /**
     * Returns the session with the specified ID, if any.
     **/
    ServletUnitHttpSession getSession( String id ) {
        return (ServletUnitHttpSession) _sessions.get( id );
    }


    /**
     * Creates a new session with a unique ID.
     **/
    ServletUnitHttpSession newSession() {
        ServletUnitHttpSession result = new ServletUnitHttpSession( _servletContext, _listenerDispatcher );
        _sessions.put( result.getId(), result );
        _listenerDispatcher.sendSessionCreated( result );
        return result;
    }

    /**
     * Returns the contextPath
     */
    String getContextPath() {
        return _contextPath;
    }


//------------------------------- private members ---------------------------


    private Hashtable _sessions = new Hashtable();

    private String _contextPath = null;


}
