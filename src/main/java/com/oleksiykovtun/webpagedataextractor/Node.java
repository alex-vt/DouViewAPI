package com.oleksiykovtun.webpagedataextractor;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * DOM node wrapper.
 */
public class Node {

    private org.w3c.dom.Node node;


    public String getAttributeName(int attributeIndex) {
        try {
            return node.getAttributes().item(attributeIndex).getNodeName().trim();
        } catch (Exception e) {
            throw new WebpageDataExtractorException("Cannot retrieve attribute " + attributeIndex
                    + " in node " + node.getNodeName(), e);
        }
    }

    public String getAttributeValue(int attributeIndex) {
        try {
            return node.getAttributes().item(attributeIndex).getNodeValue().trim();
        } catch (Exception e) {
            throw new WebpageDataExtractorException("Cannot retrieve attribute " + attributeIndex
                    + " in node " + node.getNodeName(), e);
        }
    }

    public boolean isXPathExisting(String xPathString) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            return (xpath.evaluate(xPathString, node, XPathConstants.NODE) != null);
        } catch (XPathExpressionException e) {
            throw new WebpageDataExtractorException("XPath evaluation of request " + xPathString
                    + "failed at node " + node.getNodeName(), e);
        }
    }

    public Node evaluateXPath(String xPathString) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        org.w3c.dom.Node node = null;
        try {
            node = (org.w3c.dom.Node) xpath.evaluate(xPathString, this.node, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new WebpageDataExtractorException("XPath evaluation of request " + xPathString
                    + " failed at node " + this.node.getNodeName(), e);
        }
        if (node == null) {
            throw new WebpageDataExtractorException("No element found in node " + this.node.getNodeName()
                    + " by XPath request " + xPathString);
        }
        return new Node(node);
    }

    public boolean isXPathListExisting(String xPathString) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            return (xpath.evaluate(xPathString, node, XPathConstants.NODESET) != null);
        } catch (XPathExpressionException e) {
            throw new WebpageDataExtractorException("XPath (list) evaluation of request " + xPathString
                    + "failed at node " + node.getNodeName(), e);
        }
    }

    public NodeList evaluateXPathList(String xPathString) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        org.w3c.dom.NodeList nodeList = null;
        try {
            nodeList = (org.w3c.dom.NodeList) xpath.evaluate(xPathString, node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new WebpageDataExtractorException("XPath (list) evaluation of request " + xPathString
                    + "failed at node " + node.getNodeName(), e);
        }
        if (nodeList == null) {
            throw new WebpageDataExtractorException("No elements found in node " + this.node.getNodeName()
                    + " by XPath request " + xPathString);
        }
        return new NodeList(nodeList);
    }

    public String getTextContent() {
        return node.getTextContent().trim();
    }

    public boolean isChildExisting(String nodeName) {
        return isChildExisting(nodeName, 0);
    }

    public Node getFirstChild(String nodeName) {
        return getChild(nodeName, 0);
    }

    public boolean isChildExisting(String nodeName, int index) {
        org.w3c.dom.NodeList childrenNodes = node.getChildNodes();
        int counter = 0;
        for (int i = 0; i < childrenNodes.getLength(); ++i) {
            if (childrenNodes.item(i).getNodeName().equals(nodeName)) {
                if (counter == index) {
                    return true;
                }
                ++counter;
            }
        }
        return false;
    }

    public Node getChild(String nodeName, int index) {
        Node targetNode = null;
        int counter = 0;
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            if (node.getChildNodes().item(i).getNodeName().equals(nodeName)) {
                if (counter == index) {
                    targetNode = new Node(node.getChildNodes().item(i));
                }
                ++counter;
            }
        }
        if (targetNode == null) {
            throw new WebpageDataExtractorException("Cannot retrieve node " + nodeName
                    + " # " + index + " in "+ getTree("", ""));
        }
        return targetNode;
    }

    public int getChildrenCount(String nodeName) {
        int counter = 0;
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            if (node.getChildNodes().item(i).getNodeName().equals(nodeName)) {
                ++counter;
            }
        }
        return counter;
    }

    public int getFirstIntFromContent() {
        try {
            return getIntFromString(getTextContent(), 0);
        } catch (NumberFormatException e) {
            throw new WebpageDataExtractorException("Failed to extract int for node " + getName());
        }
    }

    public int getIntFromAttributeValue(int index) {
        try {
            return getIntFromString(getAttributeValue(index), 0);
        } catch (NumberFormatException e) {
            throw new WebpageDataExtractorException("Failed to extract int for node " + getName()
                + " from attribute's " + getAttributeName(index) + " value " + getAttributeValue(index));
        }
    }

    public Node getParent() {
        return new Node(node.getParentNode());
    }

    protected Node(org.w3c.dom.Node node) {
        this.node = node;
    }

    protected org.w3c.dom.Node getRawData() {
        return node;
    }

    protected String getTree(String currentTreeString, String intend) {
        if (currentTreeString.length() > 1000) return "";
        if (intend.length() <= 40) {
            String description = " ";
            if (node.hasAttributes()) {
                description += node.getAttributes().item(0).getNodeValue();
            }
            currentTreeString += "\n" + intend + node.getNodeName() + description;
            if (node.hasChildNodes()) {
                org.w3c.dom.NodeList childrenNodes = node.getChildNodes();
                for (int i = 0; i < childrenNodes.getLength(); ++i) {
                    currentTreeString = new Node(childrenNodes.item(i)).getTree(currentTreeString, intend + "    ");
                }
            }
        }
        return currentTreeString;
    }

    private String getName() {
        return node.getNodeName();
    }

    private int getIntFromString(String string, int index) {
        return Integer.parseInt(string.replaceAll("[^0-9\\s]", "").trim().split("\\s+")[index]);
    }

}
