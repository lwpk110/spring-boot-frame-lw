'use strict'

angular.module('app')

    .constant('optionUrl', {
        userListUrl: 'users',
        depositUrl: 'users/{id}/deposit',
        transactionListUrl: 'users/transactions',

        logoutUrl: '/logout',

        listChannelsUrl: 'mail-delivery-channels',
        updateChannelUrl: 'mail-delivery-channels/{id}',
        createChannelUrl: 'mail-delivery-channels',
        changeChannelDisabledUrl: 'mail-delivery-channels/{id}/change-disabled',
        getChannelByIdUrl: 'mail-delivery-channels/{id}',

        listChannelNodesUrl: 'mail-delivery-channels/{channelId}/nodes',
        createChannelNodeUrl: 'mail-delivery-channels/{channelId}/nodes',
        updateChannelNodeUrl: 'mail-delivery-channels/{channelId}/nodes/{id}',
        changeChannelNodeStatusUrl: 'mail-delivery-channels/{channelId}/nodes/{id}/change-disabled',
        getChannelNodeByIdUrl: 'mail-delivery-channels/{channelId}/nodes/{id}',

        mailDeliveryTasks: 'mail-delivery-tasks',
        mailDeliveryTaskTemplate: 'mail-delivery-tasks/{id}/template',
        mailDeliveryTaskReport: 'mail-delivery-tasks/{id}/report',

        recipientBounceSummaryReportUrl: 'mail-delivery-bounce-report/bounce/recipientBounceSummaryReport',
        recipientBounceUrl: 'mail-delivery-bounce-report/bounce/recipientBounce',

        mailTemplateApproveUrl: 'mail-template-approve/list',
        mailTemplateApproveCheckUrl: 'mail-template-approve/{id}/approve',
        executetimingtask: '/user/tool/report/execute_timing_task',

        systemTemplates:"system_templates/list",
        getSystemTemplateByIdUrl:"system_templates/{id}",
        updateSystemTemplate:"system_templates/{id}",
        deleteSystemTemplate:"system_templates/delete/{id}",
        createSystemTemplate:"system_templates",
        getAllImage:"system_templates/images"
    });

