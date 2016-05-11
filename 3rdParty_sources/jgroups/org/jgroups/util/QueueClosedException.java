

package org.jgroups.util;


public class QueueClosedException extends Exception {

    private static final long serialVersionUID = -7575787375592873964L;

	public QueueClosedException() {

    }

    public QueueClosedException( String msg )
    {
        super( msg );
    }

    public String toString() {
        if ( this.getMessage() != null )
            return "QueueClosedException: " + this.getMessage();
        else
            return "QueueClosedException";
    }
}
