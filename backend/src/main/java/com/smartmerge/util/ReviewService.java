package com.smartmerge.util;

import org.springframework.stereotype.Service;
import com.smartmerge.model.CommentDTO;
import lombok.RequiredArgsConstructor;
import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final GithubServiceCaller githubServiceCaller;
    
    public void postReview(String accessToken, String repoOwner, String repoName, int issueNumber, String mainReview, List<String[]> inlineComments) {
        List<CommentDTO.Comments> comments = generateComments(inlineComments);
        CommentDTO review = CommentDTO.builder()
            .body(mainReview)
            .event("COMMENT")
            .comments(comments)
            .build();

        String url = GITHUB_BASE_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls/" + issueNumber + "/reviews";
        githubServiceCaller.post(url, accessToken, review, null);
    }

    private List<CommentDTO.Comments> generateComments(List<String[]> inlineComments) {
        List<CommentDTO.Comments> comments = new ArrayList<>();
        for (String[] commentArray : inlineComments) {
            comments.add(CommentDTO.Comments.builder()
                .path(commentArray[0])
                .line(Integer.parseInt(commentArray[1]))
                .side("RIGHT")
                .body(commentArray[2])
                .build());
        }
        return comments;
    }
}
