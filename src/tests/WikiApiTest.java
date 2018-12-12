import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import wiki.WikiApi;

public class WikiApiTest {

  @Test
  void returnRight() {

  }

  @Test
  void returnRightInformationFromXML() throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();
    Element item = doc.createElement("Item");
    Element node = doc.createElement("Text");
    String text = "Russia";
    node.appendChild(doc.createTextNode(text));
    item.appendChild(node);
    node = doc.createElement("Description");
    String description = "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.";
    node.appendChild(doc.createTextNode(description));
    item.appendChild(node);
    node = doc.createElement("Url");
    String url = "https://en.wikipedia.org/wiki/Russia";
    node.appendChild(doc.createTextNode(url));
    item.appendChild(node);
    assertEquals(WikiApi.getTagValue("Text", item), text);
    assertEquals(WikiApi.getTagValue("Description", item), description);
    assertEquals(WikiApi.getTagValue("Url", item), url);
  }
}
