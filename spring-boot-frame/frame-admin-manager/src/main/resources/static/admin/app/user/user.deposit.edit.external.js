'use strict'

angular.module('app').
controller('DepositCtrl', function ($scope, $routeParams, $http, $location, optionUrl, ErrorsService) {
    $scope.params = $routeParams;
    $scope.deposit = function () {
        var url = optionUrl.depositUrl;
        url = url.replace('{id}', $routeParams.userId);
        $http({
            method: "post",
            url: url,
            data: {
                credits: $scope.credits
            },
            params: {
                username: $routeParams.username
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
});