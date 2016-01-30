package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 1/28/2016.
 */
@Entity
public class Session extends Model {
    @Id
    public Long id;
    public String userEmail;
    public String token;
    public Long startTime;
    public Long endTime;
    public Boolean isValid;


    public Session (String userEmail, String token, Long startTime, Long endTime, Boolean isValid) {
        this.userEmail = userEmail;
        this.token = token;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isValid = true;
    }
    public static Model.Finder<String, Session> find = new Model.Finder(String.class, Session.class);

    public static Session create(String userEmail, String token, Long startTime, Long endTime, Boolean isValid) {
        Session session = new Session(userEmail,token,startTime,endTime,isValid);
        session.save();
        return session;
    }

}
