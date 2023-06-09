package com.normanherman.c196.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Assessment model.  Stores assessment data.
 */
@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date date;
    private AssessmentType assessmentType;
    private int courseId;
    private boolean enabledNotifications;

    @Ignore
    public Assessment() {

    }

    @Ignore
    public Assessment(String title, Date date, AssessmentType assessmentType, int courseId) {
        this.title = title;
        this.date = date;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
        this.enabledNotifications = false;
    }

    public Assessment(String title, Date date, AssessmentType assessmentType) {
        this.title = title;
        this.date = date;
        this.assessmentType = assessmentType;
        this.enabledNotifications = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public boolean getEnabledNotifications() {
        return enabledNotifications;
    }

    public void setEnabledNotifications(boolean enableNotifications) {
        this.enabledNotifications = enableNotifications;
    }
}
