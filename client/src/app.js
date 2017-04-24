"use strict";

var $ = require('jquery');
require('fullcalendar');

$(function () {
  $('#calendar').fullCalendar({
    header: {
      left: 'prev,next today',
      center: 'title',
      right: 'month,basicWeek,basicDay'
    },
    navLinks: true,
    editable: true
  });
});
