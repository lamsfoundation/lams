/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javax.websocket;

/**
 * A SessionException represents a general exception type reporting problems
 * occurring on a websocket session.
 * 
 * @author dannycoward
 */
public class SessionException extends Exception {
    private final Session session;
    private static final long serialVersionUID = 014;

    /**
     * Creates a new instance of this exception with the given message,
     * the wrapped cause of the exception and the session with which
     * the problem is associated.
     * 
     * @param message a description of the problem
     * @param cause the error that caused the problem
     * @param session the session on which the problem occurred.
     */
     
    public SessionException(String message, Throwable cause, Session session) {
        super(message, cause);
        this.session = session;
    }
    
    /** 
     * Return the Session on which the problem occurred.
     * 
     * @return the session
     */

    public Session getSession() {
        return this.session;
    }
}
