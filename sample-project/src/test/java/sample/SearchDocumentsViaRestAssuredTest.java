package sample;

import org.junit.Test;

/**
 * TODO This will show an example of using RestAssured instead of the ML Java API for performing a search.
 */
public class SearchDocumentsViaRestAssuredTest extends AbstractSampleProjectTest {

    @Test
    public void twoDocuments() {
        loadPerson("/jane.xml", "Jane", "This is a sample document");
        loadPerson("/john.xml", "John", "This is another sample document");
    }
}
