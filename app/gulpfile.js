var del                    = require('del'),
    gulp                   = require('gulp'),
    angularFilesort        = require('gulp-angular-filesort'),
    angularTemplateCache   = require('gulp-angular-templatecache'),
    bower                  = require('gulp-bower'),
    concat                 = require('gulp-concat'),
    expect                 = require('gulp-expect-file'),
    finalhandler           = require('finalhandler')
    gulpIgnore             = require('gulp-ignore'),
    httpProxy              = require('http-proxy'),
    http                   = require('http'),
    inject                 = require('gulp-inject'),
    karma                  = require('karma').server,
    less                   = require('gulp-less'),
    livereload             = require('gulp-livereload'),
    merge                  = require('gulp-merge'),
    minifyHTML             = require('gulp-minify-html'),
    minifyCSS              = require('gulp-clean-css'),
    ngAnnotate             = require('gulp-ng-annotate'),
    rev                    = require('gulp-rev'),
    runSequence            = require('run-sequence'),
    serveStatic            = require('serve-static'),
    sourcemaps             = require('gulp-sourcemaps'),
    uglify                 = require('gulp-uglify'),
    watch                  = require('gulp-watch');



var ENV_PROD = "PROD";
var ENV_DEV = "DEV";
var ENV = ENV_DEV;

var SOURCE_BASE_DIR = "src/main/ui";
var BOWER_DIR = "target/gulp-webapp/bower_components";
var BUILD_BASE_DIR = "target/gulp-webapp/app";
var WEB_SERVER_ROOT = "target/gulp-webapp/app";

var PROXY_PATHS = [ "/api" ];


var source = {
  app : {
    files : [SOURCE_BASE_DIR + '/index.html'],
    watch : SOURCE_BASE_DIR + "/index.html"
  },
  scripts : {
    vendor : {
      name : "vendor.js",
      files : require('./vendor.json')
    },
    app : {
      files: [SOURCE_BASE_DIR + '/app/app.js', SOURCE_BASE_DIR + '/app/**/*.js'],
      watch: SOURCE_BASE_DIR + "/app/**"
    },
  },
  styles : {
    dir : "app/",
    files : [ SOURCE_BASE_DIR + '/less/app.less' ],
    watch : [ SOURCE_BASE_DIR + '/less/**/*.less', SOURCE_BASE_DIR + "/less/app.less" ]
  },
  templates : {
    files : [ SOURCE_BASE_DIR + '/app/**/*.html' ],
    watch : SOURCE_BASE_DIR + "/app/**/*.html"
  },
  index : {
    file : SOURCE_BASE_DIR + "/*.html"
  },
  test : {
    watch : SOURCE_BASE_DIR + "/test/**/*.js"
  }
};

var build = {
  bower : {
    dir : BOWER_DIR
  },
  scripts : {
    dir : BUILD_BASE_DIR + "/js",
    vendor : { name : "vendor.js" },
    app : { name : "app.js" }
  },
  styles : {
    dir : BUILD_BASE_DIR + "/css",
    theme : { name : "theme.css" },
  },
  templates : {
    dir : BUILD_BASE_DIR + "/js",
    name : "templates.js",
    rootPath : "app/"
  },
  index : {
    file : BUILD_BASE_DIR + "/index.html",
    ignore : "../../../target/gulp-webapp/app/"
  },
  dir : BUILD_BASE_DIR,
  watch : BUILD_BASE_DIR + "/**"
};


//////////////////////////////////
// Tasks
//////////////////////////////////

/**
 * Clean the webapp folder. Remove all the
 */
gulp.task('clean', function(callback) {
  return del([ BUILD_BASE_DIR ], callback);
});

/**
 * Install bower components
 */
gulp.task('bowerInstall', function(callback) {
  return bower();
});


/**
 * Concat all app scripts. Performs minification when running in prod mode
 */
gulp.task('scripts:app', function() {
  var scripts = gulp.src(source.scripts.app.files)
      .pipe(gulpIgnore.exclude("**/*Spec.js"))
      .pipe(angularFilesort())
      .pipe(concat(build.scripts.app.name));

  if (ENV == ENV_PROD) {
    scripts = scripts.pipe(ngAnnotate())
        .pipe(rev())
        .pipe(uglify());
  }

  return scripts.pipe(gulp.dest(build.scripts.dir));
});


/**
 * Combines all vendor/third-party scripts into a single minified vendor file
 */
gulp.task('scripts:vendor', function() {
  return gulp.src(source.scripts.vendor.files)
      .pipe(expect({ errorOnFailure: true }, source.scripts.vendor.files))
      .pipe(uglify())
      .pipe(concat(build.scripts.vendor.name))
      .pipe(rev())
      .pipe(gulp.dest(build.scripts.dir));
});


/**
 * Imports all templates into an angular template cache. If running in prod,
 * the templates are first minified
 */
