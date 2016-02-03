package controllers;

import models.Session;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Dany on 1/30/2016.
 */
public class SessionTracking extends Controller {
    @Security.Authenticated(Secured.class)
    public static Result track() {
        return ok(Json.toJson(Session.find.all()));
    }
}
