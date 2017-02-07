'use strict'

angular.module('app')

    .controller('MailDeliveryTaskCtrl', function ($scope, $http, $window, $cookies, optionUrl) {
        $cookies.put('currentPath', 'mail-delivery-task');

        $scope.setMenuActive();

        $scope.currentPage = 1;
        $scope.maxSize = 6;
        $scope.itemsPerPage = 10;
        /*$rootScope.startDate;
        $rootScope.endDate;*/
        $scope.dt = {
    		startDate: null,
    		endDate: null 
    	}

        $scope.filterOptions = {
            availableOptions: [
                {value: 'USERNAME', name: '用户名'},
                {value: 'USER_ID', name: '用户ID'},
                {value: 'TASK_ID', name: '任务ID'}
            ],
            selectedOption: {value: 'USERNAME', name: '用户名'}
        };

        $scope.filter = {
            type: 'text',
            placeholder: "请输入用户名",
        };

        $scope.changeFilter = function () {
            switch ($scope.filterOptions.selectedOption.value) {
                case 'USER_ID':
                    $scope.filter = {
                        type: 'number',
                        placeholder: "请输入用户ID",
                        pattern: "^[0-9]*$"
                    };
                    break;
                case 'USERNAME':
                    $scope.filter = {
                        type: 'text',
                        placeholder: "请输入用户名",
                    };
                    break;
                case 'TASK_ID':
                    $scope.filter = {
                        type: 'text',
                        placeholder: '请输入任务ID'
                    };
                    break;
            }
        }

        $scope.statusTranslator = function (status) {
            var value;
            switch (status) {
                case 100:
                    value = "成功";
                    break;
                case 2:
                    value = "撤销";
                    break;
                case 1:
                    value = "失败";
                    break;
                case 0:
                    value = "发送中";
                    break;
            }
            return value;
        }

        $scope.reportShow = function (item) {
            return (!item.scheduled && item.deliveryStatus==100) || (item.scheduled && item.scheduledDate < Date.now());
        }

        $scope.clockShow = function (item) {
            return item.scheduled && item.scheduledDate > Date.now();
        }


        function getData() { // 从后台获取数据
        	$scope.loadDivVisible = true;
            $http.get(optionUrl.mailDeliveryTasks, {
                withCredentials: true,
                params: {
                    page: $scope.currentPage - 1,
                    keyword: $scope.keyword,
                    type: $scope.filterOptions.selectedOption.value,
                    "startDate": $scope.dt.startDate,
                    "endDate": $scope.dt.endDate
                }
            }).success(function (data) { // 成功从后台获取数据
                $scope.totalItems = data.totalElements;
                $scope.items = data.content;
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
            getData();
        };

        if (!$scope.items) {
            getData();
        }

        $scope.openTemplate = function (id) {
            var url = optionUrl.mailDeliveryTaskTemplate.replace('{id}', id);
            $window.open(url);
        };
    });



