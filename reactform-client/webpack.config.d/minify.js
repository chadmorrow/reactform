if (defined.PRODUCTION) {
    config.plugins.push(new webpack.DefinePlugin({
        'process.env.NODE_ENV': JSON.stringify('production')
    }));
    config.plugins.push(new webpack.optimize.UglifyJsPlugin({
        minimize: true
    }));
} else {
    config.plugins.push(new webpack.DefinePlugin({
        'process.env.NODE_ENV': JSON.stringify('development')
    }));
}