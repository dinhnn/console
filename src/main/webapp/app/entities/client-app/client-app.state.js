(function() {
    'use strict';

    angular
        .module('consoleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-app', {
            parent: 'entity',
            url: '/client-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.clientApp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-app/client-apps.html',
                    controller: 'ClientAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientApp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-app-detail', {
            parent: 'entity',
            url: '/client-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'consoleApp.clientApp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-app/client-app-detail.html',
                    controller: 'ClientAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientApp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientApp', function($stateParams, ClientApp) {
                    return ClientApp.get({id : $stateParams.id});
                }]
            }
        })
        .state('client-app.new', {
            parent: 'client-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
                $mdDialog.show({
                    templateUrl: 'app/entities/client-app/client-app-dialog.html',
                    controller: 'ClientAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                contact: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-app', null, { reload: true });
                }, function() {
                    $state.go('client-app');
                });
            }]
        })
        .state('client-app.edit', {
            parent: 'client-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-app/client-app-dialog.html',
                    controller: 'ClientAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientApp', function(ClientApp) {
                            return ClientApp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-app', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-app.delete', {
            parent: 'client-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-app/client-app-delete-dialog.html',
                    controller: 'ClientAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientApp', function(ClientApp) {
                            return ClientApp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-app', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
