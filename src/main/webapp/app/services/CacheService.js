/**
 * @ngdoc module
 * @name summitDashboard.services.cacheService
 * @description
 *
 * # summitDashboard.services.cacheService
 *
 * A module that provides dependency injection for a caching service. Meant
 * mostly to be used as a backing service to other services, not for direct
 * UI-interaction.
 */

/**
 * @ngdoc service
 * @name CacheService
 * @kind class
 *
 * @description
 *   The caching service provides a single repository for key/value pairs. It
 *   is intended to be used by other services to ensure all code is working on
 *   the same models.
 *   
 *   For example, a summary service fetches all summaries and provides them to
 *   the UI controller. Those same summaries may need to be updated as events
 *   come in through a WebSocket. In order for the WebSocket to update the same
 *   model(s), those models need to be shared across the application.
 */

(function() {
  
  var services = angular.module('bacabs.services.cacheService', []);
  
  services.factory('CacheService', function($q, $http) {
    var dataCache = new (function() {
      
      /**
       * @ngdoc function
       * @name get
       * @kind function
       * 
       * @param String key The key for the data value
       * @param Function loadFunction A function to be invoked if the cache 
       * does not have a value for the provided key. The function's only 
       * argument will be a callback function, in which the loadFunction needs
       * to invoke and provide the value to be stored in the cache.
       * @return Deferred A promise in which the loaded value is provided.
       * 
       * @example
         cacheService.get('summaries', function(callback) {
           // Do something magical... like look them up!
           callback(lookedUpSummaries);
         }).then(function(summaries) { console.log(summaries); });
       */
      this.get = function(key, loadFunction) {
        var deferred = $q.defer();
        if (angular.isDefined(cache[key])) {
          deferred.resolve(cache[key]);
          return deferred.promise;
        }
        
        loadFunction(createCallback(key, deferred));
        return deferred.promise;
      };
      
      var cache = {};
      
      var createCallback = function(key, deferred) {
        return function(value) {
          if (angular.isDefined(cache[key])) {
            value = cache[key];
          }
          else {
            cache[key] = value;
          }
          deferred.resolve(value);
        };
      };
      
    });
    
//    window.dataCache = dataCache;
    return dataCache;
  });
  
  
})();
