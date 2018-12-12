import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import wiki.WikiApi;

public class WikiApiTest {

  @Test
  void returnRightHTMLString() throws IOException, SAXException, ParserConfigurationException {
    String testXML =
        "<SearchSuggestion xmlns=\"http://opensearch.org/searchsuggest2\" version=\"2.0\">\n"
            + "<Query xml:space=\"preserve\">russia</Query>\n"
            + "<Section>\n"
            + "<Item>\n"
            + "<Text xml:space=\"preserve\">Russia</Text>\n"
            + "<Url xml:space=\"preserve\">https://en.wikipedia.org/wiki/Russia</Url>\n"
            + "<Description xml:space=\"preserve\">\n"
            + "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.\n"
            + "</Description>\n"
            + "<Image source=\"https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/50px-Flag_of_Russia.svg.png\" width=\"50\" height=\"33\"/>\n"
            + "</Item>\n"
            + "</Section>\n"
            + "</SearchSuggestion>";
    String result = WikiApi.getHTMLString(testXML);
    String expected = "<b>Russia</b>\n"
        + "<i>\n"
        + "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.\n"
        + "</i>\n"
        + "<a href=\"https://en.wikipedia.org/wiki/Russia\"> Источник</a>\n";
    assertEquals(expected, result);
  }

  @Test
  void returnRightInformationFromXML() throws ParserConfigurationException {
    Element item = createTestXML();
    assertEquals("Russia", WikiApi.getTagValue("Text", item));
    assertEquals(
        "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.",
        WikiApi.getTagValue("Description", item));
    assertEquals("https://en.wikipedia.org/wiki/Russia", WikiApi.getTagValue("Url", item));
  }

  private Element createTestXML() throws ParserConfigurationException {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element item = doc.createElement("Item");
    Element node = doc.createElement("Text");
    node.appendChild(doc.createTextNode("Russia"));
    item.appendChild(node);
    node = doc.createElement("Description");
    node.appendChild(doc.createTextNode(
        "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr."));
    item.appendChild(node);
    node = doc.createElement("Url");
    node.appendChild(doc.createTextNode("https://en.wikipedia.org/wiki/Russia"));
    item.appendChild(node);
    return item;
  }
}
