package com.menu.wantyou.domain;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimestampsCreated implements CreatedAt{
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.prePersistCreatedAt();
    }

    @Override
    public void prePersistCreatedAt() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
