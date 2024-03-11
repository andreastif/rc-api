package com.rc.rcapi.domains;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromptDto {

    private String ingredients;
    private String servings;
    private String language;
}
