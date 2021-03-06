import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import wiki.Api;
import wiki.WikiApi;


public class WikiApiTest {

  @Test
  void shouldCatchSAXException() throws IOException, SAXException, ParserConfigurationException {
    Api api = mock(Api.class);
    when(api.getHTMLString("abracadabra1")).thenThrow(new SAXException());
    //TODO
  }

  @Test
  void shouldCatchIOException() throws IOException, SAXException, ParserConfigurationException {
    Api api = mock(Api.class);
    when(api.getHTMLString("abracadabra2")).thenThrow(new IOException());
    //TODO
  }

  @Test
  void shouldCatchParserConfigurationException() throws IOException, SAXException, ParserConfigurationException {
    Api api = mock(Api.class);
    when(api.getHTMLString("abracadabra3")).thenThrow(new ParserConfigurationException());
    //TODO
  }

  @Test
  void returnRightHTMLString() throws IOException, SAXException, ParserConfigurationException {
    WikiApi wiki = new WikiApi();
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
    String result = wiki.getHTMLString(testXML);
    String expected = "<b>Russia</b>\n"
        + "<i>\n"
        + "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.\n"
        + "</i>\n"
        + "<a href=\"https://en.wikipedia.org/wiki/Russia\"> Источник</a>\n";
    assertEquals(expected, result);
  }

  @Test
  void returnRightInformationFromXML() throws ParserConfigurationException {
    WikiApi wiki = new WikiApi();
    Element item = createTestXML();
    assertEquals("Russia", wiki.getTagValue("Text", item));
    assertEquals(
        "Russia (Russian: Росси́я, tr. Rossiya, IPA: [rɐˈsʲijə]), officially the Russian Federation (Russian: Росси́йская Федера́ция, tr.",
        wiki.getTagValue("Description", item));
    assertEquals("https://en.wikipedia.org/wiki/Russia", wiki.getTagValue("Url", item));
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
