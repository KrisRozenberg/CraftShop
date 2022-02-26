package org.rozenberg.craftshop.controller.command.impl.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.controller.command.*;

public class ChangeLanguageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) {
        String language = request.getParameter(RequestParameter.LANGUAGE);
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.LOCAL, language);
        if (session.getAttribute(SessionAttribute.CURRENT_PAGE) == null) {
            return new Router(PagePath.INDEX, Router.RouterType.FORWARD);
        }
        logger.log(Level.DEBUG, "request parameter: {}; session attribute: {}", language, session.getAttribute(SessionAttribute.LOCAL));
        return new Router((String) session.getAttribute(SessionAttribute.CURRENT_PAGE), Router.RouterType.FORWARD);
    }
}
