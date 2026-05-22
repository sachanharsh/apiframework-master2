package entities;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import com.fasterxml.jackson.annotation.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet2 {
	private String createdAt;
	
    private long id;

    private String name;

    @Builder.Default
    private String status = "available";

    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    private Category category;

    @Builder.Default
    private List<Tags> tags = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private int id;
        @NonNull
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tags {
        private int id;
        private String name;
    }
    
}