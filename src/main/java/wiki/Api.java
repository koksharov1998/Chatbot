package wiki;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface Api {
  String getWikiInformation(String request);
  String getHTMLString(String line) throws ParserConfigurationException, IOException, SAXException;
}
