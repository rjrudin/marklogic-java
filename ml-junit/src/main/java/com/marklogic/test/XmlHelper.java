package com.marklogic.test;

import java.io.File;
import java.io.IOException;

import org.custommonkey.xmlunit.Diff;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;

import com.marklogic.test.jdom.Fragment;
import com.marklogic.test.jdom.MarkLogicNamespaceProvider;
import com.marklogic.test.jdom.NamespaceProvider;
import com.marklogic.test.xmlunit.CustomDifferenceListener;

/**
 * Helper for tests that work with XML via JDOM2 and XmlUnit. Extends Assert, as this class makes assertions and it's
 * also handy to extend this class when writing a test class.
 */
public class XmlHelper extends Assert {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Fragment parse(String xml) {
        return new Fragment(xml, getNamespaceProvider().getNamespaces());
    }

    protected NamespaceProvider getNamespaceProvider() {
        return new MarkLogicNamespaceProvider();
    }

    protected File getTestFileFromClasspath(String pathRelativeToClasspath) {
        try {
            return new ClassPathResource(pathRelativeToClasspath).getFile();
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    protected String readTestClassResource(String pathRelativeToMyClass, String... tokensAndValues) {
        String text = readTestResource(ClassUtils.addResourcePathToPackagePath(getClass(), pathRelativeToMyClass));
        return tokensAndValues != null ? replaceTokensWithValues(text, tokensAndValues) : text;
    }

    protected String readTestResource(String path, String... tokensAndValues) {
        try {
            String text = new String(FileCopyUtils.copyToByteArray(new ClassPathResource(path).getInputStream()));
            return tokensAndValues != null ? replaceTokensWithValues(text, tokensAndValues) : text;
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    protected String replaceTokensWithValues(String text, String... tokensAndValues) {
        for (int i = 0; i < tokensAndValues.length; i += 2) {
            String token = tokensAndValues[i];
            String value = tokensAndValues[i + 1];
            text = text.replace(token, value);
        }
        return text;
    }

    protected String format(String str, Object... args) {
        return String.format(str, args);
    }

    public void assertFragmentIsSimilar(String message, Fragment frag, String controlXml) {
        CustomDifferenceListener cdl = new CustomDifferenceListener();
        try {
            Diff diff = new Diff(controlXml, frag.getPrettyXml());
            diff.overrideDifferenceListener(cdl);
            boolean similar = diff.similar();
            assertTrue(message + ";\n" + cdl.getDifferences(), similar);
        } catch (AssertionError ae) {
            frag.prettyPrint();
            logger.error(cdl.getDifferences().toString());
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assertFragmentIsIdentical(Fragment frag, String controlXml) {
        assertFragmentIsIdentical("Expected exact match", frag, controlXml);
    }

    public void assertFragmentIsIdentical(String message, Fragment frag, String controlXml) {
        CustomDifferenceListener cdl = new CustomDifferenceListener();
        try {
            Diff diff = new Diff(controlXml, frag.getPrettyXml());
            diff.overrideDifferenceListener(cdl);
            boolean identical = diff.identical();
            assertTrue(message + ";\n" + cdl.getDifferences(), identical);
        } catch (AssertionError ae) {
            frag.prettyPrint();
            logger.error(cdl.getDifferences().toString());
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
