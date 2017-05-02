package com.campspot.dao.entities;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Punch")
public class PunchModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private DateTime start;

  @NotNull
  private DateTime end;

  @NotBlank
  private String category;

  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DateTime getStart() {
    return start;
  }

  public void setStart(DateTime start) {
    this.start = start;
  }

  public DateTime getEnd() {
    return end;
  }

  public void setEnd(DateTime end) {
    this.end = end;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
