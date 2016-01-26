package controllers;

import models.Project;
import models.Task;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;

import static play.data.Form.form;

/**
 * Created by Dany on 1/25/2016.
 */
public class Application extends Controller {
    @Security.Authenticated(Secured.class)
    public static Result Index() {
        return ok(views.html.index.render(Project.find.all(), Task.find.all()));
    }

    public static Result login() {
        return ok(views.html.login.render(form(Login.class)));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You have been logged out");
        return redirect("/login");
    }

    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect("/");
        }
    }
}
