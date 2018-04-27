package com.novamaday.afilehost.api.v1;

import com.novamaday.afilehost.objects.SiteSettings;
import com.novamaday.afilehost.utils.EmailHandler;
import de.triology.recaptchav2java.ReCaptcha;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class ContactEndpoint {
    public static String handle(Request request, Response response) {
        JSONObject body = new JSONObject(request.body());
        if (body.has("subject") && body.has("message") && body.has("name") && body.has("email") && body.has("gcap")) {
            if (new ReCaptcha(SiteSettings.RECAP_KEY.get()).isValid(body.getString("gcap"))) {
                String subject = body.getString("subject");
                String message = body.getString("message");
                String name = body.getString("name");
                String email = body.getString("email");


                EmailHandler.getHandler().sendContactEmail(subject, message, name, email);

                response.status(200);
                response.body("Success!");
            } else {
                response.status(400);
                response.body("Failed to verify ReCaptcha!");
            }
        } else {
            response.status(400);
            response.body("Bad Request");
        }
        return response.body();
    }
}