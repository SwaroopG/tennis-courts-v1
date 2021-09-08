package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.tenniscourts.TennisCourtService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Api(tags = "Schedules")
@RestController
@RequestMapping("/schedules")
public class ScheduleController extends BaseRestController {
    private final ScheduleService scheduleService;

    // Sample CURL for testing - curl -X POST "http://localhost:8080/schedules/add" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"startDateTime\": \"2021-09-08T04:28\", \"tennisCourtId\": 1}"
    @PostMapping(path = "/add")
    @ApiOperation(value = "Add schedule to a tennis court", tags = "Schedules")
    public ResponseEntity<Void> addSchedule(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @GetMapping(path = "/findByDates")
    @ApiOperation(value = "Find schedule by given date range", tags = "Schedules")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping(path = "/find/{scheduleId}")
    @ApiOperation(value = "Find schedule by Id", tags = "Schedules")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
