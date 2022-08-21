package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dao.EventStorage;
import ru.yandex.practicum.javafilmorate.enums.EventType;
import ru.yandex.practicum.javafilmorate.enums.OperationType;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Event;

import java.util.Collection;

@Service
public class EventService {
    //private final UserService userService;
    private final EventStorage eventStorage;

    @Autowired
    public EventService(EventStorage eventStorage) {
       // this.userService = userService;
        this.eventStorage = eventStorage;
    }

    public Collection<Event> getEventForUser(int id) {
//        if (userService.findUserById(id) == null) {
//            throw new NotFoundException("Пользователь не найден");
//        }
        return eventStorage.getEventForUser(id);
    }

    public void addEvent(int userId, int entityId, EventType eventType, OperationType operation) {
        eventStorage.addEvent(userId, entityId, eventType, operation);
    }
}
