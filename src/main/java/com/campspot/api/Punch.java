package com.campspot.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import io.dropwizard.validation.ValidationMethod;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Punch {
  private final Long id;
  @NotNull
  private final DateTime start;
  @NotNull
  private final DateTime end;
  @NotNull
  private final String category;
  @NotNull
  private final String description;

  public Punch(Long id, DateTime start, DateTime end, String category, String description) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.category = category;
    this.description = description;
  }

  private Punch(Builder builder) {
    id = builder.id;
    start = builder.start;
    end = builder.end;
    category = builder.category;
    description = builder.description;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Punch copy) {
    Builder builder = new Builder();
    builder.id = copy.id;
    builder.start = copy.start;
    builder.end = copy.end;
    builder.category = copy.category;
    builder.description = copy.description;
    return builder;
  }

  public Long getId() {
    return id;
  }

  public DateTime getStart() {
    return start;
  }

  public DateTime getEnd() {
    return end;
  }

  public String getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  public static final class Builder {
    private Long id;
    private DateTime start;
    private DateTime end;
    private String category;
    private String description;

    private Builder() {
    }

    public Builder id(Long val) {
      id = val;
      return this;
    }

    public Builder start(DateTime val) {
      start = val;
      return this;
    }

    public Builder end(DateTime val) {
      end = val;
      return this;
    }

    public Builder category(String val) {
      category = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Punch build() {
      return new Punch(this);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", id)
      .add("start", start)
      .add("end", end)
      .add("category", category)
      .add("description", description)
      .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Punch punch = (Punch) o;
    return Objects.equals(id, punch.id) &&
      Objects.equals(start, punch.start) &&
      Objects.equals(end, punch.end) &&
      Objects.equals(category, punch.category) &&
      Objects.equals(description, punch.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, start, end, category, description);
  }

  @JsonIgnore
  @ValidationMethod(message = "Start must be before end")
  public Boolean isStartBeforeEnd() {
    return start.isBefore(end);
  }
}
