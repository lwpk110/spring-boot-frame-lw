'use strict'

angular.module('app')

.controller('DashboardCtrl', function ($scope, $route, $location, $cookies) {
	$cookies.put('currentPath', 'dashboard');
	$cookies.put('_current', 'yes');
	$scope.gotoMainPanel = function(url) {
		$location.path(url);
	}
});