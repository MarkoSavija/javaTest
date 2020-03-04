package com.example.demo.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.Machine;
import com.example.demo.model.MachineStatus;

public interface MachineRepository extends JpaRepository<Machine, Long> {
  
  @Query("SELECT m "
      + " FROM machines m "
      + " WHERE m.name like '%:name%'"
      + " AND m.status in :status"
      + " AND (createdAt BETWEEN :dateFrom AND :dateTo)")
  List<Machine> search(
      @Param("name") String name,
      @Param("status") List<MachineStatus> status,
      @Param("dateFrom") Date dateFrom,
      @Param("dateTo") Date dateTo);
}
