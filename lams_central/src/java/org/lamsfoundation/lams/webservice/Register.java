/**
 * Register.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface Register extends java.rmi.Remote {
    public boolean createUser(String username, String password, String firstName, String lastName, String email,
	    String serverId, String datetime, String hash) throws java.rmi.RemoteException;

    public int createOrganisation(String name, String code, String description, String owner, String serverId,
	    String datetime, String hash) throws java.rmi.RemoteException;

    public boolean addUserToOrganisation(String login, Integer organisationId, Boolean asStaff, String serverId,
	    String datetime, String hash) throws java.rmi.RemoteException;

    public boolean addUserToGroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, Boolean isTeacher)
	    throws java.rmi.RemoteException;

    public boolean addUserToGroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, Boolean asStaff)
	    throws java.rmi.RemoteException;

    public boolean addUserToSubgroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, String subgroupId, String subgroupName,
	    Boolean isTeacher) throws java.rmi.RemoteException;

    public boolean addUserToSubgroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, String subgroupId,
	    String subgroupName, Boolean asStaff) throws java.rmi.RemoteException;
}
