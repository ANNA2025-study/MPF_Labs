package sumdu.edu.ua.web;


import sumdu.edu.ua.core.service.CommentService;
import sumdu.edu.ua.persistence.jdbc.JdbcCommentRepository;
import sumdu.edu.ua.persistence.jdbc.DbInit;

public class Beans {
    private static CommentService commentService;

    public static void init() {
        DbInit.init(); // schema.sql

        var repo = new JdbcCommentRepository();
        commentService = new CommentService(repo);
    }

    public static CommentService commentService() {
        return commentService;
    }
}
