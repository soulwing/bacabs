(function() {
  
  var app = angular.module('bacabs', ['ui.router', 'bacabs.controllers', 'bacabs.services'])
  
  app.config(function($stateProvider, $urlRouterProvider) {
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
    
  });

})();
