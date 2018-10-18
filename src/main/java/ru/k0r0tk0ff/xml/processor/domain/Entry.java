package ru.k0r0tk0ff.xml.processor.domain;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */
public class Entry {
    private String depCode;
    private String depJob;
    private String description;

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
