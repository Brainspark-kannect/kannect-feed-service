package com.kannect.feed.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reel_like",
       uniqueConstraints = @UniqueConstraint(columnNames = {"reel_id", "user_id"}))
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class ReelLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reel_id", nullable = false)
    private Reel reel;

    private Long userId;
    private boolean liked;

    @CreatedDate
    private Instant reactedAt;
}