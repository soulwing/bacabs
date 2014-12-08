(function() {
  
  var services = angular.module('bacabs.services.deploymentService', ['bacabs.services']);
  
  services.factory('DeploymentService', function($http, CacheService, WebSocketService) {
    return new (function() {
      var self = this;
      
      this.getDeployments = function() {
        return CacheService.get("deployments", function(callback) {
          $http.get(CONTEXT_BASE + "/api/deployments")
               .success(function(data) {
                 callback(data);
               });
        });
      };
      
      WebSocketService.registerListener("NewDeploymentEvent", function(event) {
        self.getDeployments().then(function(deployments) {
          deployments.push(event.deployment);
        })
      });

      WebSocketService.registerListener("RemovedDeploymentEvent", function(event) {
        self.getDeployments().then(function(deployments) {
          var deployment = event.deployment;
          for (var i = 0; i < deployments.length; i++) {
            if (deployments[i].identifier == deployment.identifier) {
              deployments.splice(i, 1);
              return;
            }
          }
        })
      });
    });
  });
  
})();
