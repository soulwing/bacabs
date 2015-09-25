'use strict';

describe("DeploymentService", function() {

  var $httpBackend, service, cacheService = new CacheService();

  function CacheService() {
    this.cbs = {};
    this.get = function(key, cb) { this.cbs[key] = cb; };
    this.immediate = function(key) { return this.cbs[key]; };
  }

  beforeEach(module('bacabs.services.deploymentService'));

  beforeEach(module(function($provide) {
    $provide.value('CacheService', cacheService);
  }));

  beforeEach(inject(function(DeploymentService, $injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = DeploymentService;
  }));

  it('should get deployments from the CacheService', function() {
    spyOn(cacheService, "get").and.callThrough();

    service.getDeployments();

    expect(cacheService.get).toHaveBeenCalledWith("deployments", jasmine.any(Function));
  });

});