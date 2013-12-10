package com.meterware.httpunit;

/**
 * A listener for DNS Requests. Users may implement this interface to bypass the normal DNS lookup.
 *
 * @author <a href="russgold@httpunit.org">Russell Gold</a>
 **/
public interface DNSListener {


    /**
     * Returns the IP address as a string for the specified host name.
     * Note: no validation is done to verify that the returned value is an actual IP address or
     * that the passed host name was not an IP address.
     **/
    String getIpAddress( String hostName );

}
