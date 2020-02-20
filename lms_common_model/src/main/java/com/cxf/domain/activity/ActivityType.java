package com.cxf.domain.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ac_activity_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityType {
    @Id
    private String id;

    private String type;
}
