"use strict";

const Marionette = require('backbone.marionette');
const Radio = require('backbone.radio');
const _ = require('underscore');

const PUNCHES = require("../events.js").PUNCHES;
const PunchCollection = require('../collections/punch-collection');
const ModalView = require('./modal-view');
const CalendarView = require('./calendar-view');
const PunchView = require('./punch-view');

const punchChannel = Radio.channel(PUNCHES.channel);

module.exports = Marionette.View.extend({
  template: _.template(require('./main.html')),

  regions: {
    modal: '#modal',
    calendar: '#calendar'
  },

  initialize() {
    this.listenTo(punchChannel, PUNCHES.events.NEW_PUNCH, this.fillInNewPunch);

    this.collection = new PunchCollection();
  },

  onRender() {
    this.showChildView('modal', new ModalView());
    this.showChildView('calendar', new CalendarView({collection: this.collection}));
  },

  fillInNewPunch(punch) {
    const punchView = new PunchView({model: punch});
    this.getChildView('modal').showInModal("Record Time", punchView, _.bind(punch.save, punch));
  }
});
