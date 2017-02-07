'use strict'

angular.module('app')

.controller('ChannelCtrl', function ($scope, $rootScope, $routeParams, $http, $location, optionUrl, $cookies, ErrorsService) {
	
	$cookies.put('currentPath', 'channels');
	$scope.setMenuActive();
	// 初始化table参数
	$scope.currentPage = 1;
	$scope.maxSize = 6;
	$scope.itemsPerPage = 10;

	// --------------- option table  -------------------//
	$scope.searchFrameVisible = true;
	$scope.changeSearchFrame = function () {
		switch ($scope.searchOptionsData.selectedOption.value) {
			case 'name':
			case 'id':
				$scope.searchFrameVisible = true;
				break;
			case 'queryDate':
				$scope.searchFrameVisible = false;
				break;
		}
	};

	function getData() { // 从后台获取数据
		$scope.loadDivVisible = true;
		$http.get(optionUrl.listChannelsUrl, {
			withCredentials: true,
			params: {
				page: $scope.currentPage - 1,
				size: $scope.itemsPerPage,
				name: $scope.searchValue || '' 
			}
		}).success(function (data) { // 成功从后台获取数据
			$scope.totalItems = data.totalElements;
			$scope.items = data.content;
			if (!$scope.items || $scope.items.length == 0) {
				$scope.items = null;
			} else {
				for (var i = 0; i < $scope.items.length; i++) {
					$scope.items[i].disabled = !$scope.items[i].disabled;
				}
			}
			$scope.loadDivVisible = false;
		}).error(function () {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			$scope.loadDivVisible = false;
		});
	} 
	
	$scope.pageChanged = function () { // 单击页码按钮实现数据动态变化
		getData();
	};
	
	$scope.search = function () {
		$scope.searchFilter = $scope.searchOptionsData.selectedOption.value;
		getData();
	};
	
	$scope.searchOptionsData = {
		availableOptions: [
	      	{value: 'name', name: '通道名称'}
	    ],
	    selectedOption: {value: 'name', name: '通道名称'} //This sets the default value of the select in the ui
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
	
	$scope.changeShowSize = function () {
		$scope.itemsPerPage = $scope.pageOptionsData.selectedOption.value;
		getData();
	};
	// --------------- end option table  -------------------//
	
	// ------------------- edit ----------------------- //
	
	$scope.editChannel = function (item) {
    	$rootScope.channel = {};
    	$scope.channel.title = '编辑通道';
    	$rootScope.editModel = item;
        $location.url('channels/edit?id=' + item.id); 
    };
	
	$scope.getById = function (id) {
		var url = optionUrl.getChannelByIdUrl;
		url = url.replace('{id}', id);
		$http.get(url).success(function (data) { // 成功从后台获取数据
			$scope.editChannel(data);
		}).error(function () {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
		});
	}
	if (!$rootScope.editModel) { // 在更新页面刷新的时候可以根据id重新找到服务器的数据，否则所有数据都清空
		//console.log('你刷新了或者第一次进入channels页面，呵呵');
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
    
    $scope.backToChannelList = function () {
    	$location.url('channels'); 
    };
    
    $scope.createChannel = function () {
    	$rootScope.channel = {};
    	$rootScope.editModel = {}; // 清空编辑模板
    	$scope.channel.title = '添加通道';
    	$location.url('channels/edit'); 
    };
    
    $scope.save = function () {
    	
    	var url, method;
    	
    	if ($scope.editModel.id) {
    		url = optionUrl.updateChannelUrl;
    		url = url.replace('{id}', $scope.editModel.id);
    		method = 'put';
    	} else {
    		url = optionUrl.createChannelUrl;
    		method = 'post';
    	}
    	
    	$http({
            method: method,
            url: url,
            data: {
            	name: $scope.editModel.name,
            	description: $scope.editModel.description,
            	fee: $scope.editModel.fee,
            	maxNumLimit: $scope.editModel.maxNumLimit
            },
            headers: {
            	'Content-Type': 'application/json;charset=utf-8' 
            }
        }).success(function (data, status) { // 成功从后台获取数据
        	$scope.backToChannelList();
        	if (status == 201) {
				getData();
				$scope.openAlert('success', $scope.editModel.name + ' 通道新增成功', 5000);
			} else {
				$scope.openAlert('success', $scope.editModel.name + ' 通道更新成功', 5000);
			}
        	$scope.editModel = {}; 
		}).error(function (data, status) {
			if (status == 400) {
				ErrorsService.parse(data);
			} else {
				$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			}
		});
    };
    // ------------------- end edit ----------------------- //
    
    $scope.switchDisabled = function (id, disabled) {
    	
    	var url = optionUrl.changeChannelDisabledUrl;
    	url = url.replace('{id}', id);
    	$http({
    		method: "put",
    		url: url,
    		params: {
    			disabled: disabled
    		}
    	}).success(function (data, s) { // 成功从后台获取数据
        	if (s == 200) {
        		if (disabled) {
        			$scope.openAlert('success', data.name + ' 通道已经关闭', 5000);
        		} else {
        			$scope.openAlert('success', data.name + ' 通道已经开启', 5000);
        		}
			}
		}).error(function () {
    		$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
		});
    	
    };
    
    $scope.gotoManageNodePage = function (parentId) {
    	$location.url('channel/nodes?channelId=' + parentId);
    }
    
});