package org.rozenberg.craftshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.controller.command.*;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(RequestParameter.COMMAND);
        logger.log(Level.DEBUG, "command name: {}", commandName);
        Optional<Command> command = CommandProvider.getCommand(commandName);
        logger.log(Level.DEBUG, "command: {}", command);
        if(command.isPresent()) {
            Router router = command.get().execute(request);
            switch (router.getRouterType()) {
                case FORWARD -> {
                    request.getRequestDispatcher(router.getPagePath()).forward(request, response);
                    logger.log(Level.DEBUG, "forward to {}; {}; {}", router.getPagePath(), commandName, command);
                }
                case REDIRECT -> {
                    response.sendRedirect(router.getPagePath());
                    logger.log(Level.DEBUG, "redirect to {}", router.getPagePath());
                }
                default -> {
                    logger.log(Level.ERROR, "incorrect router type: {}", router.getRouterType());
                    response.sendRedirect(PagePath.ERROR_500_PAGE);
                    logger.log(Level.DEBUG, "redirect to {}", router.getPagePath());
                }
            }
        }
        else {
            logger.log(Level.ERROR, "incorrect command name: {}", commandName);
            response.sendRedirect(PagePath.ERROR_500_PAGE);
            logger.log(Level.DEBUG, "redirect to error page");
        }
    }
}

