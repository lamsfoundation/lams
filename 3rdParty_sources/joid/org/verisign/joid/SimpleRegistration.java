//
// (C) Copyright 2007 VeriSign, Inc.  All Rights Reserved.
//
// VeriSign, Inc. shall have no responsibility, financial or
// otherwise, for any consequences arising out of the use of
// this material. The program material is provided on an "AS IS"
// BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied.
//
// Distributed under an Apache License
// http://www.apache.org/licenses/LICENSE-2.0
//

package org.verisign.joid;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Simple registration extensions, as defined by
 * http://openid.net/specs/openid-simple-registration-extension-1_0.html
 * 
 * <strong>This class should only be used by internal request/response 
 * processing</strong>. 
 * 
 * TODO to make this clearer.
 */
public class SimpleRegistration
{
    private final static Log LOG = LogFactory.getLog( SimpleRegistration.class );

    private Set<String> required;
    private Set<String> optional;
    private Map<String,String> supplied;
    private String policyUrl;

    /** The <code>openid.sreg</code> value */
    public final static String OPENID_SREG = "openid.sreg";

    /** The <code>openid.ns.sreg</code> value */
    public final static String OPENID_SREG_NSDEF = "openid.ns.sreg";

    /** The <code>openid.sreg.required</code> value */
    public final static String OPENID_SREG_REQUIRED = OPENID_SREG + ".required";

    /** The <code>openid.sreg.optional</code> value */
    public final static String OPENID_SREG_OPTIONAL = OPENID_SREG + ".optional";

    /** The <code>openid.sreg.policy_url</code> value */
    public final static String OPENID_SREG_POLICY_URL = OPENID_SREG + ".policy_url";

    /** Invalid namespace for sreg (from XRDS) seen in the wild */
    public final static String OPENID_SREG_NAMESPACE_10 = "http://openid.net/sreg/1.0";

    /** Standard namespace value for sreg */
    public final static String OPENID_SREG_NAMESPACE_11 = "http://openid.net/extensions/sreg/1.1";

    private final static String SREG_NICKNAME = "nickname";
    private final static String SREG_EMAIL = "email";
    private final static String SREG_FULLNAME = "fullname";
    private final static String SREG_DOB = "dob";
    private final static String SREG_GENDER = "gender";
    private final static String SREG_POSTCODE = "postcode";
    private final static String SREG_COUNTRY = "country";
    private final static String SREG_LANGUAGE = "language";
    private final static String SREG_TIMEZONE = "timezone";

    private String namespace;
    private String nickName;
    private String email;
    private String fullName;
    private String dob;
    private String gender;
    private String postCode;
    private String country;
    private String language;
    private String timeZone;

    /**
     * The set of the nine allowed SREG values.
     */
    public final static Set<String> ALLOWED;
    static
    {
        Set<String> set = new HashSet<String>();
        set.add( SREG_NICKNAME );
        set.add( SREG_EMAIL );
        set.add( SREG_FULLNAME );
        set.add( SREG_DOB );
        set.add( SREG_GENDER );
        set.add( SREG_POSTCODE );
        set.add( SREG_COUNTRY );
        set.add( SREG_LANGUAGE );
        set.add( SREG_TIMEZONE );
        
        ALLOWED = Collections.unmodifiableSet( set );
    }


    /**
     * Creates a simple registration. 
     */
    SimpleRegistration( Set<String> required, Set<String> optional, Map<String,String> supplied,
        String policyUrl )
    {
        this.required = required;
        this.optional = optional;
        this.supplied = supplied;
        this.policyUrl = policyUrl;
        this.namespace = OPENID_SREG_NAMESPACE_11;
    }


    /**
     * Creates a simple registration. 
     */
    SimpleRegistration( Set<String> required, Set<String> optional, Map<String,String> supplied, 
        String policyUrl, String namespace )
    {
        this.required = required;
        this.optional = optional;
        this.supplied = supplied;
        this.policyUrl = policyUrl;
        this.namespace = namespace;
    }


