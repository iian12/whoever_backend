package com.jygoh.whoever.domain.rss.controller;

import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.rss.service.RssFeedService;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RssFeedController {

    private final RssFeedService rssFeedService;

    public RssFeedController(RssFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    @GetMapping(value = "/rss/{nickname}", produces = MediaType.APPLICATION_XML_VALUE)
    public void getRssFeed(@PathVariable String nickname, HttpServletResponse response)
        throws IOException, FeedException {
        List<Post> posts = rssFeedService.getPostByMemberNickname(nickname);
        SyndFeed rssFeed = new SyndFeedImpl();
        rssFeed.setFeedType("rss_2.0");
        rssFeed.setTitle(nickname + "'s Blog RSS Feed");
        rssFeed.setDescription("Latest posts from " + nickname);
        rssFeed.setLink("http://localhost:3000/member/profile/" + nickname);
        List<SyndEntry> entries = new ArrayList<>();
        for (Post post : posts) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(post.getTitle());
            entry.setLink("http://localhost:3000/posts/" + post.getId());
            entry.setPublishedDate(
                Date.from(post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(post.getContent());
            entry.setDescription(description);
            entries.add(entry);
        }
        rssFeed.setEntries(entries);
        SyndFeedOutput output = new SyndFeedOutput();
        response.setContentType("application/rss+xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(),
            StandardCharsets.UTF_8)) {
            output.output(rssFeed, writer);
        }
    }
}
