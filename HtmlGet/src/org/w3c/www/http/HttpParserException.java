// HttpParserException.java
// $Id: HttpParserException.java,v 1.1.2.1 2003/05/28 22:58:49 owenc Exp $
// (c) COPYRIGHT MIT and INRIA, 1996.
// Please first read the full copyright statement in file COPYRIGHT.html

package org.w3c.www.http;

import org.w3c.www.mime.MimeParserException;

public class HttpParserException extends MimeParserException {

    HttpRequestMessage req = null;

    public boolean hasRequest() {
	return (req != null);
    }

    public HttpRequestMessage getRequest() {
	return req;
    }

    public HttpParserException(String msg) {
	super(msg);
    }

    public HttpParserException(String msg, HttpRequestMessage req) {
	super(msg);
	this.req = req;
    }
}
