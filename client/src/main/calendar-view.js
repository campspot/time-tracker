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
    $(() => {
      this.$el.fullCalendar({
        events: this.loadEvents.bind(this),
        timezone: 'local',
        timeFormat: 'h(:mm) A',
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
        allDaySlot: false,
        slotDuration: {minutes: 15},
        slotLabelInterval: {minutes: 60},
        slotLabelFormat: 'h(:mm) A',
        selectable: true,
        selectHelper: true,
        select: this.timeSelected.bind(this),
        eventOverlap: this.resolveOverlap.bind(this)
      });
    });
  },

  loadEvents(start, end, tz, callback) {
    return this.collection.fetch({
      data: $.param({
        start: start.toISOString(),
        end: end.toISOString(),
        _: new Date().getTime()
      })
    }).then(() => {
      const events = this.collection.models.map((model) => model.toEvent());

      return callback(events);
    });
  },

  timeSelected(start, end) {
    punchChannel.trigger(PUNCHES.events.NEW_PUNCH, new Punch({start: start, end: end}));
  },

  resolveOverlap(stillEvent, movingEvent) {
    return !stillEvent.punch;
  }
});
