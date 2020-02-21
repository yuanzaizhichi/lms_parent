package com.cxf.domain.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ac_activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable {
    @Id
    private String id;

    private String name;

    private String place;

    private String scale;

    private Date startTime;

    private String introduce;

    private Date applyTime;

    private String type;

    private String communityId;

    private Integer state;

    private String score;

}