    SimpleRegistration( Map<String,String> map ) throws OpenIdException
    {
        required = new HashSet<String>();
        optional = new HashSet<String>();
        supplied = new HashMap<String,String>();
        namespace = OPENID_SREG_NAMESPACE_11;

        Set<Map.Entry<String,String>> set = map.entrySet();
        for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();

            if ( OPENID_SREG_REQUIRED.equals( key ) )
            {
                addToSetFromList( required, value );
            }
            else if ( OPENID_SREG_OPTIONAL.equals( key ) )
            {
                addToSetFromList( optional, value );
            }
            else if ( OPENID_SREG_POLICY_URL.equals( key ) )
            {
                policyUrl = value;
            }
            else if ( OPENID_SREG_NSDEF.equals( key ) )
            {
                if ( OPENID_SREG_NAMESPACE_10.equals( value ) || OPENID_SREG_NAMESPACE_11.equals( value ) )
                {
                    namespace = value;
                }
            }
        }
    }


    public boolean isRequested()
    {
        return ( !( required.isEmpty() && optional.isEmpty() ) );
    }


    /**
     * Expects a map with values like "openid.sreg.nickname=blahblah" in it
     */
    public static SimpleRegistration parseFromResponse( Map<String,String> map )
    {
        Set<String> req = new HashSet<String>();
        Set<String> opt = new HashSet<String>();
        Map<String,String> sup = new HashMap<String,String>();
        String ns = OPENID_SREG_NAMESPACE_11;

        String trigger = OPENID_SREG + ".";
        int triggerLength = trigger.length();
        Set<Map.Entry<String,String>> set = map.entrySet();
        for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();

            if ( key.startsWith( trigger ) )
            {
                sup.put( key.substring( triggerLength ), value );
            }
            else if ( OPENID_SREG_NSDEF.equals( key ) )
            {
                if ( OPENID_SREG_NAMESPACE_10.equals( value ) || OPENID_SREG_NAMESPACE_11.equals( value ) )
                {
                    ns = value;
                }
            }
        }
        return new SimpleRegistration( req, opt, sup, "", ns );
    }


    private void addToSetFromList( Set<String> set, String value )
    {
        if ( value == null )
        {
            return;
        }
        StringTokenizer st = new StringTokenizer( value, "," );
        while ( st.hasMoreTokens() )
        {
            String token = st.nextToken().trim();
            if ( ALLOWED.contains( token ) )
            {
                set.add( token );
            }
            else
            {
                LOG.info( "Illegal sreg value: " + token );
            }
        }
    }


    public String getPolicyUrl()
    {
        return policyUrl;
    }


    public Set<String> getRequired()
    {
        return Collections.unmodifiableSet( required );
    } 


    public Set<String> getOptional()
    {
        return Collections.unmodifiableSet( optional );
    } 


    public void setRequired( Set<String> set )
    {
        required = set;
    }


    public void setOptional( Set<String> set )
    {
        optional = set;
    }


    public String getNamespace()
    {
        return namespace;
    }


    public Map<String,String> getSuppliedValues()
    {
        Map<String,String> map = new HashMap<String,String>();
        addAllNonEmpty( supplied, map );
        return map;
    }


    private void addAllNonEmpty( Map<String,String> from, Map<String,String> to )
    {
        Set<Map.Entry<String,String>> set = from.entrySet();
        for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            if ( value != null )
            {
                to.put( key, value );
            }
        }
    }


    public String toString()
    {
        return "[SimpleRegistration required=" + required
            + ", optional=" + optional + ", supplied=" + supplied
            + ", policy url=" + policyUrl
            + ", namespace=" + namespace + "]";
    }


    /**
     * @param nickName the nickName to set
     */
    public void setNickName( String nickName )
    {
        this.nickName = nickName;
    }


    /**
     * @return the nickName
     */
    public String getNickName()
    {
        return nickName;
    }


    /**
     * @param email the email to set
     */
    public void setEmail( String email )
    {
        this.email = email;
    }


    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }


    /**
     * @param fullName the fullName to set
     */
    public void setFullName( String fullName )
    {
        this.fullName = fullName;
    }


    /**
     * @return the fullName
     */
    public String getFullName()
    {
        return fullName;
    }


    /**
     * @param postCode the postCode to set
     */
    public void setPostCode( String postCode )
    {
        this.postCode = postCode;
    }


    /**
     * @return the postCode
     */
    public String getPostCode()
    {
        return postCode;
    }


    /**
     * @param dob the dob to set
     */
    public void setDob( String dob )
    {
        this.dob = dob;
    }


    /**
     * @return the dob
     */
    public String getDob()
    {
        return dob;
    }


    /**
     * @param country the country to set
     */
    public void setCountry( String country )
    {
        this.country = country;
    }


    /**
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }


    /**
     * @param language the language to set
     */
    public void setLanguage( String language )
    {
        this.language = language;
    }


    /**
     * @return the language
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone( String timeZone )
    {
        this.timeZone = timeZone;
    }


    /**
     * @return the timeZone
     */
    public String getTimeZone()
    {
        return timeZone;
    }


    /**
     * @param gender the gender to set
     */
    public void setGender( String gender )
    {
        this.gender = gender;
    }


    /**
     * @return the gender
     */
    public String getGender()
    {
        return gender;
    }
}
