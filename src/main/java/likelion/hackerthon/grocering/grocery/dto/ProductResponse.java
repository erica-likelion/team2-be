package likelion.hackerthon.grocering.grocery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 정보")
public record ProductResponse(@Schema(description = "상품 ID") Long id,
                              @Schema(description = "상품 이름") String name,
                              @Schema(description = "상품 이미지 URL") String imageUrl) {
}