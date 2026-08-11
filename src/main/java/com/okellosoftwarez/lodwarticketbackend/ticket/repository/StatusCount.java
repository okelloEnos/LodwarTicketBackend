package com.okellosoftwarez.lodwarticketbackend.ticket.repository;

import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;

public interface StatusCount {

    TicketStatus getStatus();

    Long getCount();
}