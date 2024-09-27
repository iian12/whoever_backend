package com.jygoh.whoever.domain.post.service;

import com.jygoh.whoever.domain.category.CategoryService;
import com.jygoh.whoever.domain.comment.dto.CommentDto;
import com.jygoh.whoever.domain.comment.repository.CommentRepository;
import com.jygoh.whoever.domain.hashtag.dto.HashtagDto;
import com.jygoh.whoever.domain.hashtag.model.Hashtag;
import com.jygoh.whoever.domain.hashtag.repository.HashtagRepository;
import com.jygoh.whoever.domain.hashtag.service.HashtagService;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.post.dto.PostCreateRequestDto;
import com.jygoh.whoever.domain.post.dto.PostDetailResponseDto;
import com.jygoh.whoever.domain.post.dto.PostListResponseDto;
import com.jygoh.whoever.domain.post.dto.PostUpdateRequestDto;
import com.jygoh.whoever.domain.post.like.PostLike;
import com.jygoh.whoever.domain.post.like.PostLikeRepository;
import com.jygoh.whoever.domain.post.model.Post;
import com.jygoh.whoever.domain.post.repository.PostRepository;
import com.jygoh.whoever.domain.post.view.model.View;
import com.jygoh.whoever.domain.post.view.repository.ViewRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final static long VIEW_EXPIRATION_TIME = 60 * 5;
    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ViewRepository viewRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository postRepository, HashtagService hashtagService,
        JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository,
        ViewRepository viewRepository, PostLikeRepository postLikeRepository,
        CommentRepository commentRepository, HashtagRepository hashtagRepository,
        CategoryService categoryService) {
        this.postRepository = postRepository;
        this.hashtagService = hashtagService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.viewRepository = viewRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.hashtagRepository = hashtagRepository;
        this.categoryService = categoryService;
    }

    private String extractThumbnailUrl(String content) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);
        Document doc = Jsoup.parse(htmlContent);
        Element imgaElement = doc.select("img").first();
        if (imgaElement != null) {
            return imgaElement.attr("src");
        }
        return null;
    }

    @Override
    public Long createPost(PostCreateRequestDto requestDto, String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(requestDto.getHashtagNames());
        List<Long> hashtagIds = hashtags.stream().map(Hashtag::getId).collect(Collectors.toList());
        Long categoryId = requestDto.getCategoryId() != null ? requestDto.getCategoryId()
            : categoryService.createOrUpdateDefaultCategory(memberId);
        // 썸네일 처리
        String thumbnailUrl = extractThumbnailUrl(requestDto.getContent());
        // Post 생성
        Post post = requestDto.toEntity(author.getId(), thumbnailUrl, hashtagIds, categoryId);
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public Long updatePost(Long postId, PostUpdateRequestDto requestDto, String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getAuthorId().equals(memberId)) {
            throw new AccessDeniedException("You do not have permission to edit this post.");
        }
        List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(requestDto.getHashtagNames());
        List<Long> hashtagIds = hashtags.stream().map(Hashtag::getId).collect(Collectors.toList());
        String thumbnailUrl = extractThumbnailUrl(post.getContent());
        Long categoryId = requestDto.getCategoryId() != null ? requestDto.getCategoryId()
            : categoryService.createOrUpdateDefaultCategory(memberId);
        post.updatePost(requestDto.getTitle(), requestDto.getContent(), thumbnailUrl, hashtagIds,
            categoryId);
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public void deletePost(Long postId, String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getAuthorId().equals(memberId)) {
            throw new AccessDeniedException("You do not have permission to edit this post.");
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostListResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("createdAt")));
        Page<Post> postPage = postRepository.findAll(pageable);
        // 각 게시글의 작성자 닉네임을 조회하여 DTO로 변환
        return postPage.stream().map(post -> {
            // 작성자의 닉네임 조회
            String authorNickname = memberRepository.findById(post.getAuthorId())
                .map(Member::getNickname).orElse("Unknown");
            // Post와 닉네임을 DTO에 전달
            return PostListResponseDto.builder().post(post).authorNickname(authorNickname).build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<PostListResponseDto> getPostsByCategory(Long categoryId) {
        List<Post> posts = postRepository.findAllByCategoryId(categoryId);
        return posts.stream().map(post -> {
            String authorNickname = memberRepository.findById(post.getAuthorId())
                .map(Member::getNickname).orElse("Unknown");
            return PostListResponseDto.builder().post(post).authorNickname(authorNickname).build();
        }).collect(Collectors.toList());
    }

    @Override
    public PostDetailResponseDto getPostDetail(Long postId, String token) {
        Long memberId = null;
        if (token != null && !token.isEmpty()) {
            memberId = jwtTokenProvider.getMemberIdFromToken(token);
        }
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.incrementViewCount();
        postRepository.save(post);
        if (memberId != null) {
            Optional<View> existingView = viewRepository.findByMemberIdAndPostId(memberId, postId);
            View view;
            if (existingView.isPresent()) {
                view = existingView.get();
                view.update();
            } else {
                view = View.builder().memberId(memberId).postId(postId).build();
                viewRepository.save(view);
            }
        }
        // 포스트, 댓글 및 해시태그 정보를 조회
        String authorNickname = memberRepository.findById(post.getAuthorId())
            .map(Member::getNickname).orElse("Unknown");
        List<CommentDto> commentDtos = commentRepository.findByPostId(postId).stream()
            .map(comment -> new CommentDto(comment, memberRepository)).collect(Collectors.toList());
        List<HashtagDto> hashtagDtos = hashtagRepository.findAllById(post.getHashtagIds()).stream()
            .map(HashtagDto::new).collect(Collectors.toList());
        boolean existLike = false;
        if (memberId != null) {
            existLike = postLikeRepository.existsByPostIdAndMemberId(postId, memberId);
            log.info(String.valueOf(existLike));
        }
        return PostDetailResponseDto.builder().id(post.getId()).title(post.getTitle())
            .content(post.getContent()).authorNickname(authorNickname)
            .createdAt(post.getCreatedAt()).updatedAt(post.getUpdatedAt()).comments(commentDtos)
            .hashtags(hashtagDtos).likeCount(post.getLikeCount()).isLiked(existLike)
            .viewCount(post.getViewCount()).commentCount(post.getCommentCount()).build();
    }

    @Override
    public void toggleLike(Long postId, String token) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndMemberId(postId,
            memberId);
        if (existingLike.isPresent()) {
            // User has already liked the post, so remove the like
            post.decrementLikeCount();
            postRepository.save(post);
            postLikeRepository.delete(existingLike.get());
        } else {
            // User has not liked the post, so add a new like
            post.incrementLikeCount();
            postRepository.save(post);
            PostLike postLike = PostLike.builder().postId(postId).memberId(memberId).build();
            postLikeRepository.save(postLike);
        }
    }
}
