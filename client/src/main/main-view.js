"use strict";

var Marionette = require('backbone.marionette');
var _ = require('underscore');

var ModalView = require('./modal-view');
var CalendarView = require('./calendar-view');

module.exports = Marionette.View.extend({
  template: _.template(require('./main.html')),

  regions: {
    modal: '#modal',
    calendar: '#calendar'
  },

  onRender: function () {
    this.showChildView('modal', new ModalView());
    this.showChildView('calendar', new CalendarView());
  }
});
