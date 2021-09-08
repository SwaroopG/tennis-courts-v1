package com.tenniscourts.reservations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.guests.GuestService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {
    @InjectMocks GuestService guestService;
    @Mock GuestMapper mockGuestMapper;
    @Mock GuestRepository mockGuestRepository;

    @Test
    public void testAddGuest() {
        GuestDTO expectedGuest = new GuestDTO();
        expectedGuest.setName("Guest 1");

        when(mockGuestMapper.map(mockGuestRepository.saveAndFlush(any(Guest.class)))).thenReturn(expectedGuest);

        GuestDTO actualGuest = guestService.addGuest(expectedGuest);
        assertEquals(expectedGuest.getName(), actualGuest.getName());
    }
}
