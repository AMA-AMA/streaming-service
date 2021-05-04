package com.epam.audio_streaming.repository.models;

import com.epam.audio_streaming.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

}