package wiki;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONArray;

public class WikiApi {

  public static String getWikiInformation(String request) {

    request = request.replace(" ", "_");
    String query = "https://en.wikipedia.org/w/api.php?action=opensearch&search=" + request
        + "&limit=1&format=json&utf8=1";
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
          JSONArray array = new JSONArray(line);
          List l = array.toList();
          String name = "<b>" + l.get(1).toString().substring(1, l.get(1).toString().length()-1) + "</b>";
          String inf = "<i>" + l.get(2).toString().substring(1, l.get(2).toString().length()-1) + "</i>";
          String ref = "<a href=\"" + l.get(3).toString().substring(1, l.get(3).toString().length()-1) + "\"> Источник</a>";

          sm.append(name);
          sm.append("\n");
          sm.append(inf);
          sm.append("\n");
          sm.append(ref);
          sm.append("\n");
        }
        return sm.toString();
      }
    } catch (Throwable cause) {
      return "We don't know, what is it.";
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return "!";
  }
}