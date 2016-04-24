(function() {
    'use strict';

    angular
        .module('consoleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-access', {
            parent: 'entity',
            url: '/service-access',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.serviceAccess.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-access/service-accesses.html',
                    controller: 'ServiceAccessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceAccess');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-access-detail', {
            parent: 'entity',
            url: '/service-access/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.serviceAccess.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-access/service-access-detail.html',
                    controller: 'ServiceAccessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceAccess');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceAccess', function($stateParams, ServiceAccess) {
                    return ServiceAccess.get({id : $stateParams.id});
                }]
            }
        })
        .state('service-access.new', {
            parent: 'service-access',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-access/service-access-dialog.html',
                    controller: 'ServiceAccessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rateLimit: null,
                                methods: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-access', null, { reload: true });
                }, function() {
                    $state.go('service-access');
                });
            }]
        })
        .state('service-access.edit', {
            parent: 'service-access',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-access/service-access-dialog.html',
                    controller: 'ServiceAccessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceAccess', function(ServiceAccess) {
                            return ServiceAccess.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-access', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-access.delete', {
            parent: 'service-access',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-access/service-access-delete-dialog.html',
                    controller: 'ServiceAccessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceAccess', function(ServiceAccess) {
                            return ServiceAccess.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-access', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
