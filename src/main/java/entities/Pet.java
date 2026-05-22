package entities;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import com.fasterxml.jackson.annotation.*;          // JsonProperty, JsonAlias, JsonSetter, Nulls

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String createdAt;
	
	@JsonProperty("id")
    private long id;

    @JsonAlias("petName")
    @NonNull
    private String name;

    @Builder.Default
    private String status = "available";

    // 1) Default empty list
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    // 2) Fail deserialization if null comes from JSON (even if not @NonNull)
    @JsonSetter(nulls = Nulls.FAIL)
    private Category category;

    @Builder.Default
    private List<Tags> tags = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private int id;
        @NonNull
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tags {
        private int id;
        @JsonProperty(defaultValue = "sleepy")
        private String name;
    }
}