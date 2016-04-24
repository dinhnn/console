(function() {
    'use strict';

    angular
        .module('consoleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('external-service', {
            parent: 'entity',
            url: '/external-service',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.externalService.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/external-service/external-services.html',
                    controller: 'ExternalServiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('externalService');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('external-service-detail', {
            parent: 'entity',
            url: '/external-service/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.externalService.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/external-service/external-service-detail.html',
                    controller: 'ExternalServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('externalService');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExternalService', function($stateParams, ExternalService) {
                    return ExternalService.get({id : $stateParams.id});
                }]
            }
        })
        .state('external-service.new', {
            parent: 'external-service',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/external-service/external-service-dialog.html',
                    controller: 'ExternalServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                contact: null,
                                endpoint: null,
                                healthCheck: null,
                                rateLimit: null,
                                methods: null,
                                version: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('external-service', null, { reload: true });
                }, function() {
                    $state.go('external-service');
                });
            }]
        })
        .state('external-service.edit', {
            parent: 'external-service',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/external-service/external-service-dialog.html',
                    controller: 'ExternalServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExternalService', function(ExternalService) {
                            return ExternalService.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('external-service', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('external-service.delete', {
            parent: 'external-service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/external-service/external-service-delete-dialog.html',
                    controller: 'ExternalServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExternalService', function(ExternalService) {
                            return ExternalService.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('external-service', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
