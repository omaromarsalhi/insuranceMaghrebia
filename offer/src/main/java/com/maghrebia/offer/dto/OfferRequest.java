package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record OfferRequest(
        @NotBlank(message = "Offer name is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s\\-.,'()]{3,100}$",
                message = "Name must be 3-100 characters (letters, numbers, spaces, or basic punctuation)"
        )
        String name,

        @NotBlank(message = "Offer header is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s\\-.,'()]{5,200}$",
                message = "Header must be 5-200 characters (letters, numbers, spaces, or basic punctuation)"
        )
        String header,

//        @Pattern(
//                regexp = "^(http?://)?([\\w\\-]+\\.)+[a-z]{2,6}(/\\S*)?$",
//                message = "Invalid URL format (e.g., http://example.com/image.jpg)"
//        )
        String imageUri,

        @NotNull @Valid FilteredCategoryDto category,

        @Valid List<@Valid OfferLabelDto> labels
) {}
