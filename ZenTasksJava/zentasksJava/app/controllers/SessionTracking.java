package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Session;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 1/30/2016.
 */
public class SessionTracking extends Controller {
    public static Result trackAll() {
        return ok(Json.toJson(Session.find.all()));
    }
}
