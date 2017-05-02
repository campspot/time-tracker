package com.campspot.lib.mappers;

import com.campspot.api.Punch;
import com.campspot.dao.entities.PunchModel;

public class PunchMapper {
  public static Punch fromModel(PunchModel model) {
    return Punch.builder()
      .id(model.getId())
      .start(model.getStart())
      .end(model.getEnd())
      .category(model.getCategory())
      .description(model.getDescription())
      .build();
  }

  public static PunchModel fromAPI(Punch punch) {
    PunchModel model = new PunchModel();

    model.setStart(punch.getStart());
    model.setEnd(punch.getEnd());
    model.setCategory(punch.getCategory());
    model.setDescription(punch.getDescription());

    return model;
  }
}
