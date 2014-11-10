package com.oleksiykovtun.webpagedataextractor;

/**
 *  DOM node list wrapper.
 */
public class NodeList {

    private org.w3c.dom.NodeList nodeList;

    protected NodeList(org.w3c.dom.NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public Node get(int index) {
        try {
            return new Node(nodeList.item(index));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WebpageDataExtractorException("Cannot retrieve node at " + index
                    + " from list of " + nodeList.getLength() + " nodes.", e);
        }
    }

    public int getCount() {
        return nodeList.getLength();
    }

}
