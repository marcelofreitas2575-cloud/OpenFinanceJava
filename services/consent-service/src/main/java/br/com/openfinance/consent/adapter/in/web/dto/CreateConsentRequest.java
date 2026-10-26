package br.com.openfinance.consent.adapter.in.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateConsentRequest(
        @NotEmpty List<String> permissions
) {
}
