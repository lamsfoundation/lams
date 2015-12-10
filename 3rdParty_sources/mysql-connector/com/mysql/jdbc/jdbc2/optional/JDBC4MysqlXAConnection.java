/*
  Copyright (c) 2007, 2015, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.jdbc.jdbc2.optional;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

import com.mysql.jdbc.Connection;

public class JDBC4MysqlXAConnection extends MysqlXAConnection {

    private final Map<StatementEventListener, StatementEventListener> statementEventListeners = new HashMap<StatementEventListener, StatementEventListener>();

    public JDBC4MysqlXAConnection(Connection connection, boolean logXaCommands) throws SQLException {
        super(connection, logXaCommands);
    }

    public synchronized void close() throws SQLException {
        super.close();

        this.statementEventListeners.clear();
    }

    /**
     * Registers a <code>StatementEventListener</code> with this <code>PooledConnection</code> object. Components that
     * wish to be notified when <code>PreparedStatement</code>s created by the
     * connection are closed or are detected to be invalid may use this method
     * to register a <code>StatementEventListener</code> with this <code>PooledConnection</code> object.
     * 
     * @param listener
     *            an component which implements the <code>StatementEventListener</code> interface that is to be registered with this
     *            <code>PooledConnection</code> object
     * @since 1.6
     */
    public void addStatementEventListener(StatementEventListener listener) {
        synchronized (this.statementEventListeners) {
            this.statementEventListeners.put(listener, listener);
        }
    }

    /**
     * Removes the specified <code>StatementEventListener</code> from the list of
     * components that will be notified when the driver detects that a <code>PreparedStatement</code> has been closed or is invalid.
     * 
     * @param listener
     *            the component which implements the <code>StatementEventListener</code> interface that was previously
     *            registered with this <code>PooledConnection</code> object
     * @since 1.6
     */
    public void removeStatementEventListener(StatementEventListener listener) {
        synchronized (this.statementEventListeners) {
            this.statementEventListeners.remove(listener);
        }
    }

    void fireStatementEvent(StatementEvent event) throws SQLException {
        synchronized (this.statementEventListeners) {
            for (StatementEventListener listener : this.statementEventListeners.keySet()) {
                listener.statementClosed(event);
            }
        }
    }
}