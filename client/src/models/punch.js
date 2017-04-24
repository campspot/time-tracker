"use strict";

var Backbone = require("backbone");

module.exports = Backbone.Model.extend({
  defaults: {
    start: null,
    end: null,
    category: '',
    description: ''
  }
});
