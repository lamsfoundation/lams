/**
 * Register.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface Register extends java.rmi.Remote {
    public boolean createUser(String username, 
    		String password, 
    		String firstName, 
    		String lastName, 
    		String email, 
    		String serverId, 
    		String datetime, 
    		String hash) throws java.rmi.RemoteException;
    
    public boolean addUserToGroup(
			String username, 
			String serverId, 
			String datetime, 
			String hash,
			Boolean isTeacher) throws java.rmi.RemoteException;
    
    public boolean addUserToGroupLessons(
			String username, 
			String serverId, 
			String datetime, 
			String hash) throws java.rmi.RemoteException;
    
    public boolean addUserToSubgroup(
			String username,
			String orgid,
			String serverId, 
			String datetime, 
			String hash,
			Boolean isTeacher) throws java.rmi.RemoteException;
    
    public boolean addUserToSubgroupLessons(
			String username,
			String orgId,
			String serverId, 
			String datetime, 
			String hash) throws java.rmi.RemoteException;
}
