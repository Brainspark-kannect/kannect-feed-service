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
@Table(name = "feed_like",
       uniqueConstraints = @UniqueConstraint(columnNames = {"feed_id", "user_id"}))
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class FeedLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    private Long userId;       // ID of the user who liked/disliked
    private boolean liked;     // true=like, false=dislike

    @CreatedDate
    private Instant reactedAt;
}

