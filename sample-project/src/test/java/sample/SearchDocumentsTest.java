package sample;

import org.junit.Test;

public class SearchDocumentsTest extends AbstractSampleProjectTest {

    @Test
    public void twoDocuments() {
        loadPerson("/jane.xml", "Jane", "This is a sample document");
        loadPerson("/john.xml", "John", "This is another sample document");
    }
}
