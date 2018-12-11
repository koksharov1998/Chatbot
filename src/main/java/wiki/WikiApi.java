package wiki;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WikiApi {

  public static String getWikiInformation(String request) {

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
      StringBuilder sm = new StringBuilder();
      if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
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
        System.out.println(sm.toString());
        return sm.toString();
      }
    } catch (Throwable cause) {
      cause.printStackTrace();
      return "We don't know, what is it.";
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return "!";
  }

  private static String getTagValue(String tag, Element element) {
    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node node = (Node) nodeList.item(0);
    return node.getNodeValue();
  }
}