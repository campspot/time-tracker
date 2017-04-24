"use strict";

module.exports = {
  entry: './client/src/app.js',
  output: {
    filename: './src/main/resources/assets/app.js'
  },
  node: {
    fs: "empty"
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
