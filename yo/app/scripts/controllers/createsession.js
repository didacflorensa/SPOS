'use strict';

/**
 * @ngdoc function
 * @name yoApp.controller:CreatesessionCtrl
 * @description
 * # CreatesessionCtrl
 * Controller of the yoApp
 */
angular.module('yoApp')
  .controller('CreatesessionCtrl', function ($scope, VirtualMachine) {
    $scope.vmConfig = {};
    $scope.predefinedVM = "";

    $scope.clearPredefinedVM = function(){
        $scope.predefinedVMForm.$setPristine();
        $scope.predefinedVMForm.$setValidity();
        $scope.predefinedVMForm.$setUntouched();
        $scope.predefinedVM = "";
    };

    $scope.clearVMConfig = function(){
      $scope.vmConfig = {};
      $scope.vmConfigForm.$setPristine();
      $scope.vmConfigForm.$setValidity();
      $scope.vmConfigForm.$setUntouched();
    };

    $scope.createSession = function() {
      if ($scope.predefinedVM == ""){
        $scope.vmConfig.realPercentage = $scope.vmConfig.realPercentage / 100;
        VirtualMachine.save($scope.vmConfig).$promise.then(function (vm) {
          alert(vm);
        });
        $scope.clearVMConfig();
      }
    };
  });