gulp.task('templates', function() {
  var templates = gulp.src(source.templates.files);

  if (ENV == ENV_PROD)
    templates = templates.pipe(minifyHTML());

  templates = templates.pipe(angularTemplateCache(build.templates.name, {
    module : "templates", standalone: true, root : build.templates.rootPath
  }));

  if (ENV == ENV_PROD)
    templates.pipe(rev());

  return templates.pipe(gulp.dest(build.templates.dir));
});

/**
 * Compiles LESS files. If running in prod mode, files are minified.
 */
gulp.task('styles', function() {
  var styles = gulp.src(source.styles.files)
      .pipe(sourcemaps.init())
      .pipe(less({paths : [BOWER_DIR, source.styles.dir]}));

  if (ENV == ENV_PROD)
    styles = styles.pipe(minifyCSS({keepSpecialComments : 1, processImport : false}));

  return styles.pipe(sourcemaps.write('.'))
      .pipe(gulp.dest(build.styles.dir));
});


/**
 * Builds the index.html file. Adds in stylesheets and script tags
 */
gulp.task('app:index', function() {
  // Get all app sources and ensure they are included in the proper order for
  // the app to load (angularFileSor)
  var appScriptSources = gulp.src([build.dir + "/**/*.js"])
      .pipe(gulpIgnore.exclude("**/vendor*.js"))
      .pipe(angularFilesort());

  // Get the vendor script (which will need to go first) and stylesheets
  var otherSources = gulp.src([build.scripts.dir + "/vendor*.js",
    build.styles.dir + "/*.css"], {read: false});

  var sources = merge(otherSources, appScriptSources);

  // Do the actual script/stylesheet injections into the index page
  var index = gulp.src(source.index.file)
      .pipe(inject(sources, { ignorePath: build.index.ignore, relative : true }));

  if (ENV == ENV_PROD)
    index = index.pipe(minifyHTML());

  return index.pipe(gulp.dest(build.dir));
});

/**
 * A watch task.  Starts up a livereload webserver
 */
gulp.task('watch', function() {

  /*
   * Separate watch globs are needed here because wildcarding such as ** /*.js
   * doesn't work when new files are added (https://github.com/floatdrop/gulp-watch/issues/29)
   */
  watch(source.scripts.app.watch,   function() { gulp.start('scripts:app'); gulp.start('app:index'); });
  watch(source.templates.watch,     function() { gulp.start('templates') });
  watch(source.styles.watch,        function() { gulp.start('styles') });
  watch(source.app.watch,           function() { gulp.start('app:index') });
  watch(source.index.file,          function() { gulp.start('app:index') });
  watch(source.test.watch,          function() {gulp.start('test');});

  // Using livereload for listening because it's much more responsive than the
  // livereload found in the webserver
  livereload.listen();
  watch(build.watch, function(evt) {
    livereload.changed(evt);
  });

  gulp.start('serve');
});


gulp.task('serve', function() {
  var numProxyConfigs = PROXY_PATHS.length;

  var serve = serveStatic(WEB_SERVER_ROOT);  // static content handler
  var proxy = httpProxy.createProxyServer({ target : { host : 'localhost', port : '8080' }});  // proxy handler
  proxy.on('error', function (err, req, res) {
    if (err.code == 'ECONNREFUSED') {
      res.writeHead(503, {'Content-Type': 'text/plain'});
      res.end("Looks like the remote connection isn't established yet");
    } else {
      res.writeHead(500, {'Content-Type': 'text/plain'});
      res.end("Something happened while trying to proxy");
    }
  });

  var server = http.createServer(function(req, res) {  // actual webserver
    for (var i = 0; i < numProxyConfigs; i++) {
      if (req.url.indexOf(PROXY_PATHS[i]) > -1) {
        return proxy.web(req, res, { target: 'http://localhost:8080' });
      }
    }

    var done = finalhandler(req, res);
    serve(req, res, done);
  });

  server.on('upgrade', function (req, socket, head) {
    proxy.ws(req, socket, head);
  });

  server.listen(8000);
});


/**
 * Execute karma tests
 */
gulp.task('test', function(callback) {
  return karma.start({
    configFile : __dirname + '/karma.conf.js',
    singleRun : true
  }, function() {
    callback();
  });
});

/**
 * Performs a full build. Processing of all scripts and css is done in parallel,
 * after which the index.html page is built.
 */
gulp.task('build', function(callback) {
  console.log("Build using environment: " + ENV);
  return runSequence('clean',
      'bowerInstall',
      ['styles', 'scripts:vendor', 'scripts:app', 'templates'],
      'app:index',
      //'test',
      callback);
});


/**
 * Performs a full build for the production environment
 */
gulp.task('build:prod', function(callback) {
  ENV = ENV_PROD;
  return gulp.start('build', callback);
});


/**
 * The default task.  Performs a dev build and starts watching the files
 */
gulp.task('default', function(callback) {
  return runSequence('build', 'watch', callback);
});


// Error handler
function handleError(err) {
  console.log(err.toString());
  this.emit('end');
}