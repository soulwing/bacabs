(function() {

  angular.module('bacabs.controllers', ['bacabs.services', 'bacabs.directives', 'ui.bootstrap'])
      .controller('appController', AppController);

  function AppController(deployments, NotificationService, WebSocketService) {
    var vm = this;
    vm.deployments = deployments;
    vm.allowDesktopNotifications = NotificationService.isPermitted();
    vm.requestNotifications = requestNotifications;
    vm.stopNotifications = stopNotifications;
    vm.hasUnverifiedDeployment = hasUnverifiedDeployment;
    window.deployments = deployments;

    ////////

    /**
     * @ngdoc method
     * @name AppController#requestNotifications
     *
     * @description
     * Callback method to start the process to enable desktop notifications
     */
    function requestNotifications() {
      NotificationService.requestPermission().then(function(granted) {
        vm.allowDesktopNotifications = granted;
      });
    }

    /**
     * @ngdoc method
     * @name AppController#stopNotifications
     *
     * @description
     * Disable the usage of desktop notifications
     */
    function stopNotifications() {
      NotificationService.stopNotifications();
      vm.allowDesktopNotifications = false;
    }

    /**
     * @ngdoc method
     * @name AppController#hasUnverifiedDeployment
     * @returns {boolean} True if an unverified deployment exists
     *
     * @description
     * Is there an unverified deployment?
     */
    function hasUnverifiedDeployment() {
      for (var i = 0; i < vm.deployments.length; i++) {
        if (vm.deployments[i].status == 'UNKNOWN')
          return true;
      }
      return false;
    }
  };
})();
