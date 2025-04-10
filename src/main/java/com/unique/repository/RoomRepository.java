package com.unique.repository;
import com.unique.entity.RoomEntity;
import com.unique.entity.AppealEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
}
