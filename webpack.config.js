"use strict";

module.exports = {
  entry: './client/src/app.js',
  output: {
    filename: './src/main/resources/assets/app.js'
  },
  module: {
    rules: [{
      test: /\.html$/,
      use: [{
        loader: 'html-loader'
      }]
    }]
  }
};
