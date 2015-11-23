(function() {

  angular.module('bacabs', ['ui.router', 'bacabs.controllers', 'bacabs.services', 'templates'])
      .config(stateConfig);

  function stateConfig($stateProvider, $urlRouterProvider) {
    $urlRouterProvider
        .otherwise("/");

    $stateProvider
        .state('root', {
          url : "/",
          templateUrl : 'app/partials/app.html',
          controller : 'appController as ctrl',
          resolve : {
            deployments : function(DeploymentService) {
              return DeploymentService.getDeployments();
            }
          }
        });
  };
  
})();
