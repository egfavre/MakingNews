package com.egfavre;


import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.h2.tools.Server;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by user on 12/9/16.
 */

@org.springframework.stereotype.Controller
public class Controller {

    MaxentTagger tagger = new MaxentTagger("/Users/user/IdeaProjects/MakingNews/models/english-bidirectional-distsim.tagger");


    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String welcome () {
        return "welcome";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit () throws Exception{


        return "edit";
    }

    @RequestMapping(path = "/scrape", method = RequestMethod.POST)
    public String scrape() throws Exception{
        //get document
        Document doc = Jsoup.connect("http://www.tmz.com").get();
        Elements paragraphs = doc.select("p");

        //convert element text to string
        ArrayList<String> paraStrings = new ArrayList<>();
        for(Element p : paragraphs) {
            String nextParaString = (p.text()).toString();
            paraStrings.add(nextParaString);
        }

        //add POS tagging
        ArrayList<String> taggedParaStrings = new ArrayList<>();
        for (String ps:paraStrings) {
            String tagged = tagger.tagString(ps);
            taggedParaStrings.add(tagged);
            System.out.println(tagged);
        }

        return "redirect:/edit";
    }
}
