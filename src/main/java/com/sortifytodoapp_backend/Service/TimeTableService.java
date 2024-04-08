// TimeTableService.java
package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.TimeTableDTO;
import com.sortifytodoapp_backend.Model.TimeTable;
import com.sortifytodoapp_backend.Repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;


    //this logic create for get timetable task based on the userId and day
    public List<TimeTable> getTimeTableByUserIdAndDay(Integer userId, String day) {
        return timeTableRepository.findByUserIdAndDay(userId, day);
    }

    //this logic create for save timetable task to save to database
    public TimeTable addTaskToTimeTable(Integer userId, TimeTableDTO timeTableDTO) {
        TimeTable newTask = new TimeTable(userId,
                timeTableDTO.getDay(),
                timeTableDTO.getTaskName(),
                timeTableDTO.getStartTime(),
                timeTableDTO.getEndTime());
        return timeTableRepository.save(newTask);
    }


    //this logic delete timetable task based on the this craetaed prameter
    public void deleteTaskFromTimeTable(Integer userId, TimeTableDTO timeTableDTO) {
        // Find the task to delete based on the provided parameters
        Optional<TimeTable> optionalTask = timeTableRepository.findByUserIdAndDayAndTaskNameAndStartTimeAndEndTime(
                userId,
                timeTableDTO.getDay(),
                timeTableDTO.getTaskName(),
                timeTableDTO.getStartTime(),
                timeTableDTO.getEndTime()
        );

        if (optionalTask.isPresent()) {
            // Task found, delete it
            timeTableRepository.delete(optionalTask.get());
        } else {
            // Task not found, you may want to throw an exception or handle it accordingly
            throw new EntityNotFoundException("Task not found for deletion");
        }
    }
}
