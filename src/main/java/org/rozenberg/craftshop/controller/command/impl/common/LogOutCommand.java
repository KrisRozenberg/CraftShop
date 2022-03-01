package org.rozenberg.craftshop.controller.command.impl.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.rozenberg.craftshop.controller.command.Command;
import org.rozenberg.craftshop.controller.command.PagePath;
import org.rozenberg.craftshop.controller.command.Router;
import org.rozenberg.craftshop.controller.command.SessionAttribute;

public class LogOutCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String language = (String) session.getAttribute(SessionAttribute.LOCAL);
        session.invalidate();
        session = request.getSession();
        session.setAttribute(SessionAttribute.LOCAL, language);
        return new Router(PagePath.INDEX, Router.RouterType.REDIRECT);
    }
}
