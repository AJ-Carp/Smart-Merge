package com.smartmerge.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenAIService {
    
    private final ChatClient chatClient;
    private final String systemPrompt = "You are a senior engineer. First provide a summary of what was done so that the engineer knows you understand the changes properly." +
                                        "Then review the files looking for bugs and stylistic issues. Feel free to make any suggestions.\n\n" +
                                        "Each file is contained by a main block (enclosed by equal signs). Within each main block, the files are seperated into three " + 
                                        "smaller blocks (enclosed by hyphens): File Path, File Contents, and File Patch (what has been changed). \n\nAdditionally, if you would like to make any inline " +
                                        "comments, please provide the file path, the line of the file you are commenting on (use the line number from the File Contents block, counting line 1 as the first line below \"...---File Contents---...\"."+
                                        "Only comment on lines that are visible in the File Patch.), and your comment at the very bottom of your response.\nDo not add any kind of title for the "+
                                        "inline comments section, as this section will be parsed programatically. Just begin listing them and make the format exactly as follows:\n" +

                                        "INLINE_COMMENT: src/main/java/Counter.java | 3 | I suggest replacing this code with for (int i = 0; ...) because ...\n" +
                                        "INLINE_COMMENT: src/main/java/Animal.java | 16 | This variable name is confusing because ...";
                                        
                                    

    public OpenAIService(OpenAiChatModel openAiChatModel) {
        chatClient = ChatClient.create(openAiChatModel);
    }

    public String prompt(String userPrompt) {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .content();
    }

    public String buildUserPrompt(List<String[]> fullFileContent) {
        StringBuilder userPrompt = new StringBuilder();

        for (String[] contents : fullFileContent) {
            userPrompt.append("==========================File===========================\n\n");

            userPrompt.append("------------------------File Path------------------------\n");
            userPrompt.append(contents[0] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("------------------------File Contents--------------------\n");
            userPrompt.append(contents[1] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("-------------------------File Patch----------------------\n");
            userPrompt.append(contents[2] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("=========================================================\n\n\n");
        }
        return userPrompt.toString();
    }

    public String parseMainReview(String response) {
        StringBuilder mainReview = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("INLINE_COMMENT:")) {
                    mainReview.append(line + "\n");
                }
            }
            
        } catch (IOException e) {
            log.error("Error parsing main review", e);
        }
        return mainReview.toString();
    }

    public List<String[]> parseInlineComments(String response) {
        List<String[]> inlineComments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("INLINE_COMMENT:")) {
                    line = line.replace("INLINE_COMMENT:", "");
                    String[] sub = new String[3];
                    buildSubArray(line, sub);
                    inlineComments.add(sub);
                }
            }

        } catch (IOException e) {
            log.error("Error parsing main review", e);
        }
        return inlineComments;
    }

    private void buildSubArray(String line, String[] sub) {
        StringBuilder filePath = new StringBuilder();
        StringBuilder position = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        int i = 0;
        char c = line.charAt(i);
        try {
            while (i < line.length() && c != '|') {
                filePath.append(c);
                c = line.charAt(++i);
            }
            c = line.charAt(++i);
            while (i < line.length() && c != '|') {
                position.append(c);
                c = line.charAt(++i);
            }
            c = line.charAt(++i);
            while (i < line.length() - 1) {
                comment.append(c);
                c = line.charAt(++i);
            }
            comment.append(c);
            sub[0] = filePath.toString().trim();
            sub[1] = position.toString().trim();
            sub[2] = comment.toString().trim();
        } catch (Exception e) {
            log.error("Malformed inline comments provided by AI", e);
        }
    }
}