package likelion.hackerthon.grocering.grocery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

@Schema(description = "식료품점의 상품 목록 정보")
public record GroceryProductsResponse(@Schema(description = "식료품점 ID") Long groceryId,
                                      @Schema(description = "식료품점 이름") String groceryName,
                                      @Schema(description = "주소", example = "안산시 상록구 사동") String shotAddress,
                                      @Schema(description = "원산지", example = "인도") String country,
                                      @Schema(description = "오픈 시간") LocalTime openTime,
                                      @Schema(description = "마감 시간") LocalTime closeTime,
                                      @Schema(description = "상품 목록") List<ProductResponse> products) {
}