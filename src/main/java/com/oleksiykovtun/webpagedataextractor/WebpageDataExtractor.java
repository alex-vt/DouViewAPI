package com.oleksiykovtun.webpagedataextractor;

import org.htmlcleaner.*;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *  Simple data items extractor from a web page.
 */
public class WebpageDataExtractor {

    public static Node loadDocument(String urlString) throws Exception {
        return loadDocument(urlString, UserAgent.DESKTOP);
    }

    public static Node loadDocument(String urlString, String userAgentString) throws Exception {
        URLConnection urlConnection = new URL(urlString).openConnection( );
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestProperty("User-agent", userAgentString);
        urlConnection.connect();
        InputStream contentInputStream = (InputStream)urlConnection.getContent( );
        CleanerProperties cleanerProperties = getProperties();
        Node document = new Node(new DomSerializer(cleanerProperties, false)
                .createDOM(new HtmlCleaner().clean(contentInputStream)));
        contentInputStream.close();
        return document;
    }

    public static void saveDocument(Node document, String filePathString) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Source input = new DOMSource(document.getRawData());
        Result output = new StreamResult(new File(filePathString));
        transformer.transform(input, output);
    }

    private static CleanerProperties getProperties() {
        CleanerProperties cleanerProperties = new CleanerProperties();
        cleanerProperties.setTransSpecialEntitiesToNCR(true);
        cleanerProperties.setCharset("UTF-8");
        cleanerProperties.setRecognizeUnicodeChars(true);
        cleanerProperties.setTransResCharsToNCR(true);
        cleanerProperties.setOmitComments(true);
        return cleanerProperties;
    }

}
