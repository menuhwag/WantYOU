package com.menu.wantyou.domain;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimestampsCreatedModified implements CreatedAt, ModifiedAt{
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    @PrePersist
    public void prePersist() {
        this.prePersistCreatedAt();
        this.prePersistModifiedAt();
    }

    @PreUpdate
    public void preUpdate() {
        this.preUpdateModifiedAt();
    }

    @Override
    public void prePersistCreatedAt() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public void prePersistModifiedAt() {
        this.modifiedAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public void preUpdateModifiedAt() {
        this.modifiedAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
