"use strict";

require('fullcalendar');
var Marionette = require('backbone.marionette');
var Radio = require('backbone.radio');

var PUNCHES = require("../events.js").PUNCHES;
var Punch = require('../models/punch');

var punchChannel = Radio.channel(PUNCHES.channel);

module.exports = Marionette.View.extend({
  template: false,

  onRender: function () {
    var self = this;
    setTimeout(function () {
      self.$el.fullCalendar({
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
        select: self.timeSelected
      });
    });
  },

  timeSelected: function (start, end) {
    punchChannel.trigger(PUNCHES.events.NEW_PUNCH, new Punch({start: start, end: end}));
  }
});
