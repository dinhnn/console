(function() {
    'use strict';

    angular
        .module('consoleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gateway-module', {
            parent: 'entity',
            url: '/gateway-module',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.gatewayModule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gateway-module/gateway-modules.html',
                    controller: 'GatewayModuleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gatewayModule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gateway-module-detail', {
            parent: 'entity',
            url: '/gateway-module/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.gatewayModule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gateway-module/gateway-module-detail.html',
                    controller: 'GatewayModuleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gatewayModule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GatewayModule', function($stateParams, GatewayModule) {
                    return GatewayModule.get({id : $stateParams.id});
                }]
            }
        })
        .state('gateway-module.new', {
            parent: 'gateway-module',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gateway-module/gateway-module-dialog.html',
                    controller: 'GatewayModuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                artifact: null,
                                activated: null,
                                instances: null,
                                settings: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gateway-module', null, { reload: true });
                }, function() {
                    $state.go('gateway-module');
                });
            }]
        })
        .state('gateway-module.edit', {
            parent: 'gateway-module',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gateway-module/gateway-module-dialog.html',
                    controller: 'GatewayModuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GatewayModule', function(GatewayModule) {
                            return GatewayModule.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('gateway-module', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gateway-module.delete', {
            parent: 'gateway-module',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gateway-module/gateway-module-delete-dialog.html',
                    controller: 'GatewayModuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GatewayModule', function(GatewayModule) {
                            return GatewayModule.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('gateway-module', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
