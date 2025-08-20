package likelion.hackerthon.grocering.grocery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "조회된 식료품 정보")
public record SearchedGrocery(@Schema(description = "식료품 ID") Long id,
                              @Schema(description = "식료품 이름") String name,
                              @Schema(description = "주소", example = "안산시 상록구 사동") String shotAddress,
                              @Schema(description = "원산지", example = "인도") String country,
                              @Schema(description = "오픈 시간", example = "15:30:00.050, (시:분:초)") LocalTime openTime,
                              @Schema(description = "마감 시간", example = "15:30:00.050, (시:분:초)") LocalTime closeTime) {
}
