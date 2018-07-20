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

/**
 * Created by mycola on 10.05.2018.
 */
public class Xml {


    public static boolean readBuild(int build) {
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

        //проверка что билд не прерван
        Element result = (Element) document.getElementsByTagName("result").item(0);
        if (result.getTextContent().equals("ABORTED")) {
            System.out.println("Build result is ABORTED, skipped.");
            return false;
        }

        Element parameters = (Element) document.getElementsByTagName("parameters").item(0);
        NodeList nodeList = parameters.getChildNodes();
        String area = "";

        for (int i=0; i<nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodeList.item(i);
                String name = el.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals("area")) {
                    area = el.getElementsByTagName("value").item(0).getTextContent();
                    break;
                }
            }
        }

        //чтение результатов запуска билдов из json-файлов
        for (File myFile : new File("result\\").listFiles()) {
            if (myFile.isFile()) {
                Run run = Json.readData(myFile.getPath());
                if (null == run) continue;
                run.setBuild(build);
                run.setArea(area);
                Values.runs.add(run);
            }
        }
        return true;
    }

}
