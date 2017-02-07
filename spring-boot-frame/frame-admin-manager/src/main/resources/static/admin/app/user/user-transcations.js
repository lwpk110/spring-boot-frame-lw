'use strict'

angular.module('app')

.controller('TransactionCtrl', function ($scope, $http, $cookies, optionUrl) {
	
	$cookies.put('currentPath', 'transactions');
	$scope.setMenuActive();
	$scope.headerTitle = '交易记录';
	
	// 初始化table参数
	$scope.currentPage = 1;
	$scope.maxSize = 6;
	$scope.itemsPerPage = 10;

	// --------------- option table  -------------------//
	
	$scope.filter = {
        type: 'text',
        placeholder: "请输入用户名关键字",
    };
	
	$scope.changeSearchFrame = function () {
		
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
		    
		// 选择类型的时候自动清空表单数据
		$scope.dt = {};
	}

	$scope.dt = {
			startDate: null,
			endDate: null 
	}
	
	
	function getData() { // 从后台获取数据
		$scope.loadDivVisible = true;
		$http.get(optionUrl.transactionListUrl, {
			withCredentials: true,
			params: {
				page: $scope.currentPage - 1,
				size: $scope.itemsPerPage,
				keyword: $scope.searchValue, 
				type: $scope.searchOptionsData.selectedOption.value == 'time'? null: $scope.searchOptionsData.selectedOption.value,
				start: $scope.dt.startDate,
                end: $scope.dt.endDate
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
	      	{value: 'USER_ID', name: '用户ID'}
	    ],
	    selectedOption: {value: 'USERNAME', name: '用户名'} //This set the default value of the select in the ui
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

})

.filter('transactionTypeFilter', function () {
	
	return function(v) {
		switch (v) {
		case 'DEPOSIT':
			return '充值';
		case 'BALANCE_ASSIGN':
			return '余额分配';
		case 'SEND':
			return '发送邮件';
		case 'SEND_CANCEL':
			return '取消发送';
		}
    };
	
});