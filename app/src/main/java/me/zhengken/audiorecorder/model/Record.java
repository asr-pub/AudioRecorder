package me.zhengken.audiorecorder.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/11/5.
 * ClassName    : Record
 * Description  :
 */

@Entity
public class Record {

    @Id
    long id;

    String name;

    long duration;

    Date date;

    public Record() {
    }

    public Record(String name, long duration, Date date) {

        this.name = name;
        this.duration = duration;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getFormattedDuration() {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return formatter.format(duration);
    }

    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public long getDuration() {
        return duration;
    }
}
