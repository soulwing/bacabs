(function() {

  /**
   * A service that is used as a repository for Deployments
   */
  var DeploymentService = function($http, $filter, CacheService, WebSocketService) {
    var self = this;

    /**
     * Get all deployments known to the system
     * @returns Promise A promise that'll resolve with all known deployments
     */
    this.getDeployments = function() {
      return CacheService.get("deployments", function(callback) {
        $http.get(CONTEXT_BASE + "/api/deployments")
            .success(function(data) {
              callback(data);
            });
      });
    };

    /**
     * Get a specific deployment, based on its identifier
     * @param identifier The identifier of the deployment
     * @returns object The deployment, if found. Otherwise, null.
     */
    this.getDeployment = function(identifier) {
      var deployments = CacheService.getImmediate("deployments");
      if (deployments == null) return null;
      for (var i = 0; i < deployments.length; i++) {
        if (deployments[i].identifier == identifier)
          return deployments[i];
      }
      return null;
    };

    /**
     * Event handler for NewDeploymentEvent events
     * @param event The event payload
     * @private
     */
    this._newDeploymentEventListener = function(event) {
      self.getDeployments().then(function(deployments) {
        deployments.push(event.deployment);
      })
    }

    /**
     * Event handler for UpdatedDeploymentEvent events
     * @param event The event details
     * @private
     */
    this._updatedDeploymentEventListener = function(event) {
      self.getDeployments().then(function(deployments) {
        var existingDeploymentIndex = deployments.reduce(function(current, value, index) {
          return current + (value.identifier == event.deployment.identifier ? index : 0);
        }, null);

        if (existingDeploymentIndex != null)
          deployments.splice(existingDeploymentIndex, 1, event.deployment);
        else
          deployments.push(event.deployment);
      })
    }

    /**
     * Event handler for RemovedDeploymentEvent events
     * @param event The event details
     * @private
     */
    this._removedDeploymentEventListener = function(event) {
      self.getDeployments().then(function(deployments) {
        var deployment = event.deployment;
        for (var i = 0; i < deployments.length; i++) {
          if (deployments[i].identifier == deployment.identifier) {
            deployments.splice(i, 1);
            return;
          }
        }
      });
    }

    WebSocketService.registerListener("NewDeploymentEvent", this._newDeploymentEventListener);
    WebSocketService.registerListener("UpdatedDeploymentEvent", this._updatedDeploymentEventListener);
    WebSocketService.registerListener("RemovedDeploymentEvent", this._removedDeploymentEventListener);
  };

  angular.module('bacabs.services.deploymentService', ['bacabs.services.cacheService', 'bacabs.services.webSocketService'])
      .service('DeploymentService', DeploymentService);
})();
