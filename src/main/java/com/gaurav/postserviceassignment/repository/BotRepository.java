package com.gaurav.postserviceassignment.repository;

import com.gaurav.postserviceassignment.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot,Long> {
}
