package com.okellosoftwarez.lodwarticketbackend.ticket.dto;

import java.util.List;

public record PageResponse<T>(List<T> data, PaginationMeta pagination) {}
