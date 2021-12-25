package com.gunnarro.web.utillity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HtmlParserTest {

    @Test
    void parseHtml() throws IOException {
        String baseUrl = "https://www.finn.no";
        String htmlPage = "/car/used/ad.html?finnkode=216949100";
        Document doc = Jsoup.connect(baseUrl + htmlPage).get();

        String model = ".grid__unit u-pa8";
        String title = "h1.u-t2 u-word-break";
        String id = "#pf-bfft-links";
        System.out.println("title: " + doc.select(title));
        Elements elements = doc.select("div.media__body");
        for (Element element : elements) {
            for (Element div : element.getElementsByClass("u-strong")) {
                System.out.println(div.text());
            }
        }

        System.out.println("Farge:" + doc.getElementsMatchingOwnText("Farge").next().text());
    }
}
