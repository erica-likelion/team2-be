package likelion.hackerthon.grocering.grocery.dto;

import java.time.LocalTime;

public record SearchedGrocery(Long id,
                              String name,
                              String shotAddress,
                              String country,
                              LocalTime openTime,
                              LocalTime closeTime) {
}
