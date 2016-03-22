module.exports = function(config) {
    config.set({
        basePath: '../',
        frameworks: [ 'jasmine' ],
        files: [
            'libs/jquery/dist/jquery.js',
            'node_modules/angular/angular.js',
            'node_modules/angular-mocks/angular-mocks.js',
            'node_modules/lodash/dist/lodash.js',
            'app/**/*.js',
            'tests/**/*.js',
            'app/**/*.html'
        ],
        reporters: [ 'progress' ],
        colors: true,
        autoWatch: false,
        browsers: [ 'PhantomJS' ],
        singleRun: true,
        plugins: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
        ]
    });
};