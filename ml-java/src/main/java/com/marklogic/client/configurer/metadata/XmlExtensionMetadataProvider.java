package com.marklogic.client.configurer.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import com.marklogic.client.admin.ExtensionMetadata;
import com.marklogic.client.admin.MethodType;
import com.marklogic.client.admin.ResourceExtensionsManager.MethodParameters;
import com.marklogic.util.LoggingObject;

public class XmlExtensionMetadataProvider extends LoggingObject implements ExtensionMetadataProvider {

    @Override
    public ExtensionMetadataAndParams provideExtensionMetadataAndParams(File resourceFile) {
        File metadataDir = new File(resourceFile.getParent(), "metadata");
        File metadataFile = new File(metadataDir, resourceFile.getName().replaceAll("xqy", "xml"));

        ExtensionMetadata m = new ExtensionMetadata();
        List<MethodParameters> paramList = new ArrayList<>();

        if (metadataFile.exists()) {
            try {
                Element root = new SAXBuilder().build(metadataFile).getRootElement();
                m.setTitle(root.getChildText("title"));
                Element desc = root.getChild("description");
                if (desc.getChildren() != null && desc.getChildren().size() == 1) {
                    m.setDescription(new XMLOutputter().outputString(desc.getChildren().get(0)));
                } else {
                    m.setDescription(desc.getText());
                }
                for (Element method : root.getChildren("method")) {
                    MethodParameters mp = new MethodParameters(MethodType.valueOf(method.getAttributeValue("name")));
                    paramList.add(mp);
                    for (Element param : method.getChildren("param")) {
                        String name = param.getAttributeValue("name");
                        String type = "xs:string";
                        if (param.getAttribute("type") != null) {
                            type = param.getAttributeValue("type");
                        }
                        mp.add(name, type);
                    }
                }
            } catch (Exception e) {
                logger.warn("Unable to build metadata from resource file: " + resourceFile.getAbsolutePath()
                        + "; cause: " + e.getMessage(), e);
                setDefaults(m, resourceFile);
            }
        } else {
            setDefaults(m, resourceFile);
        }

        return new ExtensionMetadataAndParams(m, paramList);
    }

    private void setDefaults(ExtensionMetadata metadata, File resourceFile) {
        metadata.setTitle(resourceFile.getName().replaceAll(".xqy", ""));
    }
}
