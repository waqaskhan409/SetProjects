package com.example.setprojects.model;

public class Projects {
    private String project_id;
    private String project_id_psdp;
    private String lat;
    private String lng;
    private String district;
    private String image_url;
    private String created_at;

    public Projects(String project_id, String project_id_psdp, String lat, String lng, String district, String image_url, String created_at) {
        this.project_id = project_id;
        this.project_id_psdp = project_id_psdp;
        this.lat = lat;
        this.lng = lng;
        this.district = district;
        this.image_url = image_url;
        this.created_at = created_at;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_id_psdp() {
        return project_id_psdp;
    }

    public void setProject_id_psdp(String project_id_psdp) {
        this.project_id_psdp = project_id_psdp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

