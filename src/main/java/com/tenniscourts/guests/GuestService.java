package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO addGuest(GuestDTO guestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public GuestDTO findGuestById(Long guestId) {
        return guestRepository.findById(guestId).map(guestMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    public List<GuestDTO> findGuestsByName(String guestName, Integer pageNumber, Integer limit) {
        Pageable paging = PageRequest.of(pageNumber, limit);
        Page<Guest> results = guestRepository.findByName(guestName, paging);
        if(results != null) {
            return guestMapper.map(results.getContent());
        }
        return Collections.emptyList();
    }

    public PagedGuestResponse findAllGuests(Integer pageNumber, Integer limit) {
        Pageable paging = PageRequest.of(pageNumber, limit);
        Page<Guest> pagedResult = guestRepository.findAll(paging);
        if(pagedResult.hasContent()) {
            return PagedGuestResponse.builder()
                .content(guestMapper.map(pagedResult.getContent()))
                .page(pagedResult.getNumber())
                .totalElements(pagedResult.getTotalElements())
                .size(pagedResult.getSize()).last(pagedResult.isLast()).build();
        }
        return PagedGuestResponse.builder()
            .content(Collections.emptyList())
            .page(0)
            .totalElements(0)
            .size(0)
            .last(true).build();
    }

    public GuestDTO updateGuest(Long guestId, GuestDTO guestDTO) {
        guestDTO.setId(guestId);
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public void deleteGuest(Long guestId) {
        guestRepository.deleteById(guestId);
    }
}
