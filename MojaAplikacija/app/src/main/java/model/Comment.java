package model;

import java.util.Date;

import javax.net.ssl.SSLEngineResult;

/**
 * Created by marko on 17.4.18..
 */

public class Comment {

    private int id;
    private String title;
    private String description;
    private User author;
    private Date date;
    private Post post;
    private int likes;
    private int dislikes;
    private SSLEngineResult.Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public SSLEngineResult.Status getStatus() {
        return status;
    }

    public void setStatus(SSLEngineResult.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", date=" + date +
                ", post=" + post +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", status=" + status +
                '}';
    }

}
