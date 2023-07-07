const path = require("path");
const { VueLoaderPlugin } = require("vue-loader");

module.exports = {
  mode: "development",
  entry: "./src/index.js",

  module: {
    rules: [
      {
        test: /\.vue$/,
        use: "vue-loader"
      },
      {
        test: /\.js$/,
        loader: "babel-loader"
      },
      {
        test: /\.css$/,
        use: ["vue-style-loader", "css-loader"]
      }
    ]
  },

  output: {
    path: path.join(__dirname, "./dist"),
    filename: "bundle.js"
  },

  devServer: {
    port: 19970,
    static: path.join(__dirname, 'public/'),
    devMiddleware: {
      publicPath: '/dist/'
    },
  },

  plugins: [
    // make sure to include the plugin for the magic
    new VueLoaderPlugin()
  ]
};

