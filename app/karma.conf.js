module.exports = function(config) {
  config.set({
    basePath : './',
    files : [
      "target/gulp-webapp/app/js/vendor*.js",
      "target/gulp-webapp/app/js/templates*.js",
      "target/gulp-webapp/app/js/app*.js",
      "target/gulp-webapp/bower_components/angular-mocks/angular-mocks.js",
      'src/main/ui/test/**/*.js'
    ],
    frameworks : ['jasmine'],
    autoWatch : false,
    browsers : ["PhantomJS"],
    reporters : ['progress', 'junit'],
    singleRun : true,
    plugins : [
      'karma-jasmine',
      'karma-phantomjs-launcher',
      'karma-junit-reporter'
    ],
    junitReporter : {
      outputFile : 'target/karma-reports/test-results.xml',
      suite : 'unit'
    }
  });
};