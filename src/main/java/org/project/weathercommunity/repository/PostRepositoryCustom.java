package org.project.weathercommunity.repository;

import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
