
package com.sortifytodoapp_backend.Controller;
import com.sortifytodoapp_backend.DTO.TimeTableDTO;
import com.sortifytodoapp_backend.Model.TimeTable;
import com.sortifytodoapp_backend.Service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {

    @Autowired
    private TimeTableService timeTableService;

    @GetMapping("/{userId}/{day}")
    public ResponseEntity<List<TimeTable>> getTimeTableByUserIdAndDay(
            @PathVariable Integer userId,
            @PathVariable String day) {
        List<TimeTable> timeTable = timeTableService.getTimeTableByUserIdAndDay(userId, day);
        return ResponseEntity.ok(timeTable);
    }

    @PostMapping("/addTask/{userId}")
    public ResponseEntity<TimeTable> addTaskToTimeTable(
            @PathVariable Integer userId,
            @RequestBody TimeTableDTO timeTableDTO) {
        TimeTable newTask = timeTableService.addTaskToTimeTable(userId, timeTableDTO);
        return ResponseEntity.ok(newTask);
    }

    @DeleteMapping("/deleteTask/{userId}")
    public ResponseEntity<String> deleteTaskFromTimeTable(
            @PathVariable Integer userId,
            @RequestBody TimeTableDTO timeTableDTO) {
        try {
            timeTableService.deleteTaskFromTimeTable(userId, timeTableDTO);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting task: " + e.getMessage());
        }
    }
}
