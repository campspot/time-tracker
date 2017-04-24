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
    let x = _.extend(this.model.toJSON(), {
      displayStart: this.model.get('start').format('HH:mm'),
      displayEnd: this.model.get('end').format('HH:mm')
    });

    console.log(x);

    return x;
  },

  updateStart() {
    console.log('start', this.$('.app-start').val());
  },

  updateEnd() {
    console.log('end', this.$('.app-end').val());
  },

  updateCategory() {
    console.log('category', this.$('.app-category').val());
  },

  updateDescription() {
    console.log('description', this.$('.app-description').val());
  }
});
