package org.verisign.joid.consumer;


import java.util.List;
import java.util.ArrayList;


/**
 * User: treeder
 * Date: Oct 2, 2008
 * Time: 11:56:56 PM
 */
public class XRDSDocument
{
    // List<XRDSService>
    private List<XRDSService> serviceList = new ArrayList<XRDSService>();


    public List<XRDSService> getServiceList()
    {
        return serviceList;
    }


    public void setServiceList( List<XRDSService> serviceList )
    {
        this.serviceList = serviceList;
    }


    public void addService( XRDSService service )
    {
        serviceList.add( service );
    }
}
