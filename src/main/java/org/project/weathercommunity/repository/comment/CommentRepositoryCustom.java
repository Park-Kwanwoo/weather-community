package org.project.weathercommunity.repository.comment;

import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.post.Post;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getList(Post post);
}
