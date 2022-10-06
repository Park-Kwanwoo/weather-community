package org.project.weathercommunity.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.comment.QComment;
import org.project.weathercommunity.domain.post.Post;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getList(Post post) {

        return jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.post.eq(post))
                .orderBy(QComment.comment.id.asc())
                .fetch();
    }
}
