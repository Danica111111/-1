import request from './request'

export function login(data) {
    return request.post('/auth/login', data)
}

export function register(data) {
    return request.post('/auth/register', data)
}

export function getUserInfo(userId) {
    return request.get(`/users/${userId}`)
}

export function updateUserInfo(userId, data) {
    return request.put(`/users/${userId}`, data)
}

export function updatePassword(data) {
    return request.put('/auth/password', data)
}