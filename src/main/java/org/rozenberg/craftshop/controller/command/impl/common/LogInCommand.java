package org.rozenberg.craftshop.controller.command.impl.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.controller.command.*;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.entity.UserStatus;
import org.rozenberg.craftshop.model.service.InvoiceService;
import org.rozenberg.craftshop.model.service.UserService;
import org.rozenberg.craftshop.model.service.impl.InvoiceServiceImpl;
import org.rozenberg.craftshop.model.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;


public class LogInCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String INCORRECT_INPUT = "logIn.invalidMessage";
    private static final String USER_IS_BLOCKED = "account.blocked";
    private final UserService userService = UserServiceImpl.getInstance();
    private final InvoiceService invoiceService = InvoiceServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request) {
        String login = request.getParameter(RequestParameter.LOGIN);
        logger.log(Level.DEBUG, "login: {}", login);
        String password = request.getParameter(RequestParameter.PASSWORD);
        logger.log(Level.DEBUG, "password: {}", password);

        Optional<User> optionalUser;
        try {
            optionalUser = userService.signIn(login, password);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getUserStatus() == UserStatus.BLOCKED) {
                    request.setAttribute(RequestAttribute.ERROR_MESSAGE, USER_IS_BLOCKED);
                    logger.log(Level.ERROR, "Failed to execute request SignInCommand: User is not active");
                    return new Router(PagePath.LOG_IN, Router.RouterType.FORWARD);
                }
                HttpSession session = request.getSession();
                session.setAttribute(SessionAttribute.AUTHORIZATION, true);
                session.setAttribute(SessionAttribute.USER_ID, user.getUserId());
//                session.setAttribute(SessionAttribute.USER_NAME, user.getName());
//                session.setAttribute(SessionAttribute.USER_SURNAME, user.getSurname());
                session.setAttribute(SessionAttribute.USER_LOGIN, user.getLogin());
                session.setAttribute(SessionAttribute.USER_EMAIL, user.getEmail());
                session.setAttribute(SessionAttribute.USER_ROLE, user.getRole());
                session.setAttribute(SessionAttribute.USER_INVOICE_ID, user.getInvoiceId());
                switch (user.getRole()) {
                    case ADMIN -> {return new Router(PagePath.TEST, Router.RouterType.REDIRECT);}
                    case CLIENT -> {
                        BigDecimal money = invoiceService.getMoney(user.getUserId()).get();
                        BigDecimal discount = invoiceService.getDiscount(user.getUserId()).get();
                        session.setAttribute(SessionAttribute.USER_MONEY, money);
                        session.setAttribute(SessionAttribute.USER_DISCOUNT, discount);
                        return new Router(PagePath.TEST, Router.RouterType.REDIRECT);
                    }
                    default -> {
                        logger.log(Level.ERROR, "Failed to sign in by user with unknown role");
                        request.setAttribute(RequestAttribute.EXCEPTION, "Failed to sign in by user with unknown role");
                        return new Router(PagePath.ERROR_500_PAGE, Router.RouterType.FORWARD);
                    }
                }
            } else {
                logger.log(Level.ERROR, "Failed to execute request LoginUserCommand: Invalid login or password");
                request.setAttribute(RequestAttribute.ERROR_MESSAGE, INCORRECT_INPUT);
                return new Router(PagePath.LOG_IN, Router.RouterType.FORWARD);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Failed to execute request SignInCommand: ", e);
            request.setAttribute(RequestAttribute.EXCEPTION, e);
            return new Router(PagePath.ERROR_500_PAGE, Router.RouterType.FORWARD);
        }
    }
}
