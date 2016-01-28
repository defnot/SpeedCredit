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
    public Long Id;
    public String userEmail;
    public String token;
    public Long startTime;
    public Long endTime;
    public Boolean isValid;

    @ManyToOne
    User user;

    public static Finder<String, Session> find = new Finder<String,Session>(String.class, Session.class);



}
