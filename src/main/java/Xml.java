import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mycola on 10.05.2018.
 */
public class Xml {


    public static void readBuild(String build) {
        String fileName = Values.buildPath + build + "\\" + Values.xmlName;
        File f = new File(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(f);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element parameters = (Element) document.getElementsByTagName("parameters").item(0);

        String browser = "";
        String resolution = "";
        String area = "";
        String language = "";

        NodeList nodeList = parameters.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                Element el = (Element) nodeList.item(i);
                String name = el.getElementsByTagName("name").item(0).getTextContent();
                String value = el.getElementsByTagName("value").item(0).getTextContent();

                switch (name) {
                    case "browser" : {
                        browser = value;
                    } break;
                    case "resolution" : {
                        resolution = value;
                    }break;
                    case "area" : {
                        area = value;
                    }break;
                    case "language_currency" : {
                        language = value;
                    }
                }
            }
        }

        for (int i=0; i<nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                Element el = (Element) nodeList.item(i);
                String name = el.getElementsByTagName("name").item(0).getTextContent();

                if (name.contains("part")) {
                    String value = el.getElementsByTagName("value").item(0).getTextContent();
                    if (value.equals("true")) {
                        Run run = new Run();
                        run.setBuild(build);
                        run.setBrowser(browser);
                        run.setResolution(resolution);
                        run.setArea(area);
                        run.setPart(name.replaceAll("\\D+",""));
                        run.setLanguage(language);

                        //чтение результатов запуска билдов из json-файлов
                        for (File myFile : new File("result\\").listFiles()) {
                            if (myFile.isFile()) {
                                Json.readData(run, myFile.getPath());
                            }
                            if (run.getStatus() != null) break;
                        }

                        Values.runs.add(run);
                    }
                }
            }
        }
    }

}
