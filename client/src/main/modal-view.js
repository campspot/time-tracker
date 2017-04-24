"use strict";

const Marionette = require('backbone.marionette');
const _ = require('underscore');

module.exports = Marionette.View.extend({
  template: _.template(require('./modal.html')),

  className: 'closed',

  regions: {
    content: '#modalContent'
  },

  events: {
    'click .save': 'save',
    'click .close-modal': 'closeModal'
  },

  serializeData() {
    return {
      name: this.name
    };
  },

  showInModal(name, view, saveCallback) {
    this.$el.removeClass('closed');
    this.name = name;
    this.saveCallback = saveCallback;

    this.showChildView('content', view);
  },

  save() {
    if (this.saveCallback) {
      this.saveCallback();
    }
  },

  closeModal() {
    delete this.name;
    delete this.saveCallback;
    this.$el.addClass('closed');
    let child = this.getChildView('content');
    this.detachChildView('content');
    child.destroy();
  }
});
