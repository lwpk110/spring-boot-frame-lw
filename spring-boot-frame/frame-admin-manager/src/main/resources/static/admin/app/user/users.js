'use strict'

angular.module('app')

.controller('UserCtrl', function ($scope, $http, $location, $cookies, optionUrl, ErrorsService) {
	
	if ($cookies.get('_current') == 'yes') {
		!$cookies.put('_current', null);
	} else {
		$cookies.put('currentPath', 'usermanage');
	}
	$scope.setMenuActive();
	$scope.headerTitle = '用户管理';
	
	// 初始化table参数
	$scope.currentPage = 1;
	$scope.maxSize = 6;
	$scope.itemsPerPage = 10;

	// --------------- option table  -------------------//

	$scope.filter = {
        type: 'text',
        placeholder: "请输入用户名关键字",
    };

    $scope.changeFilter = function () {
        switch ($scope.searchOptionsData.selectedOption.value) {
            case 'USER_ID':
                $scope.filter = {
                    type: 'number',
                    placeholder: "请输入用户ID",
                    pattern: "positiveIntReg"
                };
                break;
            case 'USERNAME':
                $scope.filter = {
                    type: 'text',
                    placeholder: "请输入用户名关键字",
                };
                break;
        }
    }

	function getData() { // 从后台获取数据
		$scope.loadDivVisible = true;
		$http.get(optionUrl.userListUrl, {
			withCredentials: true,
			params: {
				page: $scope.currentPage - 1,
				size: $scope.itemsPerPage,
				keyword: $scope.searchValue, 
				type: $scope.searchOptionsData.selectedOption.value
			}
		}).success(function(data) { // 成功从后台获取数据
			$scope.totalItems = data.totalElements;
			$scope.items = data.content;
			$scope.loadDivVisible = false;
		}).error(function() {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			$scope.loadDivVisible = false;
		});
	} 
	
	$scope.pageChanged = function() { // 单击页码按钮实现数据动态变化
		getData();
	};
	
	$scope.search = function() {
		$scope.searchFilter = $scope.searchOptionsData.selectedOption.value;
		getData();
	};
	
	$scope.searchOptionsData = {
		availableOptions: [
	      	{value: 'USERNAME', name: '用户名'}, 
	      	{value: 'USER_ID', name: 'ID'} 
	    ],
	    selectedOption: {value: 'USERNAME', name: '用户名'} //This sets the default value of the select in the ui
    };
	
	$scope.pageOptionsData = {
	    availableOptions: [
	      {name: '10', value: '10'},
	      {name: '30', value: '30'},
	      {name: '50', value: '50'},
	      {name: '80', value: '80'},
	      {name: '100', value: '100'}
	    ],
	    selectedOption: {name: '10', value: '10'} //This sets the default value of the select in the ui
    };
	
	if (!$scope.items) {
		getData();
	}
	
	$scope.changeShowSize = function () {
		$scope.itemsPerPage = $scope.pageOptionsData.selectedOption.value;
		getData();
	};
	
	// --------------- deposit  -------------------//
	
    $scope.title = '充值';
    
	$scope.initDeposit = function (id) {
		$scope.currentUserId = id;
    }
	
	$scope.deposit = function (id) {
		var url = optionUrl.depositUrl;
		url = url.replace('{id}', id);
		$http({
            method: "post",
            url: url,
            data: {
            	credits: $scope.credits
            },
            headers: {
            	'Content-Type': 'application/json;charset=utf-8' 
            }
        }).success(function (data, status) { // 成功与后台完成交互
        	if (status == 200) {
        		$scope.openAlert('success', data.user.username + '成功充值' + $scope.credits + '点', 5000);
        		$location.url('usermanage?v=' + Math.random());
        	}
        		
		}).error(function (data, status) {
			if (status == 400) {
				ErrorsService.parse(data);
			} else {
				$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			}
		});
	}
	// --------------- end deposit -------------------//

});



