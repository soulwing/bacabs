(function() {

  var otherServices = [
     'bacabs.services.cacheService',
     'bacabs.services.deploymentService',
     'bacabs.services.webSocketService',
     'bacabs.services.notificationService'
  ];

  angular.module('bacabs.services', otherServices);
})();