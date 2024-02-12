package uz.ciasev.ubdd_service.dto.internal.request.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uz.ciasev.ubdd_service.entity.dict.article.ArticlePart;
import uz.ciasev.ubdd_service.exception.ErrorCode;

import javax.validation.constraints.NotNull;

@Data
public class ConsideredByOrganArticlePartRequestDTO {

    @NotNull(message = ErrorCode.ARTICLE_PART_REQUIRED)
    @JsonProperty(value = "id")
    private ArticlePart articlePart;

//    @NotNull(message = ErrorCode.IS_HEADER_ONLY_REQUIRED)
    private Boolean isHeaderOnly;
}
