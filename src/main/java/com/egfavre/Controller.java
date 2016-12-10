package com.egfavre;

import org.h2.tools.Server;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.sql.SQLException;


/**
 * Created by user on 12/9/16.
 */

@org.springframework.stereotype.Controller
public class Controller {
    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String welcome () {
        return "welcome";
    }

}
