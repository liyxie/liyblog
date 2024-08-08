import request from '@/utils/request'

export function listSoftware() {
    return request({
        url: '/v1/software/',
        method: 'get',
    })
}
