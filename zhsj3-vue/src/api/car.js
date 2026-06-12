import request from './request'

export function getCarList() {
    return request.get('/cars')
}

export function searchCars(keyword) {
    return request.get('/cars/search', {
        params: {
            keyword
        }
    })
}