package org.lamsfoundation.lams.security;

import java.security.Principal;

public class SimplePrincipal implements Principal, java.io.Serializable
{
  private String name;

  public SimplePrincipal(String name)
  {
	this.name = name;
  }

  /** Compare this SimplePrincipal's name against another Principal
  @return true if name equals another.getName();
   */
  public boolean equals(Object another)
  {
	if( !(another instanceof Principal) )
	  return false;
	String anotherName = ((Principal)another).getName();
	boolean equals = false;
	if( name == null )
	  equals = anotherName == null;
	else
	  equals = name.equals(anotherName);
	return equals;
  }

  public int hashCode()
  {
	return (name == null ? 0 : name.hashCode());
  }

  public String toString()
  {
	return name;
  }

  public String getName()
  {
	return name;
  }
} 
