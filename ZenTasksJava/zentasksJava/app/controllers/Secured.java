package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

import java.util.Date;

import static play.mvc.Controller.session;

/**
 * Created by Dany on 1/26/2016.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Context ctx) {
        String previousTick = session("userTime");
        if(previousTick != null && !previousTick.equals("")) {
            long previousT = Long.valueOf(previousTick);
            long currentT = Long.valueOf(new Date().getTime());
            long timeOut = Long.valueOf(Play.application().configuration().getString("sessionTimeout")) * 1000 * 60;
            if ((currentT - previousT) > timeOut) {
                session().clear();
                return null;
            }
        }

        String time = Long.toString(new Date().getTime());
            session("userTime",time);

        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized (Context ctx) {
        return redirect("/login");
    }
}
