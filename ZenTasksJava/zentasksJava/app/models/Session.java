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
    public String startTime;
    public String endTime;
    public Boolean isValid;

    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<User> members = new ArrayList<User>();


    public Session (long id,String userEmail, String token, String startTime, String endTime, Boolean isValid, User owner) {
        this.id = id;
        this.userEmail = userEmail;
        this.token = token;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isValid = true;
        this.members.add(owner);
    }
    public static Model.Finder<String, Session> find = new Model.Finder(Long.class, Session.class);

    public static Session create(long id,String userEmail, String token, String startTime, String endTime, Boolean isValid, String owner) {
        Session session = new Session(id,userEmail,token,startTime,endTime,isValid,User.find.ref(owner));
        session.save();
        session.saveManyToManyAssociations("members");
        return session;
    }
}
