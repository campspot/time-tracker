"use strict";

const Backbone = require("backbone");
const moment = require("moment-timezone");

module.exports = Backbone.Model.extend({
  defaults: {
    id: null,
    start: null,
    end: null,
    category: '',
    description: ''
  },

  urlRoot: '/api/punches',

  toJSON() {
    let base = Backbone.Model.prototype.toJSON.apply(this, arguments);
    base.start = this.get('start').toISOString();
    base.end = this.get('end').toISOString();

    return base;
  },

  toEvent() {
    return {
      id: this.get('id'),
      start: this.get('start').toISOString(),
      end: this.get('end').toISOString(),
      title: this.get('description'),
      punch: true
    };
  },

  parse(response) {
    response.start = moment.tz(response.start, response.timeZone);
    response.end = moment.tz(response.end, response.timeZone);

    return response;
  }
});
