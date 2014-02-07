module.exports = function(grunt) {

  // Project configuration.
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		jade: {
			compile: {
				options: {
					data: {
						debug: false
					},
					pretty: true,
				},
				files: {
					"app/index.html": ["app/*tmpl.jade"]
				}
			}
		},
		validation: {
        	options: {
                reset: grunt.option('reset') || true,
                stoponerror: false
        	},
        	files: {
                src: ['app/*.html']
        	}
		},
		sass: {                              	// Task
			dist: {                            	// Target
		    	options: {                      // Target options
		        	style: 'expanded'
		    	},
		    	files: {                        // Dictionary of files
			        'app/mainStyle.css': 'app/mainStyle.tmpl.scss'    // 'destination': 'source'
		      	}
		    }
		},
		watch: {
			htmlToIrpt : {
				files: ['**/*.tmpl.jade'],
				tasks: ['clear', 'jade', 'sass', 'validation'],
				options: {
					livereload: true,
					spawn: false
				}

			}
		}

	});
	
    // uglify: {
      // options: {
        // banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      // },
      // build: {
        // src: 'src/<%= pkg.name %>.js',
        // dest: 'build/<%= pkg.name %>.min.js'
      // }
    // }
  // });

  // Load the plugin that provides the "uglify" task.
  // grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-jade');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-html-validation');
	grunt.loadNpmTasks('grunt-clear');
	grunt.loadNpmTasks('grunt-sass');
  	
  	// Default task(s).
	grunt.registerTask('default', ['watch']); //'uglify'
//	grunt.registerTask('default', ['jade', 'validation']); //'uglify'
};