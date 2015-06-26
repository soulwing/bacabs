(function() {

  var stateConfig = function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider
        .otherwise("/");

    $stateProvider
        .state('root', {
          url : "/",
          templateUrl : 'app/partials/app.html',
          controller : 'appController',
          resolve : {
            deployments : function(DeploymentService) {
              return DeploymentService.getDeployments();
            }
          }
        });
  };
  
  angular.module('bacabs', ['ui.router', 'bacabs.controllers', 'bacabs.services'])
      .config(stateConfig);
})();
