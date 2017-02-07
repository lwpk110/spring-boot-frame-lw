'user strict';

angular.module('app')
    .controller('MailDeliveryTaskReportCtrl', function ($scope, $http, $uibModal, optionUrl) {
        $scope.openReport = function (id) {
            var url = optionUrl.mailDeliveryTaskReport.replace('{id}', id);
            $http.get(url, {
                withCredentials: true,
            }).success(function (data) {
                $scope.report = data;
                $scope.reportModal = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/mail-delivery-task/mail-delivery-task-report.html',
                    scope: $scope
                });
            }).error(function () {
                $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
            });
        };
        
        $scope.closeReport = function () {
            $scope.reportModal.close();
        }
    });