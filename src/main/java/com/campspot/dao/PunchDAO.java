package com.campspot.dao;

import com.campspot.dao.entities.PunchModel;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.util.List;

public class PunchDAO extends AbstractDAO<PunchModel> {
  public PunchDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public PunchModel create(PunchModel punch) {
    return persist(punch);
  }

  public List<PunchModel> listForDates(DateTime start, DateTime end) {
    Criteria filter = criteria();

    filter
      .add(Restrictions.ge("start", start))
      .add(Restrictions.le("end", end));

    return list(filter);
  }

  public Boolean anyInRange(DateTime start, DateTime end) {
    Criteria filter = criteria();

    filter
      .add(Restrictions.ge("end", start))
      .add(Restrictions.le("start", end))
      .setProjection(Projections.count("id"));

    return (long) filter.uniqueResult() > 0;
  }
}
