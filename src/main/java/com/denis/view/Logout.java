package com.denis.view;

import com.denis.control.Protector;
import com.denis.domain.configs.ConfigFactory;
import com.denis.domain.configs.ConfigNames;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Logout extends HttpServlet {
    private Logger logger;
    private Protector protector;
    private Configuration exceptionConfig;

    public Logout() {
        logger = LogManager.getLogger();
        protector = Protector.getInstance();
        exceptionConfig = ConfigFactory.getConfigByName(ConfigNames.EXCEPTIONS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        protector.logoutUser(req, resp);
        resp.sendRedirect("/timeTracker/index.html");
    }
}
