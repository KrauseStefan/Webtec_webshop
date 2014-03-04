module.exports = function(grunt) {

	grunt.loadNpmTasks('grunt-contrib-connect');
	grunt.loadNpmTasks('grunt-connect-proxy');
	// grunt.loadNpmTasks('grunt-connect-rewrite');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-html-validation');
	grunt.loadNpmTasks('grunt-clear');


	// Project configuration.
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		// validation: {
		// 	options: {
		// 		reset: grunt.option('reset') || true,
		// 		stoponerror: true
		// 	},
		// 	files: {
		// 		src: ['app/*.html']
		// 	}
		// },
		// watch: {
		// 	files: ['**/*.tmpl.jade'],
		// 	tasks: ['clear', 'validation'],
		// 	options: {
		// 		livereload: true,
		// 		spawn: false

		// 	}
		// },
		connect: {
			server: {
				options: {
					port: 80,
					hostname: '0.0.0.0',
					base: '.',
					keepalive: true,
					middleware: function(connect, options) {
						var proxy = require('grunt-connect-proxy/lib/utils').proxyRequest;
						// var rewriteRulesSnippet = require('grunt-connect-rewrite/lib/utils').rewriteRequest;
						// connect.static.mime.define({
						// 	// 'text/html': ['irpt'],
						// 	'application/javascript': ['js'],
						// });
						return [
							proxy,
							// rewriteRulesSnippet,
							//connect.favicon('images/favicon.ico'),
							connect.static(options.base),
							connect.directory(options.base)
						];
					}
				},
				proxies: [{
					context: [
						'/Admin',
						'/rest'
					],
					host: 'localhost',
					port: 8080,
					https: false,
					changeOrigin: true,
					xforward: true
				}]
			}
			// rules: [{
			// 	from: '/(.*)$',
			// 	to: '^/client/WebContent/$1'
			// }]
			// rules: [{
			// 	from: '^/XMII/CM/ArlaPS_SMS_Module/(.*)$',
			// 	to: '/$1'
			// }]
		}

	});

	grunt.registerTask('default', ['configureProxies:server', 'connect']);
};