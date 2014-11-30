package sample;

import org.junit.Test;

import com.marklogic.client.helper.DatabaseClientConfig;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.StringHandle;
import com.marklogic.test.jdom.Fragment;

public class SearchDocumentsTest extends AbstractSampleProjectTest {

    /**
     * This is a simple test to show how a Spring bean can be manually retrieved from the Spring container. We also
     * confirm that certain properties have been read from gradle.properties.
     */
    @Test
    public void testConfig() {
        DatabaseClientConfig config = getApplicationContext().getBean(DatabaseClientConfig.class);
        assertEquals("localhost", config.getHost());
        assertEquals(8100, config.getPort());
    }

    @Test
    public void writeAndReadDocument() {
        loadPerson("/jane.xml", "Jane", "This is a sample document");

        String xml = getClient().newXMLDocumentManager().read("/jane.xml", new StringHandle()).get();
        Fragment frag = parse(xml);
        frag.assertElementValue("/sample:person/sample:name", "Jane");
        frag.assertElementValue("/sample:person/sample:description", "This is a sample document");
        assertFragmentIsIdentical(
                "This is an example of using XMLUnit libraries to assert on the equality of XML fragments", frag,
                readTestClassResource("jane-sample.xml"));
    }

    @Test
    public void twoDocuments() {
        loadPerson("/jane.xml", "Jane", "This is a sample document");
        loadPerson("/john.xml", "John", "This is another sample document");
    }

    private void loadPerson(String uri, String name, String description) {
        String xml = format(
                "<person xmlns='http://marklogic.com/sample'><name>%s</name><description>%s</description></person>",
                name, description);
        getClient().newXMLDocumentManager().write(uri, new StringHandle(xml).withFormat(Format.XML));
    }
}
