package com.codflix.backend.features.user;

import com.codflix.backend.core.Conf;
import com.codflix.backend.core.Database;
import com.codflix.backend.core.Template;
import com.codflix.backend.models.User;
import com.codflix.backend.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserDao userDao = new UserDao();

    public String login(Request request, Response response) throws NoSuchAlgorithmException {

        if (request.requestMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            return Template.render("auth_login.html", model);
        }

        // Get parameters
        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String email = query.get("email");
        String password = query.get("password");

        String mailH = hashingpass(email);
        String passH = hashingpass(password);


        // Authenticate user
        User user = userDao.getUserByCredentials(mailH, passH);
        if (user == null) {
            logger.info("User not found. Redirect to login");
            response.removeCookie("session");
            response.redirect("/login");
            return "KO";
        }

        // Create session
        Session session = request.session(true);
        session.attribute("user_id", user.getId());
        response.cookie("/", "user_id", "" + user.getId(), 3600, true);

        // Redirect to medias page
        response.redirect(Conf.ROUTE_LOGGED_ROOT);
        return "OK";
    }



    public String signUp(Request request, Response response) throws Exception {
        if (request.requestMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            return Template.render("auth_signup.html", model);
        }



        // Get parameters
        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String email = query.get("email");
        String password = query.get("password");
        String confirm = query.get("password_confirm");

        String passhash = hashingpass(password);
        String mailH = hashingpass(email);
        //send mail
        //String emailEntreprise ="didax.contact@gmail.com";
        //String passwordEntreprise = "escapefromtarkov";
        //MailControler.sendMessage(emailEntreprise,passwordEntreprise,email);

        try{

            Connection connection = Database.get().getDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (EMAIL,PASSWORD) VALUES (?,?)");
            preparedStatement.setString(1,mailH);
            //System.out.println("avant : "+passhash);
            preparedStatement.setString(2,passhash);
            preparedStatement.executeUpdate();
            //System.out.println("aprés : "+passhash);


            User user = userDao.getUserByCredentials(mailH, passhash);


            if (user == null) {
                logger.info("User not found. Redirect to login");
                response.removeCookie("session");
                response.redirect("/login");
                return "KO";
            }

            // Create session
            Session session = request.session(true);
            session.attribute("user_id", user.getId());
            response.cookie("/", "user_id", "" + user.getId(), 3600, true);

            // Redirect to medias page
            response.redirect(Conf.ROUTE_LOGGED_ROOT);
            return "OK";
        } catch (SQLException throwables) {
            return "Bad";
        }
    }

    public String logout(Request request, Response response) {
        Session session = request.session(false);
        if (session != null) {
            session.invalidate();
        }
        response.removeCookie("session");
        response.removeCookie("JSESSIONID");
        response.redirect("/");

        return "";
    }

    public String hashingpass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the tab of bits at a format hexadécimal - méthode 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

}
