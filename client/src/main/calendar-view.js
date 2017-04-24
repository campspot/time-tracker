"use strict";

require('fullcalendar');
var Marionette = require('backbone.marionette');

module.exports = Marionette.View.extend({
  template: false,

  onRender: function () {
    var self = this;
    setTimeout(function () {
      self.$el.fullCalendar({
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'basicWeek,basicDay,listWeek'
        },
        defaultView: 'basicWeek',
        navLinks: true,
        editable: true,
        eventLimit: true
        // selectable: true,
        // selectOverlap: false,
        // selectHelper: true,
        // select: this.timeSelector
      });
    });
  }
});
