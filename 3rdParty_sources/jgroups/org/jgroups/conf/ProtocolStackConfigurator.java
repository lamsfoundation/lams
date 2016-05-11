

package org.jgroups.conf;

/**
 * @author Filip Hanik (<a href="mailto:filip@filip.net">filip@filip.net)
 * @version 1.0
 */

public interface ProtocolStackConfigurator
{
    String         getProtocolStackString();
    ProtocolData[] getProtocolStack();
}
