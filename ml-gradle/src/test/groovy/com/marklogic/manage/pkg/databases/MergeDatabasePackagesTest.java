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
    public void addRangeIndex() {
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
