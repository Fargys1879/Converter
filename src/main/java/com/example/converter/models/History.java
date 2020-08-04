package com.example.converter.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String valuteInput, valuteOutput;
    float outputCourse;
    int inputCount;
    Date date;

    public History() {
    }

    public History(String valuteInput, String valuteOutput, float outputCourse, int inputCount, Date date) {
        this.valuteInput = valuteInput;
        this.valuteOutput = valuteOutput;
        this.outputCourse = outputCourse;
        this.inputCount = inputCount;
        this.date = date;
    }

    public String getValuteInput() {
        return valuteInput;
    }

    public void setValuteInput(String valuteInput) {
        this.valuteInput = valuteInput;
    }

    public String getValuteOutput() {
        return valuteOutput;
    }

    public void setValuteOutput(String valuteOutput) {
        this.valuteOutput = valuteOutput;
    }

    public float getOutputCourse() {
        return outputCourse;
    }

    public void setOutputCourse(float outputCourse) {
        this.outputCourse = outputCourse;
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
