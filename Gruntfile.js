module.exports = function (grunt) {

    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        config: {
            path: {
                webapp: {
                    root: 'src/main/webapp'
                },
                temp: {
                    root: 'temp'
                },
                build: {
                    root: 'src/main/webapp/build'
                }
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            build: {
                src: '<%= config.path.webapp.root %>/app/*/*.js',
                dest: '<%= config.path.build.root %>/<%= pkg.name %>.min.js'
            }
        },

        jshint: {
            all: ['Gruntfile.js', 'app/*.js', 'app/**/*.js']
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

        concat: {
            options: {
                separator: ';'
            },
            dist: {
                src: ['<%= config.path.webapp.root %>/app/*/*.js'],
                dest: '<%= config.path.build.root %>/<%= pkg.name %>.js'
            }
        },

        watch: {
            dev: {
                files: ['Gruntfile.js', '<%= config.path.webapp.root %>/app/*/*.js', '<%= config.path.webapp.root %>/*.html'],
                tasks: ['jshint', 'karma:unit', 'concat:dist'],
                options: {
                    atBegin: true
                }
            },
            serve: {
                files: ['Gruntfile.js', '<%= config.path.webapp.root %>/app/*/*.js', '<%= config.path.webapp.root %>/*.html'],
                tasks: ['jshint', 'concat:dist'],
                options: {
                    atBegin: true
                }
            }
        }
    });

    grunt.registerTask('dev', ['watch:dev']);
    grunt.registerTask('serve', ['watch:serve']);
};