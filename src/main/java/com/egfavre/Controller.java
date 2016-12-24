package com.egfavre;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.HtmlArticleExtractor;
import javafx.scene.web.WebView;
import org.h2.tools.Server;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import static javafx.scene.input.KeyCode.R;


/**
 * Created by user on 12/9/16.
 */

@org.springframework.stereotype.Controller
public class Controller {

    org.jsoup.nodes.Document doc;
    final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
    final HtmlArticleExtractor htmlExtr = HtmlArticleExtractor.INSTANCE;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String welcome () {
        return "welcome";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit (Model model) throws Exception{
        model.addAttribute("doc", doc);

        return "edit";
    }

    @RequestMapping(path = "/scrape", method = RequestMethod.POST)
    public String scrape() throws Exception{
        URL url = new URL(
                "http://www.tmz.com");
        String text = htmlExtr.process(extractor, url);

        System.out.println(text);

        Reader stringReader = new StringReader(text);
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
        HTMLEditorKit.Parser parser = new ParserDelegator();
        parser.parse(stringReader, htmlDoc.getReader(0), true);

        System.out.println(parser.toString());


        return "redirect:/edit";
    }
}
