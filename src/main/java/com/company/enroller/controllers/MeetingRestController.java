package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @GetMapping(value = "")
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity<String>(
                    "Unable to add meeting. The ID: " + meeting.getId() + " already exists.",
                    HttpStatus.CONFLICT);
        }

        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/participants")
    public ResponseEntity<?> registerParticipantToMeeting(@PathVariable("id") long id,
                                                          @RequestBody Participant participant) {

        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<String>(
                    "Meeting with ID: " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant == null) {
            return new ResponseEntity<String>(
                    "Participant " + participant.getLogin() + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        meetingService.addParticipantToMeeting(foundMeeting, foundParticipant);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.CREATED);

    }

    @GetMapping(value="/{id}/participants")
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {

        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<String>(
                    "Meeting with ID: " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participants = meetingService.getParticipants(foundMeeting);
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }
}