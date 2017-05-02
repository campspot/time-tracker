"use strict";

const Backbone = require("backbone");

const Punch = require('../models/punch-model');

module.exports = Backbone.Collection.extend({
  model: Punch,
  url: '/api/punches'
});
