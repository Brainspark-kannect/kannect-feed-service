package com.kannect.feed.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_media")
@Data @NoArgsConstructor @AllArgsConstructor
public class FeedMedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gcpUrl;      // GCP storage URL or path

    @OneToOne
    @JoinColumn(name = "feed_id", unique = true)
    private Feed feed;
}
