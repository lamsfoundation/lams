package org.jgroups.blocks;

import java.lang.reflect.Method;

/**
 * @author Bela Ban
 * @version $Id$
 */
public interface MethodLookup {
    Method findMethod(short id);
}
