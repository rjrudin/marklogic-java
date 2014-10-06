package com.marklogic.manage.pkg.databases;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Namespace;
import org.junit.Test;

import com.marklogic.test.XmlHelper;
import com.marklogic.test.jdom.Fragment;
import com.marklogic.test.jdom.MarkLogicNamespaceProvider;
import com.marklogic.test.jdom.NamespaceProvider;

public class MergeDatabasePackagesTest extends XmlHelper {

    private DatabasePackageMerger sut = new DatabasePackageMerger();

    @Test
    public void mergePackages() {
        List<String> mergeFilePaths = new ArrayList<>();
        mergeFilePaths.add("src/test/resources/test-content-database.xml");
        mergeFilePaths.add("src/test/resources/test-content-database2.xml");

        String xml = sut.mergeDatabasePackages(mergeFilePaths);
        Fragment db = parse(xml);

        db.assertElementExists("/db:package-database");

        String indexPath = "/db:package-database/db:config/db:package-database-properties/db:range-element-indexes/db:range-element-index";
        db.assertElementExists(indexPath
                + "[db:scalar-type = 'dateTime' and db:namespace-uri = 'http://marklogic.com/test' and db:localname = 'testDateTime']");
        db.assertElementExists(indexPath
                + "[db:scalar-type = 'dateTime' and db:namespace-uri = 'http://marklogic.com/test' and db:localname = 'secondTestDateTime']");
        db.assertElementExists("//db:word-positions[. = 'true']");
        db.assertElementExists("//db:collection-lexicon[. = 'true']");

        db.assertElementExists("//db:geospatial-element-indexes/db:geospatial-element-index[db:localname = 'testRegion']");

        db.assertElementExists("/db:package-database/db:config/db:links/db:schema-database[. = 'my-schemas-database']");
        db.assertElementExists("/db:package-database/db:config/db:links/db:security-database[. = 'my-security-database']");
        db.assertElementExists("/db:package-database/db:config/db:links/db:triggers-database[. = 'my-triggers-database']");

        db.assertElementExists("/db:package-database/db:config/db:package-database-properties/db:word-lexicons/db:word-lexicon[. = 'http://marklogic.com/collation/']");

        db.assertElementExists("//db:range-field-indexes/db:range-field-index[db:scalar-type = 'int' and db:field-name = 'intField']");
        //db.assertElementExists("//db:fields/db:field[db:field-name = 'intField' and db:field-path[db:path = '/some/int/element' and db:weight = '1']]");

        db.assertElementExists("//db:range-field-indexes/db:range-field-index[db:scalar-type = 'string' and db:field-name = 'secondField']");
        //db.assertElementExists("//db:fields/db:field[db:field-name = 'secondField' and db:field-path[db:path = '/some/other/element' and db:weight = '1']]");

        db.prettyPrint();
    }

    @Override
    protected NamespaceProvider getNamespaceProvider() {
        return new MarkLogicNamespaceProvider() {
            @Override
            protected List<Namespace> buildListOfNamespaces() {
                List<Namespace> list = super.buildListOfNamespaces();
                list.add(Namespace.getNamespace("db", "http://marklogic.com/manage/package/databases"));
                return list;
            }

        };
    }

}
