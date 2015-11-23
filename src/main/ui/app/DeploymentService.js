(function() {

  angular.module('bacabs')
      .service('DeploymentService', DeploymentService);

  /**
   * @ngdoc service
   * @name DeploymentService
   *
   * @description
   * A service that provides the ability to interact with deployments.
   */
  function DeploymentService($http, CacheService, WebSocketService) {
    _init();

    return {
      getDeployments : getDeployments,
      getDeployment : getDeployment
    };

    ////////

    /**
     * @private
     *
     * @description
     * Initialization for the service that sets up various websocket listeners
     */
    function _init() {
      WebSocketService.registerListener("NewDeploymentEvent", _newDeploymentEventListener);
      WebSocketService.registerListener("UpdatedDeploymentEvent", _updatedDeploymentEventListener);
      WebSocketService.registerListener("RemovedDeploymentEvent", _removedDeploymentEventListener);
    }

    /**
     * @ngdoc method
     * @name DeploymentService#getDeployments
     * @returns Promise A promise that'll resolve with all known deployments
     *
     * @description
     * Get all deployments known in the system.  This method call utilizes a
     * cache, so will hit the backend once.
     */
    function getDeployments() {
      return CacheService.get("deployments", function(callback) {
        $http.get(CONTEXT_BASE + "/api/deployments")
            .success(function(data) {
              callback(data);
            });
      });
    };

    /**
     * @ngdoc method
     * @name DeploymentService#getDeployment
     * @param identifier The identifier of the deployment
     * @returns object The deployment, if found. Otherwise, null.
     *
     * @description
     * Get a specific deployment, based on its identifier
     */
    function getDeployment(identifier) {
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
    function _newDeploymentEventListener(event) {
      getDeployments().then(function(deployments) {
        deployments.push(event.deployment);
      })
    }

    /**
     * Event handler for UpdatedDeploymentEvent events
     * @param event The event details
     * @private
     */
    function _updatedDeploymentEventListener(event) {
      getDeployments().then(function(deployments) {
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
    function _removedDeploymentEventListener(event) {
      getDeployments().then(function(deployments) {
        var deployment = event.deployment;
        for (var i = 0; i < deployments.length; i++) {
          if (deployments[i].identifier == deployment.identifier) {
            deployments.splice(i, 1);
            return;
          }
        }
      });
    }

  };

})();
