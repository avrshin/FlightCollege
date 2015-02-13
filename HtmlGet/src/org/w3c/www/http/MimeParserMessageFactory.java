// MimeParserMessageFactory.java
// $Id: MimeParserMessageFactory.java,v 1.1.2.1 2003/05/28 22:58:49 owenc Exp $  
// (c) COPYRIGHT MIT and INRIA, 1997.
// Please first read the full copyright statement in file COPYRIGHT.html

// For debuging only.

package org.w3c.www.http;

import org.w3c.www.mime.MimeHeaderHolder;
import org.w3c.www.mime.MimeParser;
import org.w3c.www.mime.MimeParserFactory;

/**
 * The MIME parser factory for HTTP requests.
 */

public class MimeParserMessageFactory implements MimeParserFactory {

    /**
     * Create a new HTTP request to hold the parser's result.
     * @param parser The parser that has something to parse.
     * @return A MimeParserHandler compliant object.
     */

    public MimeHeaderHolder createHeaderHolder(MimeParser parser) {
	return new HttpEntityMessage(parser) ;
    }
}
