"use strict";

var Marionette = require('backbone.marionette');
var _ = require('underscore');

module.exports = Marionette.View.extend({
  template: _.template(require('./modal.html')),

  className: 'closed',

  regions: {
    content: '#modalContent'
  }
});
