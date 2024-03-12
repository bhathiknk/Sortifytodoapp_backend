
package com.sortifytodoapp_backend.Repository;
import com.sortifytodoapp_backend.Model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findByUserIdAndDay(Integer userId, String day);

    Optional<TimeTable> findByUserIdAndDayAndTaskNameAndStartTimeAndEndTime(
            Integer userId,
            String day,
            String taskName,
            String startTime,
            String endTime
    );
}
