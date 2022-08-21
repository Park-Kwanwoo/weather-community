package org.project.weathercommunity.repository.post;

import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
