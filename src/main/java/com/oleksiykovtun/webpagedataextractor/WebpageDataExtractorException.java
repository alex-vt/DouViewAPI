package com.oleksiykovtun.webpagedataextractor;

/**
 * Exception for WebpageDataExtractor.
 */
public class WebpageDataExtractorException extends RuntimeException {

    public WebpageDataExtractorException(String message) {
        super(message);
    }

    public WebpageDataExtractorException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
