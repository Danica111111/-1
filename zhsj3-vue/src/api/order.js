import request from './request'

export function rentCar(data) {
    return request.post('/orders/rent', data)
}

export function reserveCar(data) {
    return request.post('/orders/reserve', data)
}

export function returnCar(orderId) {
    return request.post(`/orders/return/${orderId}`)
}

export function payDeposit(orderId) {
    return request.post(`/orders/pay-deposit/${orderId}`)
}

export function getUserOrders(userId) {
    return request.get(`/orders/user/${userId}`)
}

export function cancelReserve(orderId) {
    return request.post(`/orders/cancel-reserve/${orderId}`)
}