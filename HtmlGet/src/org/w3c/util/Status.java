// Status.java
// $Id: Status.java,v 1.1.2.1 2003/05/28 22:58:46 owenc Exp $
// (c) COPYRIGHT MIT, INRIA and Keio, 2000.
// Please first read the full copyright statement in file COPYRIGHT.html

package org.w3c.util ;

public interface Status {

    /**
     * Give the status of this class as a partial HTML text which will be added
     * into a block level element
     * @return a String, the generated HTML
     */
    public String getHTMLStatus();
}
