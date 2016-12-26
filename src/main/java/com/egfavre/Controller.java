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
    String tagged;

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
        Element body = doc.body();
        String bodyString = (body.text()).toString();
        tagged = tagger.tagString(bodyString);

        /*
        Elements paragraphs = doc.select("p");

        //convert element text to string
        ArrayList<String> paraStrings = new ArrayList<>();
        for(Element p : paragraphs) {
            String nextParaString = (p.text()).toString();
            paraStrings.add(nextParaString);
        }*/


        //count tagged POS's
        /*
        JJ Adjective
        NN Noun, singular or mass
        NNS Noun, plural
        RB Adverb
        UH Interjection
        VB Verb, base form
        VBD Verb, past tense
       */
        countNouns(tagged);
        countPluralNouns(tagged);
        countAdjectives(tagged);







        return "redirect:/edit";
    }

    public static void countNouns(String tagged) {
        String findStr = "_NN ";
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = tagged.indexOf(findStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += findStr.length();
            }
        }
        System.out.println("This stream contains " + count + " nouns.");

    }

    public static void countPluralNouns(String tagged) {
        String findStr = "_NNS ";
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = tagged.indexOf(findStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += findStr.length();
            }
        }
        System.out.println("This stream contains " + count + " plural nouns.");

    }

    public static void countAdjectives(String tagged) {
        String findStr = "_JJ ";
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = tagged.indexOf(findStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += findStr.length();
            }
        }
        System.out.println("This stream contains " + count + " adjectives.");

    }


}
