'use strict'

angular.module('app')

.controller('ChannelNodeCtrl', function ($scope, $rootScope, $routeParams, $http, $location, optionUrl, ErrorsService) {
	
	if ($location.search().channelId) {  
		$rootScope.channelId = $location.search().channelId;  
	} 

	$scope.setMenuActive();
	
	function getData() { // 从后台获取数据
		
		var url = optionUrl.listChannelNodesUrl;
		url = url.replace('{channelId}', $scope.channelId);
		$http.get(url).success(function (data) { // 成功从后台获取数据
			$scope.items = data;
			if (!$scope.items || $scope.items.length == 0) {
				$scope.items = null;
			} else {
				for (var i = 0; i < $scope.items.length; i++) {
					$scope.items[i].disabled = !$scope.items[i].disabled;
				}
			}
		}).error(function () {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
		});
	} 
	
	// ------------------- edit ----------------------- //
	
    $scope.updateChannelNode = function (item) {
    	$rootScope.channelNode = {}
		$scope.channelNode.title = '编辑节点';
    	$rootScope.editModel = item;
    	if (typeof($scope.editModel.configProps) != 'string')
    		$scope.editModel.configProps = JSON.stringify($scope.editModel.configProps); // 将object转为string
    	$location.url('channel/nodes/edit?channelId=' + $scope.channelId + '&id=' + item.id); 
        
    };
    
    $scope.getById = function (id) {
		var url = optionUrl.getChannelNodeByIdUrl;
		url = url.replace('{channelId}', $scope.channelId).replace('{id}', id);
		$http.get(url).success(function (data) { // 成功从后台获取数据
			$scope.updateChannelNode (data);
		}).error(function () {
			$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
		});
	}
	
	if (!$rootScope.editModel) { // 在更新页面刷新的时候可以根据id重新找到服务器的数据，否则所有数据都清空
		//console.log('你刷新了或者第一次进入channelNodes页面，呵呵');
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
    
    $scope.backToChannelNodeList = function () {
    	$location.url('channel/nodes?channelId=' + $scope.channelId);
    	
    };
    
    $scope.backToChannelList = function () {
    	$location.url('channels');
    };
    
    $scope.createChannelNode = function () {
    	$rootScope.channelNode = {}
    	$rootScope.editModel = {}; // 清空编辑模板
    	$scope.channelNode.title = '添加节点';
    	$location.url('channel/nodes/edit?channelId=' + $scope.channelId);
    };
    
    $scope.save = function () {
    	var url, method;
    	
    	if ($scope.editModel.id) {
    		url = optionUrl.updateChannelNodeUrl;
    		url = url.replace('{channelId}', $scope.channelId).replace('{id}', $scope.editModel.id);
    		method = 'put';
    	} else {
    		url = optionUrl.createChannelNodeUrl;
    		url = url.replace('{channelId}', $scope.channelId);
    		method = 'post';
    	}
    	$http({
            method: method,
            url: url,
            data: {
            	name: $scope.editModel.name,
            	serverKey: $scope.editModel.serverKey,
            	configProps: $scope.editModel.configProps,
				needCampaigns:$scope.editModel.needCampaigns==1?true:false
            },
            headers: {
            	'Content-Type': 'application/json;charset=utf-8' 
            }
        }).success(function (data, status) { // 成功从后台获取数据
        	$scope.backToChannelNodeList();
        	if (status == 201) {
				getData();
				$scope.openAlert('success', $scope.editModel.name + ' 节点新增成功', 5000);
			} else {
				$scope.openAlert('success', $scope.editModel.name + ' 节点更新成功', 5000);
			}
		}).error(function (data, status) {
			if (status == 400) {
				ErrorsService.parse(data);
			} else {
				$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
			}
		});
    }
    // ------------------- end edit ----------------------- //
    
    $scope.switchDisabled = function (id, disabled) {
    	
    	var url = optionUrl.changeChannelNodeStatusUrl;
    	url = url.replace('{channelId}', $scope.channelId).replace('{id}', id);
    	
    	$http({
    		method: "put",
    		url: url,
    		params: {
    			disabled: disabled
    		}
    	}).success(function (data, s) { // 成功从后台获取数据
        	if (s == 200) {
        		if (disabled) {
        			$scope.openAlert('success', data.name + ' 节点已经关闭', 5000);
        		} else {
        			$scope.openAlert('success', data.name + ' 节点已经开启', 5000);
        		}
			}
		}).error(function () {
    		$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
		});
    	
    }
    
});