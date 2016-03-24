module.exports = function(config) {
    config.set({
        basePath: '../src/main/webapp/',
        frameworks: [ 'jasmine' ],
        files: [
            'bower_components/angular/angular.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/lodash/dist/lodash.js',
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
            'karma-jasmine'
        ]
    });
};