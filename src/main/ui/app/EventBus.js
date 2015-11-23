(function() {

  angular
      .module("bacabs")
      .service("EventBus", EventBus);


  /**
   * @ngdoc service
   * @name EventBus
   * @requires $rootScope
   *
   * @description
   * Provides a single location for anything to publish or listen to events
   */
  function EventBus($rootScope) {

    /**
     * @ngdoc method
     * @name EventBus#on
     * @param name {string} The name of the event to listen to
     * @param cb {function} The callback function to invoke. The callback will
     *                      be invoked with parameters 1) the event object and
     *                      2) any other arguments passed with the broadcast
     * @param $scope        An optional scope, to which a destroy listener will
     *                      be applied to remove the event listener
     * @returns {*}         A function used for deregistration
     *
     * @description
     * Registers an event listener for events with the provided name. If the
     * scope is provided, a $destroy listener is added to remove the event
     * listening.
     */
    this.on = function(name, cb, $scope) {
      // Wraps the original callback and removes the first parameter
      // that will be provided to it, as $rootScope.$emit sends an event
      // object first. However, this is an implementation detail and the
      // listening callbacks 1) don't know it should be there and 2) don't care.
      function createCb(cb) {
        return function() {
          var args = Array.prototype.slice.call(arguments);
          args.shift();
          cb.apply(null, args);
        };
      }

      var deregFn = $rootScope.$on(name, createCb(cb));
      if ($scope !== undefined) {
        $scope.$on('$destroy', deregFn);
      }
      return deregFn;
    };

    /**
     * @ngdoc method
     * @name EventBus#broadcast
     *
     * @description
     * Broadcast an event. The first argument must be the event name and all
     * other subsequent arguments are incldued in the broadcast.
     */
    this.broadcast = function() {
      $rootScope.$emit.apply($rootScope, arguments);
    };

  }

})();