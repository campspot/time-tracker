"use strict";

const Marionette = require('backbone.marionette');
const _ = require('underscore');
const moment = require('moment-timezone');

module.exports = Marionette.View.extend({
  template: _.template(require('./punch.html')),

  className: 'punch-form',

  events: {
    'change .app-start': 'updateStart',
    'change .app-end': 'updateEnd',
    'change .app-category': 'updateCategory',
    'change .app-description': 'updateDescription',
  },

  modelEvents: {
    'change': 'render'
  },

  serializeData() {
    return _.extend(this.model.toJSON(), {
      displayStart: this.model.get('start').format('HH:mm'),
      displayEnd: this.model.get('end').format('HH:mm')
    });
  },

  updateStart() {
    const startTime = this.$('.app-start').val();
    const startDate = this.model.get('start').format('YYYY-MM-DD');
    console.log(startDate, startTime);
    const start = moment(`${startDate}T${startTime}`);
    this.model.set('start', start);
  },

  updateEnd() {
    const endTime = this.$('.app-end').val();
    const endDate = this.model.get('end').format('YYYY-MM-DD');
    console.log(endDate, endTime);
    const end = moment(`${endDate}T${endTime}`);
    this.model.set('end', end);
  },

  updateCategory() {
    let category = this.$('.app-category').val();
    this.model.set('category', category);
  },

  updateDescription() {
    let description = this.$('.app-description').val();
    this.model.set('description', description);
  }
});
