"use strict";

var $ = require('jquery');
var Marionette = require('backbone.marionette');

var MainView = require('./main/main-view');

var App = Marionette.Application.extend({
  region: '#app',

  onStart: function () {
    this.showView(new MainView());
  }
});

$(function () {
  var app = new App();
  app.start();
});
