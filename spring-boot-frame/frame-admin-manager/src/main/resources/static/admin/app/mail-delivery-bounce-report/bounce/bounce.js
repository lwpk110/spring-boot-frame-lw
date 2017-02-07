'use strict'

angular.module('app')

.controller('RecipientBounceUrl', function ($scope, $rootScope, $routeParams, $http, $location, optionUrl, ErrorsService) {
	
	$scope.pageChanged = function () { // 单击页码按钮实现数据动态变化
		getData();
	};
	
	$scope.search = function () {
		$scope.searchFilter = $scope.searchOptionsData.selectedOption.value;
		getData();
	};
	
	function getData() { // 从后台获取数据
		
		$rootScope.startDate4 = $rootScope.startDate;
		$rootScope.endDate4 = $rootScope.endDate;
		
		var starDate = $rootScope.startDate;
		var endDate = $rootScope.endDate;
		
		var starDate3 = (!starDate) ? new Date().format('yyyy-MM-ddT00:00:00.000Z') : starDate.format('yyyy-MM-ddT00:00:00.000Z');
		var endDate3 = (!endDate) ? new Date().format('yyyy-MM-ddT23:59:59.000Z') : endDate.format('yyyy-MM-ddT23:59:59.000Z');
		
		$scope.loadDivVisible = true;
		$http.get(optionUrl.recipientBounceUrl, {
			withCredentials: true,
			params: {
				recipientsMailSuffix: $rootScope.recipientsMailSuffix ,
				start : starDate3 ,
				end : endDate3 
			}
		}).success(function (data) { // 成功从后台获取数据
			
			if(data.length==0){
				alert("暂无数据");
			}
			$scope.loadDivVisible = false;
			$scope.details = data;
			if (!$scope.details || $scope.details.length == 0) {
				$scope.details = null;
			} else {
				for (var i = 0; i < $scope.details.length; i++) {
					
					$scope.details[i].disabled = !$scope.details[i].disabled;
				}
			}
		}).error(function () {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			$scope.loadDivVisible = false;
		});
	} 
	
	if (!$rootScope.editModel) { // 在更新页面刷新的时候可以根据id重新找到服务器的数据，否则所有数据都清空
		var id = $routeParams.id;
		if (id) {
			$scope.getById(id);
		} else {
			getData(); // 初始化list数据
		}
	} else {
		if ($location.path().indexOf('edit') < 0)
			getData(); // 初始化list数据
	}
    
    $scope.backToBounceReportlList = function () {
    	$location.url('mail-delivery-bounce-report'); 
    };

});