package com.tenniscourts.guests;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagedGuestResponse {
    private List<GuestDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
