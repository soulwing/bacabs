(function() {

  var services = angular.module('bacabs.services.deploymentService', ['bacabs.services']);

  services.factory('DeploymentService', function($http, $filter, CacheService, WebSocketService) {
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

      WebSocketService.registerListener("UpdatedDeploymentEvent", function(event) {
        self.getDeployments().then(function(deployments) {
          var existingDeploymentIndex = deployments.reduce(function(current, value, index) {
            return current + (value.identifier == event.deployment.identifier ? index : 0);
          }, null);

          if (existingDeploymentIndex != null)
            deployments.splice(existingDeploymentIndex, 1, event.deployment);
          else
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
