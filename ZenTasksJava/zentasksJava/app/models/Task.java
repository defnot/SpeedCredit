package models;

import play.db.ebean.Model;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

/**
 * Created by Dany on 1/18/2016.
 */
public class Task extends Model {
    @Id
    public Long id;
    public String title;
    public boolean done = false;
    public Date dueDate;

    @ManyToOne
    public User assignedTo;
    @ManyToOne
    public Project project;

    public String folder;

    public static Model.Finder<Long, Task> find = new Model.Finder(Long.class, Task.class);

    public static List<Task> findTodoInvolving(String useremail) {
        return find.fetch("project").where().eq("done", false).eq("project.members.email", useremail).findList();
    }

    public static Task create(Task task, Long project, String folder) {
        task.project = Project.find.ref(project);
        task.folder = folder;
        task.save();
        return task;
    }
}
