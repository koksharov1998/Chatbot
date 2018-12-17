package wiki;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WikiApi {

  public String getWikiInformation(String request) {
    request = request.replace(" ", "_");
    String query = "https://en.wikipedia.org/w/api.php?action=opensearch&search=" + request
        + "&limit=1&format=xml";
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) new URL(query).openConnection();
      connection.setUseCaches(false);
      connection.setConnectTimeout(250);
      connection.setReadTimeout(250);
      connection.connect();
      if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = in.readLine();
        return getHTMLString(line);
      } else {
        return "Sorry, there are problems with Internet connection! Try later.";
      }
    } catch (SAXException e) {
      return "Some errors in response XML file.";
    } catch (ParserConfigurationException e) {
      return "Some errors in parsing of response XML file.";
    } catch (IOException e) {
      return "Some errors in connection with specified URL.";
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public String getHTMLString(String line)
      throws ParserConfigurationException, IOException, SAXException {
    StringBuilder sm = new StringBuilder();
    if (line != null) {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder();
      Document document = documentBuilder.parse(new ByteArrayInputStream(line.getBytes()));
      NodeList nodeList = document.getElementsByTagName("Item");
      Element element = (Element) nodeList.item(0);
      String name = "<b>" + getTagValue("Text", element) + "</b>";
      String inf = "<i>" + getTagValue("Description", element) + "</i>";
      String ref = "<a href=\"" + getTagValue("Url", element) + "\"> Источник</a>";
      sm.append(name);
      sm.append("\n");
      sm.append(inf);
      sm.append("\n");
      sm.append(ref);
      sm.append("\n");
    }
    return sm.toString();
  }

  public String getTagValue(String tag, Element element) {
    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node node = (Node) nodeList.item(0);
    return node.getNodeValue();
  }
}