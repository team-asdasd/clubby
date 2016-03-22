module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        uglify: {
            options: {
                mangle: false
            },
            build: {
                src: 'app/*/*.js',
                dest: 'build/<%= pkg.name %>.min.js'
            }
        },

        jshint: {
            all: [ 'Gruntfile.js', 'app/*.js', 'app/**/*.js' ]
        },

        karma: {
            options: {
                configFile: 'karma.conf.js'
            },
            unit: {
                singleRun: true
            },
            continuous: {
                singleRun: false,
                autoWatch: true
            }
        },

        connect: {
            server: {
                options: {
                    hostname: 'localhost',
                    port: 8080
                }
            }
        },

        watch: {
            dev: {
                files: [ 'Gruntfile.js', 'app/*/.js', '*.html' ],
                tasks: [  ],
                options: {
                    atBegin: true
                }
            }
        }
    });

    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-karma');

    // Default task(s).
    grunt.registerTask('dev', [ 'connect:server', 'watch:dev' ]);
    grunt.registerTask('test', [ 'jshint', 'karma:continuous' ]);

};