"use strict";

const Marionette = require('backbone.marionette');
const _ = require('underscore');

module.exports = Marionette.View.extend({
  template: _.template(require('./punch.html')),

  className: 'punch-form',

  events: {
    'change .app-start': 'updateStart',
    'change .app-end': 'updateStart',
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
    let start = this.$('.app-start').val();
    this.model.set('start', start);
  },

  updateEnd() {
    let end = this.$('.app-end').val();
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
