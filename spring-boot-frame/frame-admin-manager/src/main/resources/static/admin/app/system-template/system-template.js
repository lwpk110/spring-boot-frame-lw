'use strict'

angular.module('app')

    .controller('SystemTemplateCtrl', function ($rootScope, $scope, $http, $window, $cookies, $location, $routeParams, $uibModal, $sce, ErrorsService, optionUrl) {


            $scope.currentPage = 1;
            $scope.maxSize = 6;
            $scope.itemsPerPage = 10;
            $rootScope.systemTemplateImageError = false;
            getData();

            function getData() { // 从后台获取数据
                $http.get(optionUrl.systemTemplates, {
                    withCredentials: true,
                    params: {
                        page: $scope.currentPage - 1,
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

            function getSystemTemplateImage() {
                $http({
                    method: "get",
                    url: optionUrl.getAllImage,
                }).success(function (data) { // 成功从后台获取数据
                    $rootScope.images = data;
                    $rootScope.edited = true;
                }).error(function () {
                    $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
                });
            }

            if (!$rootScope.images) {
                getSystemTemplateImage()
            }


            $scope.backToSystemTemplateList = function () {
                $location.url('system-template-manage');
            };


            $scope.pageChanged = function () { // 单击页码按钮实现数据动态变化
                getData();
            };

            $scope.edit = function (item) {
                $rootScope.edited = false;
                $rootScope.systemTemplate = {};
                $rootScope.editModel = item;
                $scope.systemTemplate.title = '编辑系统模板';
                $location.url('system-templates/edit?id=' + item.id);
            };

            $scope.submit = function (item) {
                $rootScope.editModel = item;
                $location.url('system-templates/edit?id=' + item.id);
            };

            $scope.getById = function (id) {
                var url = optionUrl.getSystemTemplateByIdUrl;
                url = url.replace('{id}', id);
                $http.get(url).success(function (data) { // 成功从后台获取数据
                    $scope.edit(data);
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

            $scope.selected = [];

            $scope.isChecked = function (item) {
                return $scope.selected.indexOf(item) >= 0;
            };

            $scope.updateSelection = function ($event, item) {
                var checkbox = $event.target;
                var checked = checkbox.checked;
                if (checked) {
                    $scope.selected.push(item);
                } else {
                    var idx = $scope.selected.indexOf(item);
                    $scope.selected.splice(idx, 1);
                }
            };


            $scope.save = function () {
                var url, method;
                if ($scope.editModel.id) {
                    url = optionUrl.updateSystemTemplate;
                    url = url.replace('{id}', $scope.editModel.id);
                    method = 'put';
                } else {
                    url = optionUrl.createSystemTemplate;
                    method = 'post';
                    if ($scope.selected.length == 2) {
                        var leftNum = $scope.selected[0].lastIndexOf("_"),
                            leftStr = $scope.selected[0].substring(0, leftNum + 1),
                            rightNum = $scope.selected[1].lastIndexOf("_"),
                            rightStr = $scope.selected[1].substring(0, rightNum + 1)
                        if (leftStr == rightStr) {
                            if ($scope.selected[0].charAt(leftNum + 1) != 'l') {
                                $scope.selected.reverse();
                            }
                        } else {
                            $rootScope.systemTemplateImageError = true;
                            return;
                        }
                    } else {
                        $rootScope.systemTemplateImageError = true;
                        return;
                    }
                }
                        $http({
                            method: method,
                            url: url,
                            data: {
                                name: $scope.editModel.name,
                                htmlContent: $scope.editModel.htmlContent,
                                portraits: $scope.selected
                            },
                            headers: {
                                'Content-Type': 'application/json;charset=utf-8'
                            }
                        }).success(function (data, status) { // 成功从后台获取数据
                            $scope.backToSystemTemplateList();
                            if (status == 201) {
                                getData();
                                $scope.openAlert('success', $scope.editModel.name + ' 系统模板新增成功', 5000);
                            } else {
                                $scope.openAlert('success', $scope.editModel.name + ' 系统模板更新成功', 5000);
                            }
                            $scope.editModel = {};
                        }).error(function (data, status) {
                            if (status == 400) {
                                ErrorsService.parse(data);
                            } else {
                                $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
                            }
                        });

            }
            ;

            $scope.delete = {
                start: function (item) {
                    $scope.deleteModel = item;
                    $scope.deleteModal = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/system-template/system-template-delete.html',
                        controller: "SystemTemplateCtrl",
                        scope: $scope
                    });
                },

                confirm: function () {
                    var url = optionUrl.deleteSystemTemplate.replace('{id}', $scope.deleteModel.id);
                    $http({
                        method: "delete",
                        url: url,
                    }).success(function (data, status) { // 成功从后台获取数据
                        $scope.deleteModal.close();
                        getData();
                        location.reload();
                        $scope.openAlert('success', $scope.deleteModal.name + ' 系统模板删除成功', 5000);
                    }).error(function (data, status) {
                        if (status == 400) {
                            ErrorsService.parse(data);
                        } else {
                            $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
                        }
                    });
                },

                close: function () {
                    $scope.deleteModal.close();
                }


            }

            $scope.createSystemTemplate = function () {
                $rootScope.systemTemplate = {};
                $rootScope.editModel = {};
                $scope.systemTemplate.title = '添加系统模板';
                getSystemTemplateImage();
                $location.url('system-templates/add');
            }

            $scope.preview = {
                start: function (item) {
                    $rootScope.previewHtmlContent = $sce.trustAsHtml(item.htmlContent);
                    $scope.previewModal = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/system-template/system-template-preview.html',
                        scope: $scope,
                        windowClass: "app-modal-window",
                        controller: "SystemTemplateCtrl"
                    });
                },

                close: function () {
                    $scope.previewModal.close();
                }
            }


        }
    )
;



