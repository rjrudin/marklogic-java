package com.marklogic.test.jdom;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fragment extends Assert {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Document internalDoc;
    private Namespace[] namespaces;
    private String uri;

    public Fragment(String xml, Namespace... namespaces) {
        try {
            internalDoc = new SAXBuilder().build(new StringReader(xml));
            this.namespaces = namespaces;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Fragment(String uri, String xml, Namespace... namespaces) {
        this(xml, namespaces);
        this.uri = uri;
    }

    public Fragment(Fragment other) {
        this.internalDoc = other.internalDoc;
        this.namespaces = other.namespaces;
        this.uri = other.uri;
    }

    public Fragment(Element el, Namespace... namespaces) {
        this.internalDoc = new Document(el.detach());
        this.namespaces = namespaces;
    }

    protected String format(String format, Object... args) {
        return String.format(format, args);
    }

    public Fragment getFragment(String xpath) {
        List<Element> list = evaluateForElements(xpath);
        try {
            assertElementListHasOneElement("Expected to find a single element with xpath: " + xpath, list, xpath);
            return new Fragment(list.get(0), this.namespaces);
        } catch (AssertionError ae) {
            prettyPrint();
            throw ae;
        }
    }

    public List<Fragment> getFragments(String xpath) {
        List<Element> elements = evaluateForElements(xpath);
        List<Fragment> fragments = new ArrayList<Fragment>();
        for (Element el : elements) {
            fragments.add(new Fragment(el, this.namespaces));
        }
        return fragments;
    }

    public void assertElementValue(String xpath, String value) {
        assertElementValue("Could not find element with value", xpath, value);
    }

    public String getElementValue(String xpath) {
        List<Element> list = evaluateForElements(xpath);
        try {
            assertElementListHasOneElement("", list, xpath);
            return list.get(0).getText();
        } catch (AssertionError ae) {
            prettyPrint();
            throw ae;
        }
    }

    /**
     * Seemingly can't use XPath in JDOM2 to get an attribute value directly.
     */
    public String getAttributeValue(String elementXpath, String attributeName) {
        List<Element> list = evaluateForElements(elementXpath);
        try {
            assertElementListHasOneElement("", list, elementXpath);
            return list.get(0).getAttributeValue(attributeName);
        } catch (AssertionError ae) {
            prettyPrint();
            throw ae;
        }
    }

    public void assertElementValue(String message, String xpath, String value) {
        List<Element> list = evaluateForElements(xpath);
        try {
            assertTrue(message += ";\nCould not find at least one element, xpath: " + xpath, list.size() > 0);
            boolean found = false;
            for (Element el : list) {
                if (value.equals(el.getText())) {
                    found = true;
                    break;
                }
            }
            assertTrue(message + "\n:Elements: " + list, found);
        } catch (AssertionError ae) {
            prettyPrint();
            throw ae;
        }
    }

    private void assertElementListHasOneElement(String message, List<Element> list, String xpath) {
        int size = list.size();
        assertTrue(message + ";\nExpected 1 element, but found " + size + "; xpath: " + xpath, size == 1);
    }

    public void assertElementExists(String xpath) {
        assertElementExists("", xpath);
    }

    public void assertElementExists(String message, String xpath) {
        List<Element> list = evaluateForElements(xpath);
        try {
            assertElementListHasOneElement(message, list, xpath);
        } catch (AssertionError ae) {
            prettyPrint();
            throw ae;
        }
    }

    public void assertElementMissing(String message, String xpath) {
        List<Element> list = evaluateForElements(xpath);
        assertEquals(message + ";\nexpected no elements matching xpath " + xpath, 0, list.size());
    }

    protected List<Element> evaluateForElements(String xpath) {
        XPathFactory f = XPathFactory.instance();
        XPathExpression<Element> expr = f.compile(xpath, Filters.element(), new HashMap<String, Object>(), namespaces);
        return expr.evaluate(internalDoc);
    }

    public void prettyPrint() {
        logger.info(getPrettyXml());
    }

    public String getPrettyXml() {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(internalDoc);
    }

    public String getUri() {
        return uri;
    }

    public Document getInternalDoc() {
        return internalDoc;
    }

    public Namespace[] getNamespaces() {
        return namespaces;
    }
}
