package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.Model.Event;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.EventRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService; // Assuming you have a UserService



    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getEventsByUserId(int userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        List<Event> userEvents = eventRepository.findByUser(user);

        // Format dates before returning the events
        userEvents.forEach(event -> {
            String eventDate = event.getEventDate();
            if (eventDate != null) {
                event.setEventDateStr(eventDate.format(String.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            } else {
                event.setEventDateStr(""); // Set an empty string or another default value
            }
        });

        return userEvents;
    }
    public void deleteEvent(int eventId) throws NotFoundException {
        Event event = getEventById(eventId);
        eventRepository.delete(event);
    }

    private Event getEventById(int eventId) throws NotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }

}