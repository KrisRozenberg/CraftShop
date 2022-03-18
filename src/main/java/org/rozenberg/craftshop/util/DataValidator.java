package org.rozenberg.craftshop.util;

public class DataValidator {

    private static final String NAME_AND_SURNAME_PATTERN = "^[A-ZА-Я]{1}[a-zа-я]{2,20}$";
    private static final String LOGIN_PATTERN = "[A-Za-z0-9]{3,20}";
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}";
    private static final String EMAIL_PATTERN = "^(?=.{1,30}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

//    private static final String CHECK_IMAGE_URL = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";
//    private static final String CHECK_MONEY_REGEX = "^[0-9]{1,3}(\\.[0-9]{1,2})?$";
//    private static final String LIMIT_FOR_RECHARGED_MONEY_REGEX = "^[0-9]{1,2}(\\.[0-9]{1,2})?$";
//    private static final int MAX_STRING_LENGTH = 90;

    private static DataValidator instance;

    private DataValidator() {
    }

    public static DataValidator getInstance() {
        if (instance == null) {
            instance = new DataValidator();
        }
        return instance;
    }

    public boolean isNameValid(String name) {
        return !isNotNullOrEmpty(name) || name.matches(NAME_AND_SURNAME_PATTERN);
    }

    public boolean isSurnameValid(String surname) {
        return !isNotNullOrEmpty(surname) || surname.matches(NAME_AND_SURNAME_PATTERN);
    }

    public boolean isLoginValid(String login) {
        return isNotNullOrEmpty(login) && login.matches(LOGIN_PATTERN);
    }

    public boolean isPasswordValid(String password) {
        return isNotNullOrEmpty(password) && password.matches(PASSWORD_PATTERN);
    }

    public boolean isEmailValid(String email) {
        return isNotNullOrEmpty(email) && email.matches(EMAIL_PATTERN);
    }

//    public boolean checkMoney(String money) {
//        return money != null && money.matches(CHECK_MONEY_REGEX);
//    }
//
//    public boolean checkRechargedMoney(String money) {
//        return money != null && money.matches(LIMIT_FOR_RECHARGED_MONEY_REGEX);
//    }
//
//    public boolean checkImageUrl(String imageUrl) {
//        return imageUrl != null && imageUrl.matches(CHECK_IMAGE_URL);
//    }
//
//    public boolean checkOnMaxLength(String field) {
//        return field != null && field.length() <= MAX_STRING_LENGTH;
//    }

    private boolean isNotNullOrEmpty(String data){
        return data != null && !data.isEmpty();
    }
}
