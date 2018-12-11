package java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import wiki.WikiApi;

public class WikiApiTest {


  @Test
  void returnRightStringOnRequest() {
    String rightStr = "<b>Tsar</b>\n<i>Tsar ( or ; Old Church Slavonic: ц︢рь [usually written thus with a title] or цар, царь), also spelled csar, or czar, is a title used to designate East and South Slavic monarchs or supreme rulers of Eastern Europe, originally Bulgarian monarchs from 10th century onwards.</i>\n<a href=\"https://en.wikipedia.org/wiki/Tsar\"> Источник</a>\n";
    String response = WikiApi.getWikiInformation("tsar");
    System.out.println(response);
    assertEquals(response, rightStr);
  }

  @Test
  void returnRightInformationFromXML() {

  }
}
