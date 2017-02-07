'use strict'

angular.module('app')

.controller('MenuCtrl', function ($scope, $rootScope, $location, $cookies) {

	// -------------- menu set ------------------//
	$scope.datalists = [
		{name: '首页', href: 'dashboard', menuIcon: 'fa-dashboard'},
		{name: '用户管理', href: 'usermanage', menuIcon: 'fa-users'},
		{name: '系统模板管理', href: 'system-template-manage', menuIcon: 'fa-tasks'},
		{name: '任务记录', href: 'mail-delivery-task', menuIcon: 'fa-tasks'},
		{name: '交易记录', href: 'transactions', menuIcon: 'fa-book'},
		{name: '退信记录', href: 'mail-delivery-bounce-report', menuIcon: 'fa-tasks'},
		{name: '模版审核', href: 'mail-delivery-template-approve', menuIcon: 'fa-tasks'},
		{
			name: '通道管理', 
			childrenSign: 0,
			children: [ {
			    name: '通道',
			    href:'channels'
		    } ],
		    menuIcon: 'fa-anchor'
		},
		{name: '系统工具', href: 'mail-tools', menuIcon: 'fa-tasks'},
	];
	
	$scope.secMenuToggle = [false, false];
	
	$scope.toggleSec = function (item) {
		
		var childrenSign = item.datalist.childrenSign;
		
		for (var i = 0, j = $scope.secMenuToggle.length; i < j; i++) {
			if (i == item.datalist.childrenSign) {
				if ($scope.secMenuToggle[i]) {
					$scope.secMenuToggle[i] = false;
				} else {
					$scope.secMenuToggle[i] = true;
				}
				continue;
			}
			$scope.secMenuToggle[i] = false;
		}
		
		//$scope.secMenuToggle[childrenSign] = !$scope.secMenuToggle[childrenSign];
		
	}
	
	$scope.select = function(item) {
		if (!item.children) {
			$scope.secSelected = {};
		}
		if ($scope.selected != item) {
			$scope.selected = item; 
			$location.url(item.href);
		}
			
	};
	
	/*$scope.secSelect = function(item) {
		if ($scope.secSelected == item) {
			$scope.secSelected = item;
			$location.url(item.href);
		}
	}*/
	
	$scope.isActive = function(item) {
		return $scope.selected === item;
	};
	
	$scope.isSecActive = function(item) {
		return $scope.secSelected === item;
	};
	
	$rootScope.setMenuActive = function () {
		var currentPath = $cookies.get('currentPath');
		for (var i = 0, j = $scope.datalists.length; i < j; i++) { // 初始化menu选中
			var data = $scope.datalists[i];
			if (!currentPath) {
				currentPath = $location.path();
			}
			
			if (data.children) {
				for (var m = 0, n = data.children.length; m < n; m++) {
					if (currentPath === data.children[m].href) {
						$scope.secSelected = data.children[m];
						$scope.secMenuToggle[data.childrenSign] = true;
						$scope.selected = data;
						break;
					}
				}
			} else {
				if (currentPath === data.href) {
					$scope.selected = data;
					break;
				}
			}
		}
	}
	// -------------- end menu set ------------------//
	
});