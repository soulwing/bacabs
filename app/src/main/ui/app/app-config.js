(function() {

  // Performs route configuration for the application

  angular.module('bacabs')
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
