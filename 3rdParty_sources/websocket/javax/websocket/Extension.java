/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javax.websocket;

import java.util.List;

/**
 * A simple representation of a websocket extension as a name and map of extension parameters.
 *
 * @author dannycoward
 */
public interface Extension {

    /**
     * The name of the extension.
     *
     * @return the name of the extension.
     */
    String getName();

    /**
     * The extension parameters for this extension in the order
     * they appear in the http headers.
     *
     * @return The read-only Map of extension parameters belonging to this extension.
     */
    List<Parameter> getParameters();

    /**
     * This member interface defines a single websocket extension parameter.
     */
    interface Parameter {
        /**
         * Return the name of the extension parameter.
         *
         * @return the name of the parameter.
         */
        String getName();

        /**
         * Return the value of the extension parameter.
         *
         * @return the value of the parameter.
         */
        String getValue();
    }
}
