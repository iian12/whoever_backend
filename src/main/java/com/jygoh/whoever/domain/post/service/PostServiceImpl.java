package com.jygoh.whoever.domain.post.service;

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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final static long VIEW_EXPIRATION_TIME = 60 * 5;
    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final ViewRepository viewRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final HashtagRepository hashtagRepository;

    public PostServiceImpl(PostRepository postRepository, HashtagService hashtagService,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider,
        MemberRepository memberRepository, ViewRepository viewRepository,
        PostLikeRepository postLikeRepository, CommentRepository commentRepository,
        HashtagRepository hashtagRepository) {
        this.postRepository = postRepository;
        this.hashtagService = hashtagService;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.viewRepository = viewRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.hashtagRepository = hashtagRepository;
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
        String thumbnailUrl = extractThumbnailUrl(requestDto.getContent());
        Post post = requestDto.toEntity(author.getId(), thumbnailUrl, hashtagIds);
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
        post.updatePost(requestDto.getTitle(), requestDto.getContent(), thumbnailUrl, hashtagIds);
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> postPage = postRepository.findAll(pageable);

        // 각 게시글의 작성자 닉네임을 조회하여 DTO로 변환
        return postPage.stream()
            .map(post -> {
                // 작성자의 닉네임 조회
                String authorNickname = memberRepository.findById(post.getAuthorId())
                    .map(Member::getNickname)
                    .orElse("Unknown");

                // Post와 닉네임을 DTO에 전달
                return PostListResponseDto.builder()
                    .post(post)
                    .authorNickname(authorNickname)
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Override
    public PostDetailResponseDto getPostDetail(Long postId, String token) {
        String redisKey = "postView:" + postId;
        // 사용자 ID를 가져오는 로직을 간소화
        String userId = null;
        if (token != null && !token.isEmpty()) {
            try {
                Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
                userId = (memberId != null) ? memberId.toString() : null;
            } catch (Exception e) {
                throw new RuntimeException("Error processing token: " + e.getMessage(), e);
            }
        }
        // Redis 키에 사용자 ID 추가
        if (userId != null) {
            redisKey += ":" + userId;
        }
        // Redis에서 조회 여부 확인
        Boolean hasViewed = redisTemplate.hasKey(redisKey);
        // 조회한 적이 없다면 조회수를 증가시키고 Redis에 키를 추가
        if (Boolean.FALSE.equals(hasViewed)) {
            Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            // 조회수 증가
            post.incrementViewCount();
            postRepository.save(post);
            // Redis에 키를 추가하고 일정 시간 후에 자동으로 만료되도록 설정
            redisTemplate.opsForValue()
                .set(redisKey, "true", VIEW_EXPIRATION_TIME, TimeUnit.SECONDS);
            // 사용자 ID가 있는 경우 View 엔티티 처리
            if (userId != null) {
                Long memberId = Long.parseLong(userId);
                // View 엔티티가 존재하는지 확인하고 필요 시 생성
                viewRepository.findByMemberIdAndPostId(memberId, postId).orElseGet(() -> {
                    View view = View.builder().memberId(memberId).postId(postId).build();
                    return viewRepository.save(view);
                });
            }
        }
        // 포스트, 댓글 및 해시태그 정보를 조회
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        String authorNickname = memberRepository.findById(post.getAuthorId())
            .map(Member::getNickname)
            .orElse("Unknown");
        List<CommentDto> commentDtos = commentRepository.findByPostId(postId).stream()
            .map(comment -> new CommentDto(comment, memberRepository)).collect(Collectors.toList());
        List<HashtagDto> hashtagDtos = hashtagRepository.findAllById(post.getHashtagIds()).stream()
            .map(HashtagDto::new).collect(Collectors.toList());
        return PostDetailResponseDto.builder().id(post.getId()).title(post.getTitle())
            .content(post.getContent()).authorNickname(authorNickname)
            .createdAt(post.getCreatedAt()).updatedAt(post.getUpdatedAt()).comments(commentDtos)
            .hashtags(hashtagDtos).viewCount(post.getViewCount())
            .commentCount(post.getCommentCount()).build();
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
