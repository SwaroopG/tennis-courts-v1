package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Api(tags = "Guests")
@RestController
@RequestMapping("/guests")
public class GuestController extends BaseRestController {
    private final GuestService guestService;

    @PostMapping(path = "/add")
    @ApiOperation(value = "Add a guest", tags = "Guests")
    public ResponseEntity<Void> addGuest(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
    }

    @GetMapping(path = "/find/{guestId}")
    @ApiOperation(value = "Find guest by Id", tags = "Guests")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping(path = "/findByName")
    @ApiOperation(value = "Find guests by name", tags = "Guests")
    public ResponseEntity<List<GuestDTO>> findGuestsByName(
        @NonNull
        @RequestParam String guestName,
        @ApiParam(name = "pageNumber", value = "Page number", defaultValue = "0")
        @RequestParam(defaultValue = "0") int pageNumber,
        @ApiParam(name = "limit", value = "Maximum number of results to retrieve per page.", defaultValue = "20")
        @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(guestService.findGuestsByName(guestName, pageNumber, limit));
    }

    @GetMapping(path = "/findAllGuests")
    @ApiOperation(value = "Find all guests", tags = "Guests")
    public ResponseEntity<PagedGuestResponse> findAllGuests(
        @ApiParam(name = "pageNumber", value = "Page number", defaultValue = "0")
        @RequestParam(defaultValue = "0") int pageNumber,
        @ApiParam(name = "limit", value = "Maximum number of results to retrieve per page.", defaultValue = "20")
        @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(guestService.findAllGuests(pageNumber, limit));
    }

    @PutMapping(path = "/update/{guestId}")
    @ApiOperation(value = "Update guest", tags = "Guests")
    public ResponseEntity<GuestDTO> updateGuest(
        @PathVariable Long guestId, @RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestId, guestDTO));
    }

    @DeleteMapping(path = "/delete/{guestId}")
    @ApiOperation(value = "Delete guest by Id", tags = "Guests")
    public void deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
    }
}
