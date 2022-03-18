package org.rozenberg.craftshop.controller.command.impl.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.controller.command.*;
import org.rozenberg.craftshop.exception.ServiceException;
import org.rozenberg.craftshop.model.entity.Role;
import org.rozenberg.craftshop.model.entity.User;
import org.rozenberg.craftshop.model.entity.UserStatus;
import org.rozenberg.craftshop.model.service.UserService;
import org.rozenberg.craftshop.model.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class RegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private static final String INVALID_INPUT = "inputData.invalid";
    private static final String MISMATCHED_PASSWORDS = "mismatched.password";
    private static final String LOGIN_IS_NOT_FREE = "update.profile.login.notFree";
    private static final String EMAIL_IS_NOT_FREE = "update.profile.email.notFree";
    private static final String EMPTY_STRING = "";
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request) {
        String name = request.getParameter(RequestParameter.NAME);
        logger.log(Level.DEBUG, "Name: {}", name);
        String surname = request.getParameter(RequestParameter.SURNAME);
        logger.log(Level.DEBUG, "Surname: {}", surname);
        String login = request.getParameter(RequestParameter.LOGIN);
        logger.log(Level.DEBUG, "Login: {}", login);
        String password = request.getParameter(RequestParameter.PASSWORD);
        logger.log(Level.DEBUG, "Passwordt: {}", password);
        String repeatedPassword = request.getParameter(RequestParameter.REPEATED_PASSWORD);
        logger.log(Level.DEBUG, "Repeated password: {}", repeatedPassword);
        String email = request.getParameter(RequestParameter.EMAIL);
        logger.log(Level.DEBUG, "E-mail: {}", email);

        Map<String, String> data = new HashMap<>();
        data.put(RequestParameter.NAME, name);
        data.put(RequestParameter.SURNAME, surname);
        data.put(RequestParameter.LOGIN, login);
        data.put(RequestParameter.PASSWORD, password);
        data.put(RequestParameter.REPEATED_PASSWORD, repeatedPassword);
        data.put(RequestParameter.EMAIL, email);

        boolean isDataValid = userService.isRegistrationDataValid(data);
        if (!isDataValid) {
            request.setAttribute(RequestAttribute.FORM_DATA, data);
            request.setAttribute(RequestAttribute.ERROR_MESSAGE, INVALID_INPUT);
            return new Router(PagePath.REGISTRATION, Router.RouterType.FORWARD);
        }

        if (!password.equals(repeatedPassword)) {
            data.put(RequestParameter.REPEATED_PASSWORD, EMPTY_STRING);
            request.setAttribute(RequestAttribute.FORM_DATA, data);
            request.setAttribute(RequestAttribute.ERROR_MESSAGE, MISMATCHED_PASSWORDS);
            return new Router(PagePath.REGISTRATION, Router.RouterType.FORWARD);
        }

        try {
            boolean isLoginExist = userService.isLoginExist(login);
            if (isLoginExist) {
                data.put(RequestParameter.LOGIN, EMPTY_STRING);
                request.setAttribute(RequestAttribute.FORM_DATA, data);
                request.setAttribute(RequestAttribute.ERROR_MESSAGE, LOGIN_IS_NOT_FREE);
                return new Router(PagePath.REGISTRATION, Router.RouterType.FORWARD);
            }
            boolean isEmailExist = userService.isEmailExist(email);
            if (isEmailExist) {
                data.put(RequestParameter.EMAIL, EMPTY_STRING);
                request.setAttribute(RequestAttribute.FORM_DATA, data);
                request.setAttribute(RequestAttribute.ERROR_MESSAGE, EMAIL_IS_NOT_FREE);
                return new Router(PagePath.REGISTRATION, Router.RouterType.FORWARD);
            }

            HttpSession session = request.getSession();
//            String roleName = request.getParameter(RequestParameter.ROLE);
//            logger.log(Level.DEBUG, "Role name: {}", roleName);
////            Role role = Role.valueOf(roleName.toUpperCase());
            Role sessionRole = (Role) session.getAttribute(SessionAttribute.USER_ROLE);
            Role role = (sessionRole == Role.ADMIN) ? Role.ADMIN : Role.CLIENT;

            User user = new User(name, surname, login, password, email, role, UserStatus.ACTIVE);
            userService.create(user);
            return new Router(PagePath.TEST, Router.RouterType.REDIRECT);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Error to execute SignUpCommand", e);
            request.setAttribute(RequestAttribute.EXCEPTION, e);
            return new Router(PagePath.ERROR_500_PAGE, Router.RouterType.FORWARD);
        }
    }
}
