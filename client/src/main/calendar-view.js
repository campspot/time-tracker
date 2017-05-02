"use strict";

require('fullcalendar');
const $ = require('jquery');
const Marionette = require('backbone.marionette');
const Radio = require('backbone.radio');

const PUNCHES = require("../events.js").PUNCHES;
const Punch = require('../models/punch-model');

const punchChannel = Radio.channel(PUNCHES.channel);

module.exports = Marionette.View.extend({
  template: false,

  onRender() {
    let self = this;
    setTimeout(function () {
      self.$el.fullCalendar({
        events: self.loadEvents.bind(self),
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'agendaWeek,agendaDay'
        },
        defaultView: 'agendaWeek',
        navLinks: true,
        editable: true,
        eventLimit: true,
        nowIndicator: true,
        slotDuration: '00:15:00',
        slotLabelInterval: 15,
        slotLabelFormat: 'h(:mm)a',
        selectable: true,
        selectHelper: true,
        select: self.timeSelected.bind(this)
      });
    });
  },

  loadEvents(start, end, tz, callback) {
    return this.collection.fetch({
      data: $.param({
        start: start.toISOString(),
        end: end.toISOString()
      })
    }).then(() => {
      const events = this.collection.models.map((model) => model.toEvent());

      callback(events);
    });
  },

  timeSelected(start, end) {
    punchChannel.trigger(PUNCHES.events.NEW_PUNCH, new Punch({start: start, end: end}));
  }
});
