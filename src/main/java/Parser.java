import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {
    private static Document getPage() throws IOException {
        String url = "http://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }

    private static int printFourValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            switch (values.size()){
                case 19:
                    iterationCount = 3;
                    break;
                case 18:
                    iterationCount = 2;
                    break;
                case 17:
                    iterationCount = 1;
                    break;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "  ");
            }
            System.out.println();
        }
        return iterationCount;

    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWeather = page.select("table[class=wt]").first();
        Elements names = tableWeather.select("tr[class=wth]");
        Elements values = tableWeather.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + " Явление  Температура Давление Влажность Ветер");
            int iterationCount = printFourValues(values, index);
            index = iterationCount + index;
        }
    }
}
