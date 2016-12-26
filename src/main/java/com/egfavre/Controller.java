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
    public String welcome() {
        return "welcome";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit() throws Exception {
        return "edit";
    }

    @RequestMapping(path = "/scrape", method = RequestMethod.POST)
    public String scrape() throws Exception {

        //get document
        Document doc = Jsoup.connect("http://www.tmz.com").get();

        //tag text String
        Element body = doc.body();
        String bodyString = (body.text()).toString();
        tagged = tagger.tagString(bodyString);

        //put tagged words into a list
        String[] allTaggedWords = tagged.split("\\s+");

        //separate words into separate lists based on POS
        ArrayList<String> nouns = new ArrayList<>();
        for (String noun : allTaggedWords) {
            if ((noun.endsWith("_NN"))) {
                nouns.add(noun);
            }
        }
        System.out.println("there are " + nouns.size() + " nouns");

        ArrayList<String> pluralNouns = new ArrayList<>();
        for (String pns : allTaggedWords) {
            if ((pns.endsWith("_NNS"))) {
                pluralNouns.add(pns);
            }
        }
        System.out.println("there are " + pluralNouns.size() + " plural nouns");

        ArrayList<String> adjectives = new ArrayList<>();
        for (String adj : allTaggedWords) {
            if ((adj.endsWith("_JJ"))) {
                adjectives.add(adj);
            }
        }
        System.out.println("there are " + adjectives.size() + " adjectives");

        ArrayList<String> adverbs = new ArrayList<>();
        for (String adv : allTaggedWords) {
            if ((adv.endsWith("_RB"))) {
                adverbs.add(adv);
            }
        }
        System.out.println("there are " + adverbs.size() + " adverbs");

        ArrayList<String> interjections = new ArrayList<>();
        for (String interj : allTaggedWords) {
            if ((interj.endsWith("_UH"))) {
                interjections.add(interj);
            }
        }
        System.out.println("there are " + interjections.size() + " interjections");

        ArrayList<String> verbs = new ArrayList<>();
        for (String verb : allTaggedWords) {
            if ((verb.endsWith("_VB"))) {
                verbs.add(verb);
            }
        }
        System.out.println("there are " + verbs.size() + " verbs");

        ArrayList<String> pastTenseVerbs = new ArrayList<>();
        for (String ptv : allTaggedWords) {
            if ((ptv.endsWith("_VBD"))) {
                pastTenseVerbs.add(ptv);
            }
        }
        System.out.println("there are " + pastTenseVerbs.size() + " past tense verbs");

        return "redirect:/edit";
    }
}

    /*
        JJ Adjective
        NN Noun, singular or mass
        NNS Noun, plural
        RB Adverb
        UH Interjection
        VB Verb, base form
        VBD Verb, past tense
       */

 /*
        Elements paragraphs = doc.select("p");

        //convert element text to string
        ArrayList<String> paraStrings = new ArrayList<>();
        for(Element p : paragraphs) {
            String nextParaString = (p.text()).toString();
            paraStrings.add(nextParaString);
        }*/