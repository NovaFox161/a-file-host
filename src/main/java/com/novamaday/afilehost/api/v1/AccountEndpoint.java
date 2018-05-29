package com.novamaday.afilehost.api.v1;

import com.novamaday.afilehost.account.AccountHandler;
import com.novamaday.afilehost.database.DatabaseManager;
import com.novamaday.afilehost.logger.Logger;
import com.novamaday.afilehost.objects.Confirmation;
import com.novamaday.afilehost.objects.SiteSettings;
import com.novamaday.afilehost.objects.User;
import com.novamaday.afilehost.utils.EmailHandler;
import com.novamaday.afilehost.utils.Generator;
import de.triology.recaptchav2java.ReCaptcha;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AccountEndpoint {
    public static String register(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("username") && body.has("email") && body.has("password") && body.has("gcap")) {
            if (new ReCaptcha(SiteSettings.RECAP_KEY.get()).isValid(body.getString("gcap"))) {
                String username = body.getString("username");
                String email = body.getString("email");
                if (!DatabaseManager.getManager().usernameOrEmailTaken(username, email)) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hash = encoder.encode(body.getString("password"));

                    DatabaseManager.getManager().addNewUser(username, email, hash);
                    User account = DatabaseManager.getManager().getUserFromEmail(email);

                    //Send confirmation email!!!
                    EmailHandler.getHandler().sendEmailConfirm(email, Generator.generateEmailConfirmationLink(account));

                    Map<String, Object> m = new HashMap<>();
                    m.put("year", LocalDate.now().getYear());
                    m.put("loggedIn", true);
                    m.put("account", account);
                    AccountHandler.getHandler().addAccount(m, request.session().id());

                    response.status(200);
                    response.body(new JSONObject().put("message", "Success").toString());
                } else {
                    response.status(400);
                    response.body("Username/Email taken!");
                }
            } else {
                response.status(400);
                response.body("Failed to verify ReCaptcha!");
            }
        } else {
            response.status(400);
            response.body("Invalid Request!");
        }
        return response.body();
    }

    public static String login(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("email") && body.has("password") && body.has("gcap")) {
            if (new ReCaptcha(SiteSettings.RECAP_KEY.get()).isValid(body.getString("gcap"))) {
                String email = body.getString("email");
                if (DatabaseManager.getManager().validLogin(email, body.getString("password"))) {

                    User account = DatabaseManager.getManager().getUserFromEmail(email);

                    Map<String, Object> m = new HashMap<>();
                    m.put("year", LocalDate.now().getYear());
                    m.put("loggedIn", true);
                    m.put("account", account);
                    AccountHandler.getHandler().addAccount(m, request.session().id());

                    Logger.getLogger().api("User logged into account: " + account.getUsername(), request.ip());

                    response.status(200);
                    response.body(new JSONObject().put("message", "Success!").toString());
                } else {
                    response.status(400);
                    response.body("Email/password invalid!");
                }
            } else {
                response.status(400);
                response.body("Failed to verify ReCaptcha!");
            }
        } else {
            response.status(400);
            response.body("Invalid Request!");
        }
        return response.body();
    }

    public static String logout(Request request, Response response) {
        AccountHandler.getHandler().removeAccount(request.session().id());
        response.redirect("/", 301);
        return response.body();
    }

    public static String confirmEmail(Request request, Response response) {
        if (request.queryParams().contains("code")) {
            String code = request.queryParams("code");
            Confirmation con = DatabaseManager.getManager().getConfirmationInfo(code);
            if (con != null) {
                User user = DatabaseManager.getManager().getUserFromId(con.getUserId());
                user.setEmailConfirmed(true);
                DatabaseManager.getManager().removeConfirmationInfo(code);
                DatabaseManager.getManager().updateUser(user);

                Logger.getLogger().api("Confirmed user email: " + user.getId(), request.ip());

                //Success... redirect to account page.
                response.redirect("/account", 301);
            } else {
                response.status(400);
                response.body("Invalid Code");
            }
        } else {
            response.status(400);
            response.body("Invalid Request");
        }
        return response.body();
    }

    public static String changeUsername(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("username") && body.has("password")) {
            Map<String, Object> map = AccountHandler.getHandler().getAccount(request.session().id());
            User user = (User) map.get("account");

            if (DatabaseManager.getManager().validLogin(user.getEmail(), body.getString("password"))) {

                if (!DatabaseManager.getManager().usernameTaken(body.getString("username"))) {
                    user.setUsername(body.getString("username"));

                    DatabaseManager.getManager().updateUser(user);

                    response.status(200);
                    response.body("Username successfully changed!");
                } else {
                    response.status(400);
                    response.body("Username already taken!");
                }
            } else {
                response.status(400);
                response.body("Invalid Password");
            }
        } else {
            response.status(400);
            response.body("Invalid Request");
        }
        return response.body();
    }

    public static String changeEmail(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("email") && body.has("password")) {
            Map<String, Object> map = AccountHandler.getHandler().getAccount(request.session().id());
            User user = (User) map.get("account");

            if (DatabaseManager.getManager().validLogin(user.getEmail(), body.getString("password"))) {
                if (!DatabaseManager.getManager().emailTaken(body.getString("email"))) {
                    user.setEmail(body.getString("email"));
                    user.setEmailConfirmed(false);

                    DatabaseManager.getManager().updateUser(user);

                    EmailHandler.getHandler().sendEmailConfirm(user.getEmail(), Generator.generateEmailConfirmationLink(user));

                    response.status(200);
                    response.body("Email successfully changed! Please reconfirm your email!");
                } else {
                    response.status(400);
                    response.body("That email is in use!");
                }
            } else {
                response.status(400);
                response.body("Invalid Password");
            }
        } else {
            response.status(400);
            response.body("Invalid Request");
        }
        return response.body();
    }

    public static String changePassword(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("old") && body.has("new")) {
            Map<String, Object> map = AccountHandler.getHandler().getAccount(request.session().id());
            User user = (User) map.get("account");

            if (DatabaseManager.getManager().validLogin(user.getEmail(), body.getString("old"))) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hash = encoder.encode(body.getString("new"));

                DatabaseManager.getManager().updateUser(user, hash);

                response.status(200);
                response.body("Password successfully changed!");
            } else {
                response.status(400);
                response.body("Invalid Password");
            }
        } else {
            response.status(400);
            response.body("Invalid Request");
        }
        return response.body();
    }
}