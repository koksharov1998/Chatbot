package wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiApi {

  public static String getWikiInformation(String request) {

    request = request.replace(" ", "_");
    String query = "https://en.wikipedia.org/w/api.php?action=opensearch&search=" + request
        + "&limit=1&format=json";
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
          System.out.println(line);
          Pattern p = Pattern.compile(",[\"(.+)\",\"(.+)\"],\"(.+\")");
          Matcher m = p.matcher(line);
          System.out.println(m.group(1));

          sm.append(line);
          sm.append("\n");
        }
        return sm.toString();
      }
    } catch (IndexOutOfBoundsException cause) {
      return "We don't know, what is it.";
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return "!";
  }
}