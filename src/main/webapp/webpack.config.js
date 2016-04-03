var webpack = require("webpack");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = {
    entry: {
        "vendor": "./app/vendor",
        "app": "./app/boot",
        "styles": "./res"
    },
    output: {
        path: __dirname,
        filename: "./dist/[name].bundle.js",
        chunkFilename: "./dist/[id].js"
    },
    resolve: {
        extensions: ['', '.js', '.ts', '.css'],
        modulesDirectories: [
            'node_modules'
        ]
    },
    devtool: 'source-map',
    module: {
        loaders: [
            {
                test: /\.ts/,
                loaders: ['ts-loader'],
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader")
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"./dist/vendor.bundle.js", Infinity),
        new ExtractTextPlugin("./dist/[name].bundle.css")
    ]
}