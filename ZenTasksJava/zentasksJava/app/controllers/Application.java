package controllers;

import models.Project;
import models.Session;
import models.Task;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static play.data.Form.form;

/**
 * Created by Dany on 1/25/2016.
 */
public class Application extends Controller {
    public static long sessionId;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
    @Security.Authenticated(Secured.class)
    public static Result Index() {
        return ok(views.html.index.render(Project.find.all(), Task.find.all()));
    }

    public static Result login() {
        return ok(views.html.login.render(form(Login.class)));
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        Session currentSession = Session.find.where().eq("id",sessionId).findUnique();
        currentSession.endTime = LocalDateTime.now().toString();
        currentSession.isValid = false;
        currentSession.save();
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
    public static Result authenticate() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        play.data.Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        else {
            session().clear();
            session("email", loginForm.get().email);
            User userToSave = User.find.byId(loginForm.get().email);
            String userEmail = userToSave.email;
            LocalDateTime startDate = LocalDateTime.now();
            Boolean isValid = true;
            String token = makeSHA1Hash(randomString(12));
            User user = userToSave;
            sessionId++;
            Session.create(sessionId,userEmail,token,startDate.toString(),"",isValid,userEmail);
            return redirect("/");
        }
    }
    public static String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }

    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
