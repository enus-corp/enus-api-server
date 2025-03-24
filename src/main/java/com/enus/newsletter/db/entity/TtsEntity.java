/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.db.entity;

import com.enus.newsletter.enums.DetailLevel;
import com.enus.newsletter.enums.ReadSpeed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author idohyeon
 */
@Entity
@Table(name = "tts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TtsEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name="read_speed")
    private double readSpeed;

    @Column(nullable = false, name="detail_level")
    private int detailLevel;

    @Column(nullable = false, name="briefing_count")
    private int briefingCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_session_id", nullable=false)
    private ChatSessionEntity chatSession;
}
